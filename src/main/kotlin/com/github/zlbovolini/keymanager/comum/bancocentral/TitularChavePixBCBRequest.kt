package com.github.zlbovolini.keymanager.comum.bancocentral

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected

@Introspected
data class TitularChavePixBCBRequest(
    @JsonProperty("type")
    val tipoCliente: TipoClienteBCBRequest,

    @JsonProperty("name")
    val nome: String,

    @JsonProperty("taxIdNumber")
    val cpf: String
)
