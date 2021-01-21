package io.horizontalsystems.xrateskit.storage

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.horizontalsystems.xrateskit.entities.*

@androidx.room.Database(version = 6, exportSchema = false, entities = [
    HistoricalRate::class,
    ChartPointEntity::class,
    MarketInfoEntity::class,
    GlobalCoinMarket::class,
    ProviderCoinInfo::class
])

@TypeConverters(DatabaseConverters::class)

abstract class Database : RoomDatabase() {
    abstract val providerCoinInfoDao: ProviderCoinInfoDao
    abstract val historicalRateDao: HistoricalRateDao
    abstract val chartPointDao: ChartStatsDao
    abstract val marketInfoDao: MarketInfoDao
    abstract val globalMarketInfoDao: GlobalMarketInfoDao

    companion object {
        fun create(context: Context): Database {
            return Room.databaseBuilder(context, Database::class.java, "x-rate-database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }
}
