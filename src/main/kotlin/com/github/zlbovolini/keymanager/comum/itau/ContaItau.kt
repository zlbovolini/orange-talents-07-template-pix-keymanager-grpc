package com.github.zlbovolini.keymanager.comum.itau

import javax.validation.Constraint
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FIELD

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = [ContaItauValidator::class])
annotation class ContaItau(
    val message: String = "Conta Itaú não encontrada"
)
