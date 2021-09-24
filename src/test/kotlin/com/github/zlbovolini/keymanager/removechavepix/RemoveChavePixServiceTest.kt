package com.github.zlbovolini.keymanager.removechavepix

import com.github.zlbovolini.keymanager.comum.*
import com.github.zlbovolini.keymanager.comum.bancocentral.BancoCentralPixClient
import com.github.zlbovolini.keymanager.comum.bancocentral.RemoveChavePixBCBRequest
import com.github.zlbovolini.keymanager.comum.itau.*
import com.github.zlbovolini.keymanager.grpc.RemoveChavePixRequest
import com.github.zlbovolini.keymanager.grpc.RemoveChavePixServiceGrpc
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
import org.mockito.Mockito.only
import org.mockito.Mockito.verify
import java.util.*

@MicronautTest(transactional = false)
internal class RemoveChavePixServiceTest(
    private val pixRepository: ChavePixRepository,
    private val grpcClient: RemoveChavePixServiceGrpc.RemoveChavePixServiceBlockingStub
) {

    private val ponteClienteId = UUID.randomUUID().toString()
    private val ponteChave = Chave(TipoChave.CPF, "02467781054")
    private val ponteConta = Conta(TipoConta.CONTA_CORRENTE, "0001", "291900")
    private val ponteTitular = Titular(ponteClienteId, "Ponte", "02467781054")
    private val ponteChavePix = ChavePix(ponteChave, ponteConta, ponteTitular)

    private val yuriClienteId = UUID.randomUUID().toString()
    private val yuriChave = Chave(TipoChave.CPF, "86135457004")
    private val yuriConta = Conta(TipoConta.CONTA_POUPANCA, "0001", "123455")
    private val yuriTitular = Titular(yuriClienteId, "Yuri", "86135457004")
    private val yuriChavePix = ChavePix(yuriChave, yuriConta, yuriTitular)

    @Inject
    private lateinit var consultaClienteHttpClient: ConsultaClienteHttpClient

    @Inject
    private lateinit var consultaContaHttpClient: ConsultaContaHttpClient

    @Inject
    private lateinit var bancoCentralPixClient: BancoCentralPixClient

    @BeforeEach
    fun setUp() {
        pixRepository.deleteAll()

        // Ponte
        Mockito.`when`(consultaClienteHttpClient.porId(ponteClienteId))
            .thenReturn(HttpResponse.ok(dadosPonteClienteResponse()))

        Mockito.`when`(consultaContaHttpClient.porClienteTipoConta(ponteClienteId, TipoConta.CONTA_CORRENTE))
            .thenReturn(HttpResponse.ok(dadosPonteContaResponse()))

        // Yuri
        Mockito.`when`(consultaClienteHttpClient.porId(yuriClienteId))
            .thenReturn(HttpResponse.ok(dadosYuriClienteResponse()))

        Mockito.`when`(consultaContaHttpClient.porClienteTipoConta(yuriClienteId, TipoConta.CONTA_POUPANCA))
            .thenReturn(HttpResponse.ok(dadosYuriContaResponse()))
    }

    @Test
    fun `deve remover chave pix cpf`() {

        Mockito.`when`(bancoCentralPixClient.remove(ponteChave.valor, RemoveChavePixBCBRequest(ponteChave.valor)))
            .thenReturn(HttpResponse.ok())

        pixRepository.save(ponteChavePix)

        val request = RemoveChavePixRequest.newBuilder()
            .setChaveId(ponteChavePix.uuid)
            .setClienteId(ponteChavePix.titular.clienteId)
            .build()

        grpcClient.remove(request)

        assertFalse(pixRepository.existsByChaveValor(ponteChavePix.chave.valor))
        verify(bancoCentralPixClient, only()).remove("02467781054", RemoveChavePixBCBRequest("02467781054"))
    }

    @Test
    fun `nao deve remover chave pix invalida`() {

        pixRepository.save(ponteChavePix)

        val request = RemoveChavePixRequest.newBuilder().build()
        val error = assertThrows<StatusRuntimeException> {
            grpcClient.remove(request)
        }

        with(error) {
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("Chave não encontrada", status.description)
            assertTrue(pixRepository.existsByChaveValor(ponteChavePix.chave.valor))
        }
    }

    @Test
    fun `nao deve remover chave pix de outro cliente`() {

        pixRepository.save(ponteChavePix)
        pixRepository.save(yuriChavePix)

        val request = RemoveChavePixRequest.newBuilder()
            .setChaveId(ponteChavePix.uuid)
            .setClienteId(yuriClienteId)
            .build()

        val error = assertThrows<StatusRuntimeException> {
            grpcClient.remove(request)
        }

        with(error) {
            assertEquals(Status.PERMISSION_DENIED.code, status.code)
            assertEquals("Cliente não é o dono da chave", status.description)
            assertTrue(pixRepository.existsByChaveValor(ponteChavePix.chave.valor))
        }
    }

    @Test
    fun `nao deve remover chave pix quando nao encontrada no banco central`() {

        Mockito.`when`(bancoCentralPixClient.remove(ponteChave.valor, RemoveChavePixBCBRequest(ponteChave.valor)))
            .thenReturn(HttpResponse.notFound())

        pixRepository.save(ponteChavePix)

        val request = RemoveChavePixRequest.newBuilder()
            .setChaveId(ponteChavePix.uuid)
            .setClienteId(ponteClienteId)
            .build()

        val error = assertThrows<StatusRuntimeException> {
            grpcClient.remove(request)
        }

        with(error) {
            assertEquals(Status.FAILED_PRECONDITION.code, status.code)
            assertEquals("Erro ao remover chave pix do Banco Central", status.description)
        }
        assertTrue(pixRepository.existsByChaveValor(ponteChave.valor))
        verify(bancoCentralPixClient).remove(ponteChave.valor, RemoveChavePixBCBRequest(ponteChave.valor))
    }

    private fun dadosPonteClienteResponse(): ClienteResponse {
        val instituicaoResponse = InstituicaoResponse("ITAÚ UNIBANCO S.A.", "60701190")

        return ClienteResponse(
            id = ponteClienteId,
            nome = "Rafael M C Ponte",
            cpf = "02467781054",
            instituicao = instituicaoResponse
        )
    }

    private fun dadosYuriClienteResponse(): ClienteResponse {
        val instituicaoResponse = InstituicaoResponse("ITAÚ UNIBANCO S.A.", "60701190")

        return ClienteResponse(
            id = "5260263c-a3c1-4727-ae32-3bdb2538841b",
            nome = "Yuri Matheus",
            cpf = "86135457004",
            instituicao = instituicaoResponse
        )
    }

    private fun dadosPonteContaResponse(): ContaResponse {
        val instituicaoResponse = InstituicaoResponse("ITAÚ UNIBANCO S.A.", "60701190")
        val titularResponse = TitularResponse(ponteClienteId, "Rafael M C Ponte", "02467781054")

        return ContaResponse(
            tipo = TipoConta.CONTA_CORRENTE.name,
            instituicao = instituicaoResponse,
            agencia = "0001",
            numero = "291900",
            titular = titularResponse
        )
    }

    private fun dadosYuriContaResponse(): ContaResponse {
        val instituicaoResponse = InstituicaoResponse("ITAÚ UNIBANCO S.A.", "60701190")
        val titularResponse = TitularResponse("5260263c-a3c1-4727-ae32-3bdb2538841b", "Yuri Matheus", "86135457004")

        return ContaResponse(
            tipo = TipoConta.CONTA_CORRENTE.name,
            instituicao = instituicaoResponse,
            agencia = "0001",
            numero = "123455",
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

    @MockBean(BancoCentralPixClient::class)
    fun bancoCentralPixClient(): BancoCentralPixClient {
        return Mockito.mock(BancoCentralPixClient::class.java)
    }

    @Factory
    class Clients {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): RemoveChavePixServiceGrpc.RemoveChavePixServiceBlockingStub {
            return RemoveChavePixServiceGrpc.newBlockingStub(channel)
        }
    }
}