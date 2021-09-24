package com.github.zlbovolini.keymanager.removechavepix

import com.github.zlbovolini.keymanager.comum.grpc.ErrorHandler
import com.github.zlbovolini.keymanager.grpc.RemoveChavePixRequest
import com.github.zlbovolini.keymanager.grpc.RemoveChavePixServiceGrpc
import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Validated
@Singleton
class RemoveChavePixEndpoint(
    val removeChavePixService: RemoveChavePixService
) : RemoveChavePixServiceGrpc.RemoveChavePixServiceImplBase() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @ErrorHandler
    override fun remove(request: RemoveChavePixRequest, responseObserver: StreamObserver<Empty>) {

        val removeChave = request.toRemoveChave()

        val error = removeChavePixService.remove(removeChave)

        if (error != null) {
            logger.info("Erro durante remoção de chave pix id={} do cliente={}", request.chaveId, request.clienteId)
            responseObserver.onError(error)
            return
        }

        responseObserver.onNext(Empty.newBuilder().build())
        responseObserver.onCompleted()
    }
}