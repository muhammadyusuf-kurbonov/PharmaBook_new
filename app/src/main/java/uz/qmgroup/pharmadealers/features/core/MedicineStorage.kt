package uz.qmgroup.pharmadealers.features.core

import uz.qmgroup.pharmadealers.models.Medicine

interface MedicineStorage {
    suspend fun saveMedicine(medicine: Medicine)

    suspend fun removeOldMedicines(dealer: String)
}