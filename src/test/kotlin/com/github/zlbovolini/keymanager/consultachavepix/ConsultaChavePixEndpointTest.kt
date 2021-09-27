package com.github.zlbovolini.keymanager.consultachavepix

import com.github.zlbovolini.keymanager.comum.*
import com.github.zlbovolini.keymanager.comum.bancocentral.*
import com.github.zlbovolini.keymanager.grpc.ConsultaChavePixServiceGrpc
import com.github.zlbovolini.keymanager.grpc.ConsultaPorIdOuChaveRequest
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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.time.Instant
import java.util.*

@MicronautTest(transactional = false)
internal class ConsultaChavePixEndpointTest(
    private val grpcClient: ConsultaChavePixServiceGrpc.ConsultaChavePixServiceBlockingStub,
    private val pixRepository: ChavePixRepository
) {

    private val ponteClienteId = UUID.randomUUID().toString()
    private val ponteChave = Chave(TipoChave.CPF, "02467781054")
    private val ponteConta = Conta("ITAÚ UNIBANCO S.A.", "60701190", TipoConta.CONTA_CORRENTE, "0001", "291900")
    private val ponteTitular = Titular(ponteClienteId, "Ponte", "02467781054")
    private val ponteChavePix = ChavePix(ponteChave, ponteConta, ponteTitular)

    @Inject
    private lateinit var bancoCentralPixClient: BancoCentralPixClient

    @BeforeEach
    fun setUp() {
        pixRepository.deleteAll()
    }

    @Test
    fun `deve retornar chave pix por pixId e clienteId`() {

        pixRepository.save(ponteChavePix)

        val request = ConsultaPorIdOuChaveRequest.newBuilder()
            .setPorId(
                ConsultaPorIdOuChaveRequest.PorId.newBuilder().setPixId(ponteChavePix.uuid)
                    .setClienteId(ponteChavePix.titular.clienteId).build()
            ).build()

        val response = grpcClient.consulta(request)

        with(response) {
            assertNotNull(chaveId)
            assertNotNull(clienteId)
        }
    }

    @Test
    fun `nao deve retornar chave pix por pixId e clienteId quando nao existir`() {

        val request = ConsultaPorIdOuChaveRequest.newBuilder()
            .setPorId(
                ConsultaPorIdOuChaveRequest.PorId.newBuilder()
                    .setPixId(UUID.randomUUID().toString())
                    .setClienteId(UUID.randomUUID().toString())
                    .build()
            ).build()


        val error = assertThrows<StatusRuntimeException> {
            grpcClient.consulta(request)
        }

        with(error) {
            assertEquals(Status.NOT_FOUND.code, status.code)
        }
    }

    @Test
    fun `nao deve retornar chave pix por pixId e clienteId quando cliente nao for dono da chave`() {

        pixRepository.save(ponteChavePix)

        val request = ConsultaPorIdOuChaveRequest.newBuilder()
            .setPorId(
                ConsultaPorIdOuChaveRequest.PorId.newBuilder()
                    .setPixId(ponteChavePix.uuid)
                    .setClienteId("0")
                    .build()
            ).build()

        val error = assertThrows<StatusRuntimeException> {
            grpcClient.consulta(request)
        }

        with(error) {
            assertEquals(Status.PERMISSION_DENIED.code, status.code)
        }
    }

    @Test
    fun `nao deve retornar chave pix quando requisicao invalida`() {

        val request = ConsultaPorIdOuChaveRequest.newBuilder().build()

        val error = assertThrows<StatusRuntimeException> {
            grpcClient.consulta(request)
        }

        with(error) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
        }
    }

    @Test
    fun `deve retornar chave pix quando existir somente no banco central`() {

        Mockito.`when`(bancoCentralPixClient.consulta(ponteChave.valor))
            .thenReturn(HttpResponse.ok(detalhesChavePixResponse()))

        val request = ConsultaPorIdOuChaveRequest.newBuilder()
            .setChave(ponteChave.valor)
            .build()

        val response = grpcClient.consulta(request)

        with(response) {
            assertEquals("", chaveId)
            assertEquals("", clienteId)
            assertEquals(ponteChave.valor, chave)
        }

        Mockito.verify(bancoCentralPixClient).consulta(ponteChave.valor)
    }

    private fun detalhesChavePixResponse(): ChavePixDetalhesResponse {
        return ChavePixDetalhesResponse(
            tipoChave = TipoChaveBCB.CPF,
            chave = "02467781054",
            conta = ContaBCB(
                agencia = "0001",
                numero = "291900",
                tipoConta = TipoContaBCB.CACC
            ),
            titular = TitularChavePixBCB(
                tipoCliente = TipoClienteBCB.NATURAL_PERSON,
                nome = "Rafael M C Ponte",
                cpf = "02467781054"
            ),
            criadoEm = Instant.now().toString()
        )
    }

    @MockBean(BancoCentralPixClient::class)
    fun bancoCentralPixClient(): BancoCentralPixClient {
        return Mockito.mock(BancoCentralPixClient::class.java)
    }

    @Factory
    class Clients {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): ConsultaChavePixServiceGrpc.ConsultaChavePixServiceBlockingStub {
            return ConsultaChavePixServiceGrpc.newBlockingStub(channel)
        }
    }
}