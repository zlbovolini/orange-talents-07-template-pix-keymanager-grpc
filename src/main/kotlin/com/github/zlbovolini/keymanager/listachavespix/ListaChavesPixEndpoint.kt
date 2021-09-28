package com.github.zlbovolini.keymanager.listachavespix

import com.github.zlbovolini.keymanager.comum.ChavePix
import com.github.zlbovolini.keymanager.comum.ChavePixRepository
import com.github.zlbovolini.keymanager.comum.grpc.ErrorHandler
import com.github.zlbovolini.keymanager.grpc.*
import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.micronaut.validation.Validated
import jakarta.inject.Singleton

@Singleton
@Validated
class ListaChavesPixEndpoint(
    private val pixRepository: ChavePixRepository
) : ListaChavePixServiceGrpc.ListaChavePixServiceImplBase() {

    @ErrorHandler
    override fun lista(request: ListaChavesPixRequest, responseObserver: StreamObserver<ListaChavesPixResponse>) {
        val clienteId = request.clienteId

        if (clienteId.isNullOrBlank()) {
            val error = Status.INVALID_ARGUMENT
                .withDescription("Identificador do cliente inválido")
                .asRuntimeException()
            responseObserver.onError(error)
            return
        }

        val chaves = pixRepository.findAllByTitularClienteId(clienteId)
        val response = converte(chaves)

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}

fun converte(chaves: List<ChavePix>): ListaChavesPixResponse {

    val list = chaves.map { chavePix ->
        with(chavePix) {
            ListaChavesPixResponse.ChavePixResponse.newBuilder()
                .setPixId(uuid)
                .setClienteId(titular.clienteId)
                .setTipoChave(TipoChaveMessage.valueOf(chave.tipoChave.name))
                .setChave(chave.valor)
                .setTipoConta(TipoContaMessage.valueOf(conta.tipoConta.name))
                .setCriadoEm(criadoEm.toString())
                .build()
        }
    }.toCollection(mutableListOf()).toList()

    return ListaChavesPixResponse.newBuilder()
        .addAllChaves(list)
        .build()
}