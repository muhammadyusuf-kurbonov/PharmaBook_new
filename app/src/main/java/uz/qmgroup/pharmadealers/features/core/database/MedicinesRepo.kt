package uz.qmgroup.pharmadealers.features.core.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uz.qmgroup.pharmadealers.features.core.MedicineStorage
import uz.qmgroup.pharmadealers.models.Medicine

class MedicinesRepo(
    private val database: MedicineDatabase
): MedicineStorage {
    override suspend fun saveMedicine(medicine: Medicine) = withContext(Dispatchers.IO) {
        database.medicineDao.insert(medicine.toEntity())
    }

    override suspend fun removeOldMedicines(dealer: String) = withContext(Dispatchers.IO){
        database.medicineDao.deleteDealersMedicines(dealer)
    }

    suspend fun queryMedicines(query: String): List<Medicine> = withContext(Dispatchers.IO) {
        database.medicineDao.queryMedicines(query).map { it.toDomain() }
    }
}