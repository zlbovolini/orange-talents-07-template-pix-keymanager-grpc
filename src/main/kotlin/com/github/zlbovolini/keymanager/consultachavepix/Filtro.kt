package com.github.zlbovolini.keymanager.consultachavepix

import com.github.zlbovolini.keymanager.comum.ChavePixRepository
import com.github.zlbovolini.keymanager.comum.bancocentral.BancoCentralPixClient
import com.github.zlbovolini.keymanager.comum.exception.ResourceNotFoundException
import com.github.zlbovolini.keymanager.comum.exception.UnauthorizedAccessException
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpStatus
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
sealed class Filtro {

    abstract fun filtra(pixRepository: ChavePixRepository, bancoCentralPixClient: BancoCentralPixClient): ChavePixInfo

    @Introspected
    data class ConsultaPorId(
        @field:NotBlank
        val chaveId: String,

        @field:NotBlank
        val clienteId: String
    ) : Filtro() {
        override fun filtra(
            pixRepository: ChavePixRepository,
            bancoCentralPixClient: BancoCentralPixClient
        ): ChavePixInfo {
            pixRepository.findByUuid(chaveId)
                .let { chavePix ->
                    if (chavePix == null || chavePix.chave.valor.isBlank()) {
                        throw ResourceNotFoundException("Chave não encontrada")
                    }

                    if (!chavePix.isDono(clienteId)) {
                        throw UnauthorizedAccessException("Cliente não é o dono da chave")
                    }

                    return ChavePixInfo.of(chavePix)
                }
        }
    }

    @Introspected
    data class ConsultaPorChave(
        @field:NotBlank
        @field:Size(max = 77)
        val chave: String
    ) : Filtro() {
        override fun filtra(
            pixRepository: ChavePixRepository,
            bancoCentralPixClient: BancoCentralPixClient
        ): ChavePixInfo {
            pixRepository.findByChaveValor(chave)
                .let { chavePix ->
                    if (chavePix == null) {
                        val response = bancoCentralPixClient.consulta(chave)

                        if (response.status != HttpStatus.OK) {
                            throw ResourceNotFoundException("Chave não encontrada")
                        }

                        return response.body()?.toChavePixInfo()!!
                    }

                    return ChavePixInfo.of(chavePix)
                }
        }
    }

    @Introspected
    class Invalido : Filtro() {
        override fun filtra(
            pixRepository: ChavePixRepository,
            bancoCentralPixClient: BancoCentralPixClient
        ): ChavePixInfo {
            throw IllegalArgumentException("Chave pix inválida ou não informada")
        }

    }

}