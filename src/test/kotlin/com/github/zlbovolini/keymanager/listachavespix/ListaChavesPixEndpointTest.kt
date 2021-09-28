package com.github.zlbovolini.keymanager.listachavespix

import com.github.zlbovolini.keymanager.comum.*
import com.github.zlbovolini.keymanager.grpc.ListaChavePixServiceGrpc
import com.github.zlbovolini.keymanager.grpc.ListaChavesPixRequest
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

@MicronautTest(transactional = false)
internal class ListaChavesPixEndpointTest(
    private val grpcClient: ListaChavePixServiceGrpc.ListaChavePixServiceBlockingStub,
    private val pixRepository: ChavePixRepository
) {

    private val yuriClienteId = UUID.randomUUID().toString()
    private val yuriChave = Chave(TipoChave.CPF, "86135457004")
    private val yuriConta = Conta("ITAÚ UNIBANCO S.A.", "60701190", TipoConta.CONTA_POUPANCA, "0001", "123455")
    private val yuriTitular = Titular(yuriClienteId, "Yuri", "86135457004")
    private val yuriChavePix = ChavePix(yuriChave, yuriConta, yuriTitular)

    @BeforeEach
    fun setUp() {
        pixRepository.deleteAll()
        pixRepository.save(yuriChavePix)
    }

    @Test
    fun `deve retornar lista de chaves pix`() {

        val request = ListaChavesPixRequest.newBuilder()
            .setClienteId(yuriClienteId)
            .build()

        val response = grpcClient.lista(request)

        with(response) {
            assertNotNull(chavesList)
            assertEquals(1, chavesList.size)
            assertEquals(yuriClienteId, chavesList[0].clienteId)
        }
    }

    @Test
    fun `deve retornar lista vazia de chaves pix`() {

        val request = ListaChavesPixRequest.newBuilder()
            .setClienteId("0")
            .build()

        val response = grpcClient.lista(request)

        with(response) {
            assertNotNull(chavesList)
            assertEquals(0, chavesList.size)
        }
    }

    @Test
    fun `nao deve retornar lista chaves pix quando requisicao invalida`() {

        val request = ListaChavesPixRequest.newBuilder().build()

        val error = assertThrows<StatusRuntimeException> {
            grpcClient.lista(request)
        }

        with(error) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("Identificador do cliente inválido", status.description)
        }
    }

    @Factory
    class Clients {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): ListaChavePixServiceGrpc.ListaChavePixServiceBlockingStub {
            return ListaChavePixServiceGrpc.newBlockingStub(channel)
        }
    }
}