package uz.qmgroup.pharmabook.features.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [MedicineEntity::class],
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