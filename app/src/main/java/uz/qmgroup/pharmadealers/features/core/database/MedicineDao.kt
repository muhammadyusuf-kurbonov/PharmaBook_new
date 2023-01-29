package uz.qmgroup.pharmadealers.features.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.qmgroup.pharmadealers.features.core.models.MedicineDealersStats

@Dao
interface MedicineDao {
    @Insert
    suspend fun insert(medicineEntity: MedicineEntity)

    @Query("SELECT COUNT(*) FROM MedicineEntity")
    fun allMedicinesCount(): Flow<Int>

    @Query("SELECT COUNT(*) as medicinesCount, dealer FROM MedicineEntity GROUP BY dealer HAVING dealer in (:dealers);")
    fun dealersMedicinesCount(dealers: List<String>): Flow<List<MedicineDealersStats>>

    @Query("DELETE FROM MedicineEntity WHERE dealer=:dealer")
    suspend fun deleteDealersMedicines(dealer: String)

    @Query("SELECT * FROM MedicineEntity WHERE name LIKE '%' || :query || '%'")
    suspend fun queryMedicines(query: String): List<MedicineEntity>
}