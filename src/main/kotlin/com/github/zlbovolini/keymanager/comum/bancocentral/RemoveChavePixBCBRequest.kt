package com.github.zlbovolini.keymanager.comum.bancocentral

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected

@Introspected
data class RemoveChavePixBCBRequest(
    @JsonProperty("key")
    val chave: String,

    @JsonProperty("participant")
    val ispb: String = "60701190"
)
