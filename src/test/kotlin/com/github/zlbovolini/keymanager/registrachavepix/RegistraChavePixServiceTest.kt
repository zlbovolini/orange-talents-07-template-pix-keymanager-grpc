package com.github.zlbovolini.keymanager.registrachavepix

import com.github.zlbovolini.keymanager.comum.ChavePixRepository
import com.github.zlbovolini.keymanager.comum.TipoConta
import com.github.zlbovolini.keymanager.comum.itau.*
import com.github.zlbovolini.keymanager.grpc.RegistraChavePixRequest
import com.github.zlbovolini.keymanager.grpc.RegistraChavePixServiceGrpc
import com.github.zlbovolini.keymanager.grpc.TipoChaveRequest
import com.github.zlbovolini.keymanager.grpc.TipoContaRequest
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.util.*

@MicronautTest(transactional = false)
internal class RegistraChavePixServiceTest(
    private val grpcClient: RegistraChavePixServiceGrpc.RegistraChavePixServiceBlockingStub,
    private val pixRepository: ChavePixRepository
) {

    private val clienteId = UUID.randomUUID().toString()

    @Inject
    private lateinit var consultaClienteHttpClient: ConsultaClienteHttpClient

    @Inject
    private lateinit var consultaContaHttpClient: ConsultaContaHttpClient

    private lateinit var request: RegistraChavePixRequest

    @BeforeEach
    fun setUp() {
        pixRepository.deleteAll()

        request = RegistraChavePixRequest.newBuilder()
            .setClienteId(clienteId)
            .setTipoChave(TipoChaveRequest.CPF)
            .setChave("02467781054")
            .setTipoConta(TipoContaRequest.CONTA_CORRENTE)
            .build()
    }

    @Test
    fun `deve registrar nova chave pix do tipo cpf para conta corrente`() {

        Mockito.`when`(consultaClienteHttpClient.porId(clienteId))
            .thenReturn(HttpResponse.ok(dadosClienteResponse()))

        Mockito.`when`(consultaContaHttpClient.porClienteTipoConta(clienteId, TipoConta.CONTA_CORRENTE))
            .thenReturn(HttpResponse.ok(dadosContaResponse()))

        val response = grpcClient.registra(request)

        with(response) {
            assertNotNull(pixId)
            assertTrue(pixRepository.existsByChaveValor("02467781054"))
        }
    }

    @Test
    fun `nao deve registar chave pix com cliente nao registrado no itau`() {

        Mockito.`when`(consultaClienteHttpClient.porId(clienteId))
            .thenReturn(HttpResponse.notFound())

        Mockito.`when`(consultaContaHttpClient.porClienteTipoConta(clienteId, TipoConta.CONTA_CORRENTE))
            .thenReturn(HttpResponse.notFound())

        val error = assertThrows<StatusRuntimeException> {
            grpcClient.registra(request)
        }

        with(error) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("parametros de entrada invalidos", status.description)
        }
    }

    @Test
    fun `nao deve registrar a mesma chave mais de uma vez`() {

        Mockito.`when`(consultaClienteHttpClient.porId(clienteId))
            .thenReturn(HttpResponse.ok(dadosClienteResponse()))

        Mockito.`when`(consultaContaHttpClient.porClienteTipoConta(clienteId, TipoConta.CONTA_CORRENTE))
            .thenReturn(HttpResponse.ok(dadosContaResponse()))

        grpcClient.registra(request)
        val error = assertThrows<StatusRuntimeException> {
            grpcClient.registra(request)
        }

        with(error) {
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
            assertEquals("Chave já registrada", status.description)
        }
    }

    private fun dadosClienteResponse(): ClienteResponse {
        val instituicaoResponse = InstituicaoResponse("ITAÚ UNIBANCO S.A.", "60701190")

        return ClienteResponse(
            id = clienteId,
            nome = "Rafael M C Ponte",
            cpf = "02467781054",
            instituicao = instituicaoResponse
        )
    }

    private fun dadosContaResponse(): ContaResponse {
        val instituicaoResponse = InstituicaoResponse("ITAÚ UNIBANCO S.A.", "60701190")
        val titularResponse = TitularResponse(clienteId, "Rafael M C Ponte", "02467781054")

        return ContaResponse(
            tipo = TipoConta.CONTA_CORRENTE.name,
            instituicao = instituicaoResponse,
            agencia = "0001",
            numero = "291900",
            titular = titularResponse
        )
    }

    @MockBean(ConsultaClienteHttpClient::class)
    fun consultaClienteHttpClient(): ConsultaClienteHttpClient {
        return Mockito.mock(ConsultaClienteHttpClient::class.java)
    }

    @MockBean(ConsultaContaHttpClient::class)
    fun consultaContaHttpClient(): ConsultaContaHttpClient {
        return Mockito.mock(ConsultaContaHttpClient::class.java)
    }

    @Factory
    class Clients {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): RegistraChavePixServiceGrpc.RegistraChavePixServiceBlockingStub {
            return RegistraChavePixServiceGrpc.newBlockingStub(channel)
        }
    }
}
