package com.github.zlbovolini.keymanager.comum.bancocentral

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.zlbovolini.keymanager.comum.ChavePix
import io.micronaut.core.annotation.Introspected

@Introspected
data class RegistraChavePixBCBRequest(
    @JsonProperty("keyType")
    val tipoChave: TipoChaveBCB,

    @JsonProperty("key")
    val chave: String,

    @JsonProperty("bankAccount")
    val conta: ContaBCB,

    @JsonProperty("owner")
    val dono: TitularChavePixBCB
) {
    companion object {
        fun of(chavePix: ChavePix): RegistraChavePixBCBRequest {
            return RegistraChavePixBCBRequest(
                tipoChave = chavePix.chave.tipoChave.tipoChaveBCB,
                chave = chavePix.chave.valor,
                conta = with(chavePix.conta) {
                    ContaBCB(
                        agencia = agencia,
                        numero = numero,
                        tipoConta = tipoConta.tipoContaBCB
                    )
                },
                dono = with(chavePix.titular) {
                    TitularChavePixBCB(
                        tipoCliente = TipoClienteBCB.NATURAL_PERSON,
                        nome = nome,
                        cpf = cpf
                    )
                }
            )
        }
    }
}
