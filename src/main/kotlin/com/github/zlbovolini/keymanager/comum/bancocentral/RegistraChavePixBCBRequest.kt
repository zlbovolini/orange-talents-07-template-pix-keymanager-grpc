package com.github.zlbovolini.keymanager.comum.bancocentral

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.zlbovolini.keymanager.comum.ChavePix
import io.micronaut.core.annotation.Introspected

@Introspected
data class RegistraChavePixBCBRequest(
    @JsonProperty("keyType")
    val tipoChave: TipoChaveBCBRequest,

    @JsonProperty("key")
    val chave: String,

    @JsonProperty("bankAccount")
    val conta: ContaBCBRequest,

    @JsonProperty("owner")
    val dono: TitularChavePixBCBRequest
) {
    companion object {
        fun of(chavePix: ChavePix): RegistraChavePixBCBRequest {
            return RegistraChavePixBCBRequest(
                tipoChave = chavePix.chave.tipoChave.tipoChaveBCBRequest,
                chave = chavePix.chave.valor,
                conta = with(chavePix.conta) {
                    ContaBCBRequest(
                        agencia = agencia,
                        numero = numero,
                        tipoConta = tipoConta.tipoContaBCBRequest
                    )
                },
                dono = with(chavePix.titular) {
                    TitularChavePixBCBRequest(
                        tipoCliente = TipoClienteBCBRequest.NATURAL_PERSON,
                        nome = nome,
                        cpf = cpf
                    )
                }
            )
        }
    }
}
