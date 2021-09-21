package com.github.zlbovolini.keymanager.registrachavepix

import com.github.zlbovolini.keymanager.comum.validacao.Unico
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Embeddable
class Chave(
    @field:NotNull
    @Enumerated(EnumType.STRING)
    val tipo: TipoChave,

    @field:NotBlank
    @field:Unico
    @Column(unique = true, nullable = false)
    val valor: String,
)