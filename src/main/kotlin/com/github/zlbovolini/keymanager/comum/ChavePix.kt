package com.github.zlbovolini.keymanager.comum

import com.github.zlbovolini.keymanager.comum.itau.ContaItau
import com.github.zlbovolini.keymanager.comum.validacao.Pix
import java.time.Instant
import java.util.*
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Entity
class ChavePix(

    @Valid
    @field:NotNull
    @Pix
    @Embedded
    val chave: Chave,

    @Valid
    @field:NotNull
    @ContaItau
    @Embedded
    //@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = [CascadeType.PERSIST])
    val conta: Conta,

    @Valid
    @field:NotNull
    @Embedded
    val titular: Titular,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    val uuid = UUID.randomUUID().toString()

    val criadoEm = Instant.now()

    fun isDono(clienteId: String): Boolean {
        return titular.clienteId.uppercase() == clienteId.uppercase()
    }

    fun atualiza(chave: String) {
        this.chave.valor = chave
    }
}