package com.github.zlbovolini.keymanager.comum.bancocentral

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.zlbovolini.keymanager.consultachavepix.ChavePixInfo
import com.github.zlbovolini.keymanager.consultachavepix.ContaInfo
import com.github.zlbovolini.keymanager.consultachavepix.TitularInfo
import io.micronaut.core.annotation.Introspected

@Introspected
data class ChavePixDetalhesResponse(
    @JsonProperty("keyType")
    val tipoChave: TipoChaveBCB,

    @JsonProperty("key")
    val chave: String,

    @JsonProperty("bankAccount")
    val conta: ContaBCB,

    @JsonProperty("owner")
    val titular: TitularChavePixBCB,

    @JsonProperty("createdAt")
    val criadoEm: String
) {
    fun toChavePixInfo(): ChavePixInfo {
        return ChavePixInfo(
            chaveId = "",
            clienteId = "",
            tipoChave = tipoChave.name,
            chave = chave,
            titular = TitularInfo(titular.nome, titular.cpf),
            conta = ContaInfo(
                instituicao = "",
                ispb = conta.ispb,
                tipoConta = conta.tipoConta.name,
                agencia = conta.agencia,
                numero = conta.numero
            )
        )
    }
}