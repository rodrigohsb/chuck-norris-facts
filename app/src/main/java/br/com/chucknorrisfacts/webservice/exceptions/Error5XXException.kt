package br.com.chucknorrisfacts.webservice.exceptions

/**
 * @rodrigohsb
 */
class Error5XXException(msg: String?) : Exception(msg ?: "ERROR 5XX")