package com.github.zlbovolini.keymanager.removechavepix

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class RemoveChavePix(
    @field:NotBlank
    val chaveId: String,

    @NotBlank
    val clienteId: String,
)
