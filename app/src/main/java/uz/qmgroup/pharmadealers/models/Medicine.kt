package uz.qmgroup.pharmadealers.models

data class Medicine(
    val databaseId: Int,
    val id: String,
    val name: String,
    val manufacturer: String,
    val price: Double,
    val expireDate: String,
    val dealer: String
)
