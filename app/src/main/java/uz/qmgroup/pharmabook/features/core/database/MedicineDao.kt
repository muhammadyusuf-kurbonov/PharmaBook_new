package uz.qmgroup.pharmabook.features.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {
    @Insert
    suspend fun insert(medicineEntity: MedicineEntity)

    @Query("SELECT COUNT(*) FROM MedicineEntity")
    fun allMedicinesCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM MedicineEntity WHERE dealer=:dealer")
    fun dealersMedicinesCount(dealer: String): Flow<Int>

    @Query("DELETE FROM MedicineEntity WHERE dealer=:dealer")
    suspend fun deleteDealersMedicines(dealer: String)
}