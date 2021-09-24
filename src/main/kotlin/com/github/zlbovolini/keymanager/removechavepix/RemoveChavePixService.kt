package com.github.zlbovolini.keymanager.removechavepix

import com.github.zlbovolini.keymanager.comum.ChavePix
import com.github.zlbovolini.keymanager.comum.ChavePixRepository
import com.github.zlbovolini.keymanager.comum.bancocentral.BancoCentralPixClient
import com.github.zlbovolini.keymanager.comum.bancocentral.RemoveChavePixBCBRequest
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import jakarta.validation.Valid
import javax.transaction.Transactional

@Singleton
@Validated
class RemoveChavePixService(
    private val chavePixRepository: ChavePixRepository,
    private val bancoCentralPixClient: BancoCentralPixClient
) {

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

                val bcbRequest = RemoveChavePixBCBRequest(chavePix.chave.valor)
                bancoCentralPixClient.remove(chavePix.chave.valor, bcbRequest)

                return null
            }
    }
}