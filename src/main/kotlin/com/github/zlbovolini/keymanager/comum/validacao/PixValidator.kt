package com.github.zlbovolini.keymanager.comum.validacao

import com.github.zlbovolini.keymanager.comum.Chave
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import jakarta.inject.Singleton

private const val TAMANHO_MAXIMO_CHAVE = 77

@Singleton
class ChavePixValidator : ConstraintValidator<Pix, Chave> {

    override fun isValid(
        chave: Chave?,
        annotationMetadata: AnnotationValue<Pix>,
        context: ConstraintValidatorContext
    ): Boolean {
        if (chave == null || chave.valor.length > TAMANHO_MAXIMO_CHAVE) {
            return false
        }

        return chave.tipoChave.valida(chave.valor)
    }
}