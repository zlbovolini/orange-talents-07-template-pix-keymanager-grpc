package com.github.zlbovolini.keymanager.registrachavepix

import com.github.zlbovolini.keymanager.comum.ChavePix
import com.github.zlbovolini.keymanager.comum.ChavePixRepository
import com.github.zlbovolini.keymanager.comum.bancocentral.BancoCentralPixClient
import com.github.zlbovolini.keymanager.comum.bancocentral.RegistraChavePixBCBRequest
import com.github.zlbovolini.keymanager.comum.exception.ChavePixJaExistenteException
import com.github.zlbovolini.keymanager.comum.exception.ResourceNotFoundException
import com.github.zlbovolini.keymanager.comum.itau.ConsultaContaHttpClient
import com.github.zlbovolini.keymanager.comum.validacao.UnicoValidator
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional
import javax.validation.Valid

@Singleton
@Validated
class NovaChavePixService(
    @PersistenceContext
    private val entityManager: EntityManager,
    private val pixRepository: ChavePixRepository,
    private val consultaContaHttpClient: ConsultaContaHttpClient,
    private val bancoCentralPixClient: BancoCentralPixClient
) {

    @Transactional
    fun registra(@Valid novaChavePix: NovaChavePix): ChavePix {

        if (!isChaveUnica(novaChavePix.chave)) {
            throw ChavePixJaExistenteException("Chave já registrada");
        }

        val response = consultaContaHttpClient.porClienteTipoConta(novaChavePix.clienteId, novaChavePix.tipoConta)
        val conta = response.body() ?: throw ResourceNotFoundException("Cliente não encontrado no sistema do Itaú")

        val chavePix = novaChavePix.toModel(conta)

        pixRepository.save(chavePix)

        val bcbResponse = bancoCentralPixClient.registra(RegistraChavePixBCBRequest.of(chavePix))

        if (bcbResponse.status != HttpStatus.CREATED) {
            throw IllegalStateException("Erro ao registrar chave no Banco Central")
        }

        chavePix.atualiza(bcbResponse.body()!!["key"]!!)

        return chavePix
    }

    private fun isChaveUnica(valor: String): Boolean {
        return UnicoValidator(entityManager).run {
            isValid(valor, null, null)
        }
    }
}