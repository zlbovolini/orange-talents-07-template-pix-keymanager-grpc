package com.github.zlbovolini.keymanager.removechavepix

import com.github.zlbovolini.keymanager.comum.ChavePix
import com.github.zlbovolini.keymanager.comum.ChavePixRepository
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import jakarta.validation.Valid
import javax.transaction.Transactional

@Singleton
@Validated
class RemoveChavePixService(private val chavePixRepository: ChavePixRepository) {

    @Transactional
    fun remove(@Valid removeChave: RemoveChavePix): StatusRuntimeException? {

        val (chaveId, clienteId) = removeChave

        chavePixRepository.findByUuid(chaveId)
            .let { chavePix: ChavePix? ->
                if (chavePix == null) {
                    return Status.NOT_FOUND
                        .withDescription("Chave não encontrada")
                        .asRuntimeException()
                }

                if (!chavePix.isDono(clienteId)) {
                    return Status.PERMISSION_DENIED
                        .withDescription("Cliente não é o dono da chave")
                        .asRuntimeException()
                }
                chavePixRepository.deleteById(chavePix.id!!)

                return null
            }
    }
}