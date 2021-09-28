package com.github.zlbovolini.keymanager.registrachavepix

import com.github.zlbovolini.keymanager.comum.*
import com.github.zlbovolini.keymanager.comum.itau.ContaResponse
import com.github.zlbovolini.keymanager.comum.validacao.ClienteItau
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
data class NovaChavePix(
    @field:NotBlank
    @field:ClienteItau
    val clienteId: String,

    @field:NotNull
    val tipoChave: TipoChave,

    @field:Size(max = 77)
    val chave: String,

    @field:NotBlank
    val tipoConta: TipoConta
) {
    fun toModel(contaResponse: ContaResponse): ChavePix {
        with(contaResponse) {
            val chave = Chave(tipoChave, chave)
            val conta = Conta(
                instituicao = instituicao.nome,
                ispb = instituicao.ispb,
                tipoConta = tipoConta,
                agencia = agencia,
                numero = numero
            )
            val titular = titular.toTitular()

            return ChavePix(chave, conta, titular)
        }
    }
}