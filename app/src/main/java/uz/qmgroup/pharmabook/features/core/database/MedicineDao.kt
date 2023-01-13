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
}