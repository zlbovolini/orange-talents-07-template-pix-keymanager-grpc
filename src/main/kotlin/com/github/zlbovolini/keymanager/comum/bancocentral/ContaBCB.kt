package com.github.zlbovolini.keymanager.comum.bancocentral

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected

@Introspected
data class ContaBCB(
    @JsonProperty("participant")
    val ispb: String = "60701190",

    @JsonProperty("branch")
    val agencia: String,

    @JsonProperty("accountNumber")
    val numero: String,

    @JsonProperty("accountType")
    val tipoConta: TipoContaBCB
)
