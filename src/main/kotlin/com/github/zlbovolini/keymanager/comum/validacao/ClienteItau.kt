package com.github.zlbovolini.keymanager.comum.validacao

import javax.validation.Constraint
import kotlin.annotation.AnnotationRetention.RUNTIME

@Retention(RUNTIME)
@Constraint(validatedBy = [ClienteItauValidator::class])
annotation class ClienteItau(
    val message: String = "Client Itaú não encontrado"
)
