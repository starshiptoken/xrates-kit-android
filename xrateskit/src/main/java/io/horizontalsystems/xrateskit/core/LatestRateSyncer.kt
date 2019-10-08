package io.horizontalsystems.xrateskit.core

import io.horizontalsystems.xrateskit.XRatesDataSource
import io.horizontalsystems.xrateskit.storage.LatestRate
import io.horizontalsystems.xrateskit.storage.RateInfo
import io.reactivex.disposables.Disposable

class LatestRateSyncer(
        private val storage: IStorage,
        private val factory: Factory,
        private val dataSource: XRatesDataSource,
        private val rateProvider: ILatestRateProvider)
    : SyncScheduler.Listener {

    var listener: Listener? = null
    var syncListener: ISyncCompletionListener? = null

    interface Listener {
        fun onUpdate(rate: RateInfo)
    }

    private var disposable: Disposable? = null

    fun sync() {
        if (disposable != null)
            return

        disposable = rateProvider.getLatestRate(dataSource.coins, dataSource.currency)
                .subscribe({
                    update(it)
                }, {
                    syncListener?.onFail()
                }, {
                    syncListener?.onSuccess()
                })
    }

    private fun update(rate: LatestRate) {
        listener?.onUpdate(factory.createRateInfo(rate))
        storage.saveLatestRate(rate)
    }

    //  Scheduler.Listener

    override fun onFire() {
        sync()
    }

    override fun onStop() {
        disposable?.dispose()
        disposable = null
    }
}
