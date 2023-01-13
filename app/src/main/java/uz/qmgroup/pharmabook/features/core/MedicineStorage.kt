package uz.qmgroup.pharmabook.features.core

import uz.qmgroup.pharmabook.models.Medicine

interface MedicineStorage {
    suspend fun saveMedicine(medicine: Medicine)
}