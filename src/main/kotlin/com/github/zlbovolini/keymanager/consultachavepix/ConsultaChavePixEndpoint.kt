package com.github.zlbovolini.keymanager.consultachavepix

import com.github.zlbovolini.keymanager.comum.ChavePixRepository
import com.github.zlbovolini.keymanager.comum.bancocentral.BancoCentralPixClient
import com.github.zlbovolini.keymanager.comum.grpc.ErrorHandler
import com.github.zlbovolini.keymanager.grpc.ConsultaChavePixServiceGrpc
import com.github.zlbovolini.keymanager.grpc.ConsultaPorIdOuChaveRequest
import com.github.zlbovolini.keymanager.grpc.DadosChavePixResponse
import io.grpc.stub.StreamObserver
import io.micronaut.validation.Validated
import io.micronaut.validation.validator.Validator
import jakarta.inject.Singleton

@Singleton
@Validated
class ConsultaChavePixEndpoint(
    private val validator: Validator,
    private val pixRepository: ChavePixRepository,
    private val bancoCentralPixClient: BancoCentralPixClient
) : ConsultaChavePixServiceGrpc.ConsultaChavePixServiceImplBase() {

    @ErrorHandler
    override fun consulta(
        request: ConsultaPorIdOuChaveRequest,
        responseObserver: StreamObserver<DadosChavePixResponse>
    ) {

        val filtro = request.toFiltro(validator)
        val chavePixInfo = filtro.filtra(pixRepository, bancoCentralPixClient)

        val response = toResponse(chavePixInfo)

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}