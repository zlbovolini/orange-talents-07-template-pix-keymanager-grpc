package com.github.zlbovolini.keymanager.comum.bancocentral

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.zlbovolini.keymanager.comum.TipoChave
import com.github.zlbovolini.keymanager.comum.TipoConta
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
            tipoChave = when (tipoChave) {
                TipoChaveBCB.CPF -> TipoChave.CPF
                TipoChaveBCB.PHONE -> TipoChave.CELULAR
                TipoChaveBCB.EMAIL -> TipoChave.EMAIL
                TipoChaveBCB.RANDOM -> TipoChave.ALEATORIA
                else -> throw RuntimeException("Tipo chave não suportada")
            }.name,
            chave = chave,
            titular = TitularInfo(titular.nome, titular.cpf),
            conta = ContaInfo(
                instituicao = "",
                ispb = conta.ispb,
                tipoConta = when (conta.tipoConta) {
                    TipoContaBCB.CACC -> TipoConta.CONTA_CORRENTE
                    TipoContaBCB.SVGS -> TipoConta.CONTA_POUPANCA
                }.name,
                agencia = conta.agencia,
                numero = conta.numero
            )
        )
    }
}