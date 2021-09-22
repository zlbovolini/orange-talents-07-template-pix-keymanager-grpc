package com.github.zlbovolini.keymanager.comum

import com.github.zlbovolini.keymanager.comum.validacao.Unico
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Embeddable
class Chave(
    @field:NotNull
    @Enumerated(EnumType.STRING)
    val tipoChave: TipoChave,

    @field:NotBlank
    @field:Unico
    @Column(unique = true, nullable = false)
    val valor: String,
)