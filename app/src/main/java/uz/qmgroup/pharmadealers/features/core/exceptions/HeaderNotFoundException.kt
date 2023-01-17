package uz.qmgroup.pharmadealers.features.core.exceptions

class HeaderNotFoundException(headerName: String): RuntimeException("Header for field $headerName was found found in headers row")