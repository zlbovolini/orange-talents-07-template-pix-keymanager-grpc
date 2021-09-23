package com.github.zlbovolini.keymanager.comum

import javax.persistence.Embeddable

@Embeddable
class Titular(
    val clienteId: String,
    val nome: String,
    val cpf: String
)