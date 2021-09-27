package com.github.zlbovolini.keymanager.comum

import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Embeddable
class Conta(
    @field:NotBlank
    val instituicao: String,

    @field:NotBlank
    val ispb: String,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    val tipoConta: TipoConta,

    @field:NotBlank
    val agencia: String,

    @field:NotBlank
    val numero: String
)