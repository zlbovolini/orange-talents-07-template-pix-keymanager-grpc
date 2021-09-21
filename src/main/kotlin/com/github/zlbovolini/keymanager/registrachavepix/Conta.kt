package com.github.zlbovolini.keymanager.registrachavepix

import com.github.zlbovolini.keymanager.comum.validacao.ClienteItau
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Conta(
    @field:NotNull
    @Enumerated(EnumType.STRING)
    val tipo: TipoConta,

    @field:NotBlank
    @field:ClienteItau
    val clienteId: String,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}