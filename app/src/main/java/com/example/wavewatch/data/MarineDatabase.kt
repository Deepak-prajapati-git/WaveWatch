package com.example.wavewatch.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "telemetry_logs")
data class TelemetryLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val waveHeight: Float,
    val wavePeriod: Float,
    val pitch: Float,
    val roll: Float,
    val speedKnots: Float,
    val alertLevel: String
)

@Entity(tableName = "sos_logs")
data class SosLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val packetId: String,
    val timestamp: Long,
    val lat: Double,
    val lng: Double,
    val alertLevel: String,
    val beaconStatus: String,
    val isAcknowledged: Boolean
)

@Dao
interface MarineDao {
    @Insert
    suspend fun insertTelemetry(entity: TelemetryLogEntity)

    @Query("SELECT * FROM telemetry_logs ORDER BY timestamp DESC LIMIT 100")
    fun getTelemetryLogs(): Flow<List<TelemetryLogEntity>>

    @Insert
    suspend fun insertSos(entity: SosLogEntity)

    @Query("SELECT * FROM sos_logs ORDER BY timestamp DESC")
    fun getSosLogs(): Flow<List<SosLogEntity>>

    @Query("UPDATE sos_logs SET isAcknowledged = :acknowledged WHERE packetId = :packetId")
    suspend fun updateSosAcknowledged(packetId: String, acknowledged: Boolean): Int
}

@Database(entities = [TelemetryLogEntity::class, SosLogEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun marineDao(): MarineDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wave_watch_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
