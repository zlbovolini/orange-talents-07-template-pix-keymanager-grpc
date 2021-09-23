package com.github.zlbovolini.keymanager.registrachavepix

import com.github.zlbovolini.keymanager.comum.grpc.ErrorHandler
import com.github.zlbovolini.keymanager.grpc.RegistraChavePixRequest
import com.github.zlbovolini.keymanager.grpc.RegistraChavePixResponse
import com.github.zlbovolini.keymanager.grpc.RegistraChavePixServiceGrpc
import io.grpc.stub.StreamObserver
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
@Validated
class RegistraChavePixEndpoint(
    private val novaChavePixService: NovaChavePixService,
    private val registraChavePixBCBService: RegistraChavePixBCBService
) :
    RegistraChavePixServiceGrpc.RegistraChavePixServiceImplBase() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @ErrorHandler
    override fun registra(
        request: RegistraChavePixRequest,
        responseObserver: StreamObserver<RegistraChavePixResponse>
    ) {
        val novaChavePix = request.toNovaChave()
        val chavePix = novaChavePixService.registra(novaChavePix)

        registraChavePixBCBService.executa(chavePix)

        val response = RegistraChavePixResponse.newBuilder()
            .setPixId(chavePix.uuid)
            .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

}