package uz.qmgroup.pharmadealers.features.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MedicineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val medicineCode: String,
    @ColumnInfo(index = true)
    val name: String,
    val manufacturer: String,
    val expiringDate: String,
    val price: Double,
    val dealer: String
)