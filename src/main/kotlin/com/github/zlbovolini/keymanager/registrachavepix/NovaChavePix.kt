package com.github.zlbovolini.keymanager.registrachavepix

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
data class NovaChavePix(
    @field:NotBlank
    val clienteId: String,

    @field:NotNull
    val tipoChave: TipoChave,

    @field:NotBlank
    @field:Size(max = 77)
    val chave: String,

    @field:NotBlank
    val tipoConta: TipoConta
) {
    fun toModel(): ChavePix {
        val chave = Chave(tipoChave, chave)
        val conta = Conta(tipoConta, clienteId)

        return ChavePix(chave, conta)
    }
}