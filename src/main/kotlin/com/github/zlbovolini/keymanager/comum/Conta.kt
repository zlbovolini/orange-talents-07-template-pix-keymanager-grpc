package com.github.zlbovolini.keymanager.comum

import com.github.zlbovolini.keymanager.comum.validacao.ClienteItau
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Embeddable
class Conta(
    @field:NotNull
    @Enumerated(EnumType.STRING)
    val tipoConta: TipoConta,

    @field:NotBlank
    @field:ClienteItau
    val clienteId: String,
) {

    fun isDono(clienteId: String): Boolean {
        return clienteId.uppercase() == this.clienteId.uppercase()
    }
}