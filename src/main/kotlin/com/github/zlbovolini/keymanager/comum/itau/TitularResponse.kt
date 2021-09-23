package com.github.zlbovolini.keymanager.comum.itau

import com.github.zlbovolini.keymanager.comum.Titular

data class TitularResponse(
    val id: String,
    val nome: String,
    val cpf: String
) {

    fun toTitular(): Titular {
        return Titular(
            id, nome, cpf
        )
    }
}
