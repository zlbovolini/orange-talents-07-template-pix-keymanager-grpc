package com.github.zlbovolini.keymanager.comum.itau

import com.github.zlbovolini.keymanager.registrachavepix.Conta
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import jakarta.inject.Singleton

@Singleton
open class ContaItauValidator(
    private val consultaContaHttpClient: ConsultaContaHttpClient
) : ConstraintValidator<ContaItau, Conta> {

    override fun isValid(
        conta: Conta?,
        annotationMetadata: AnnotationValue<ContaItau>,
        context: ConstraintValidatorContext
    ): Boolean {
        if (conta == null) {
            return false
        }

        with(conta) {
            val response = try {
                consultaContaHttpClient.porClienteTipoConta(clienteId, tipo)
            } catch (e: HttpClientResponseException) {
                return false
            }

            if (response.status().equals(HttpStatus.NOT_FOUND)) {
                return false
            }
        }

        return true
    }
}