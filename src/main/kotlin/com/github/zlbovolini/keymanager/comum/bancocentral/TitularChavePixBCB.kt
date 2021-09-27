package com.github.zlbovolini.keymanager.comum.bancocentral

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected

@Introspected
data class TitularChavePixBCB(
    @JsonProperty("type")
    val tipoCliente: TipoClienteBCB,

    @JsonProperty("name")
    val nome: String,

    @JsonProperty("taxIdNumber")
    val cpf: String
)
