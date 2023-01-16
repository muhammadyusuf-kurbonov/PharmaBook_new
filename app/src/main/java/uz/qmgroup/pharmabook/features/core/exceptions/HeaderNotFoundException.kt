package uz.qmgroup.pharmabook.features.core.exceptions

class HeaderNotFoundException(headerName: String): RuntimeException("Header for field $headerName was found found in headers row")