package com.github.zlbovolini.keymanager.registrachavepix

import com.github.zlbovolini.keymanager.comum.ChavePix
import com.github.zlbovolini.keymanager.comum.ChavePixRepository
import com.github.zlbovolini.keymanager.comum.exception.ChavePixJaExistenteException
import com.github.zlbovolini.keymanager.comum.exception.ResourceNotFoundException
import com.github.zlbovolini.keymanager.comum.itau.ConsultaContaHttpClient
import com.github.zlbovolini.keymanager.comum.validacao.UnicoValidator
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
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
    private val consultaContaHttpClient: ConsultaContaHttpClient
) {

    @Transactional
    fun registra(@Valid novaChavePix: NovaChavePix): ChavePix {

        if (!isChaveUnica(novaChavePix.chave)) {
            throw ChavePixJaExistenteException("Chave já registrada");
        }

        val response = try {
            consultaContaHttpClient.porClienteTipoConta(novaChavePix.clienteId, novaChavePix.tipoConta)
        } catch (e: HttpClientResponseException) {
            throw e
        }

        if (response.status == HttpStatus.NOT_FOUND) {
            throw ResourceNotFoundException("Cliente não encontrado no sistema do Itaú")
        }

        val chavePix = novaChavePix.toModel(response.body()!!)
        return pixRepository.save(chavePix)
    }

    private fun isChaveUnica(valor: String): Boolean {
        return UnicoValidator(entityManager).run {
            isValid(valor, null, null)
        }
    }
}