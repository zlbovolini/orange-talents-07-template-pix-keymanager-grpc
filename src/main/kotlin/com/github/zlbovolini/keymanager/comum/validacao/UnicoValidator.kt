package com.github.zlbovolini.keymanager.comum.validacao

import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import jakarta.inject.Singleton
import javax.persistence.*
import javax.transaction.Transactional

@Singleton
open class UnicoValidator(@PersistenceContext private val entityManager: EntityManager) : ConstraintValidator<Unico, String> {

    @Transactional
    override fun isValid(
        valor: String?,
        annotationMetadata: AnnotationValue<Unico>?,
        context: ConstraintValidatorContext?
    ): Boolean {
        if (valor == null) {
            return true
        }

        val query = "SELECT COUNT(p.id) = 0 FROM ChavePix p WHERE p.chave.valor = :valor"
        val isUnique: TypedQuery<java.lang.Boolean> = entityManager.createQuery(query, java.lang.Boolean::class.java)
        isUnique.setParameter("valor", valor)

        return isUnique.singleResult.booleanValue()
    }
}