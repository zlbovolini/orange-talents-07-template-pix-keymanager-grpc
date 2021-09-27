package com.github.zlbovolini.keymanager.consultachavepix

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class ConsultaPorId(
    @field:NotBlank
    val chaveId: String,

    @field:NotBlank
    val clienteId: String
)