package com.github.zlbovolini.keymanager.comum.validacao

import javax.validation.Constraint
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FIELD

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = [UnicoValidator::class])
annotation class Unico(
    val message: String = "Chave Pix já registrada"
)