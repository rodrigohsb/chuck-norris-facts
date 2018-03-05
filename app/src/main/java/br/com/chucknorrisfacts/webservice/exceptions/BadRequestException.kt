package br.com.chucknorrisfacts.webservice.exceptions

/**
 * @rodrigohsb
 */
class BadRequestException(msg: String?) : Exception(msg ?: "BadRequest 404")