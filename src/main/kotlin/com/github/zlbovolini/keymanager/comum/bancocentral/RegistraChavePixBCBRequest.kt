package com.github.zlbovolini.keymanager.comum.bancocentral

import com.fasterxml.jackson.annotation.JsonProperty
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
)
