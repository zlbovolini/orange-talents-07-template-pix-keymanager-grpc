package com.github.zlbovolini.keymanager.consultachavepix

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class ConsultaPorChave(
    @NotBlank
    val chave: String
)
