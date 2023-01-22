package uz.qmgroup.pharmadealers.features.core.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    version = 2,
    entities = [MedicineEntity::class],
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class MedicineDatabase: RoomDatabase() {
    companion object {
        @Volatile
        private var instance: MedicineDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                MedicineDatabase::class.java, "database.db")
                .build()
    }

    abstract val medicineDao: MedicineDao
}