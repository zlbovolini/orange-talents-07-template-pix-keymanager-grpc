package com.github.zlbovolini.keymanager.comum.itau

import io.micronaut.core.annotation.Introspected

@Introspected
data class ClienteResponse(
    val id: String,
    val nome: String,
    val cpf: String,
    val instituicao: InstituicaoResponse
)
