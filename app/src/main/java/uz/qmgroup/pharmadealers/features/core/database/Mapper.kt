package uz.qmgroup.pharmadealers.features.core.database

import uz.qmgroup.pharmadealers.models.Medicine

fun Medicine.toEntity() = MedicineEntity(
    name = name,
    manufacturer = manufacturer,
    price = price,
    expiringDate = expireDate,
    medicineCode = id,
    id = databaseId,
    dealer = dealer
)

fun MedicineEntity.toDomain() = Medicine(
    name = name,
    manufacturer = manufacturer,
    price = price,
    expireDate = expiringDate,
    id = medicineCode,
    databaseId = id,
    dealer = dealer
)
