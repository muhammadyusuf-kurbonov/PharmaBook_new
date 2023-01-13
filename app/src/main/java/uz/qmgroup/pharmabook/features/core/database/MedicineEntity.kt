package uz.qmgroup.pharmabook.features.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MedicineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val medicineCode: String,
    val name: String,
    val manufacturer: String,
    val expiringDate: String,
    val price: Double
)