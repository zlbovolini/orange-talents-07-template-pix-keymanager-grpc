package com.github.zlbovolini.keymanager.comum.validacao

import com.github.zlbovolini.keymanager.comum.itau.ConsultaClienteHttpClient
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import jakarta.inject.Singleton

@Singleton
class ClienteItauValidator(private val consultaClienteHttpClient: ConsultaClienteHttpClient) :
    ConstraintValidator<ClienteItau, String> {

    override fun isValid(
        clienteId: String?,
        annotationMetadata: AnnotationValue<ClienteItau>?,
        context: ConstraintValidatorContext?
    ): Boolean {
        if (clienteId == null) {
            return true
        }

        val response = try {
            consultaClienteHttpClient.porId(clienteId)
        } catch (e: HttpClientResponseException) {
            return false
        }

        if (response.status().equals(HttpStatus.NOT_FOUND)) {
            return false
        }

        return true
    }
}