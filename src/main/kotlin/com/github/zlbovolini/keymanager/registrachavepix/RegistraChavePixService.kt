package com.github.zlbovolini.keymanager.registrachavepix

import com.github.zlbovolini.keymanager.comum.grpc.ErrorHandler
import com.github.zlbovolini.keymanager.comum.grpc.toNovaChave
import com.github.zlbovolini.keymanager.grpc.RegistraChavePixRequest
import com.github.zlbovolini.keymanager.grpc.RegistraChavePixResponse
import com.github.zlbovolini.keymanager.grpc.RegistraChavePixServiceGrpc
import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
@Validated
class RegistraChavePixService(
    private val novaChavePixService: NovaChavePixService
) :
    RegistraChavePixServiceGrpc.RegistraChavePixServiceImplBase() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @ErrorHandler
    override fun registra(
        request: RegistraChavePixRequest?,
        responseObserver: StreamObserver<RegistraChavePixResponse>?
    ) {
        if (request == null) {
            val error = "Request nulo durante registro de chave Pix"
            logger.info(error)
            responseObserver?.onError(
                Status.INTERNAL.withDescription(error)
                    .asRuntimeException()
            )
            return
        }

        val novaChavePix = request.toNovaChave()
        val chavePix = novaChavePixService.registra(novaChavePix)

        val response = RegistraChavePixResponse.newBuilder()
            .setPixId(chavePix.uuid)
            .build()

        responseObserver?.onNext(response)
        responseObserver?.onCompleted()
    }

}