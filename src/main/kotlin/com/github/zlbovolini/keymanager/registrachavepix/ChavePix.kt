package com.github.zlbovolini.keymanager.registrachavepix

import com.github.zlbovolini.keymanager.comum.itau.ContaItau
import com.github.zlbovolini.keymanager.comum.validacao.Pix
import java.util.*
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Entity
class ChavePix(

    @Valid
    @Pix
    @Embedded
    val chave: Chave,

    @Valid
    @field:NotNull
    @ContaItau
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = [CascadeType.PERSIST])
    val conta: Conta
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    val uuid = UUID.randomUUID().toString()
}