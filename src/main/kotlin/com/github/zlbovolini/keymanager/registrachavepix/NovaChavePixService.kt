package com.github.zlbovolini.keymanager.registrachavepix

import com.github.zlbovolini.keymanager.comum.ChavePix
import com.github.zlbovolini.keymanager.comum.ChavePixRepository
import com.github.zlbovolini.keymanager.comum.exception.ChavePixJaExistenteException
import com.github.zlbovolini.keymanager.comum.validacao.UnicoValidator
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
    //private val consultaClienteHttpClient: ConsultaClienteHttpClient
) {

    @Transactional
    fun registra(@Valid novaChavePix: NovaChavePix): ChavePix {

        if (!isChaveUnica(novaChavePix.chave)) {
            throw ChavePixJaExistenteException("Chave já registrada");
        }

        val chavePix = novaChavePix.toModel()
        return pixRepository.save(chavePix)
    }

    private fun isChaveUnica(valor: String): Boolean {
        return UnicoValidator(entityManager).run {
            isValid(valor, null, null)
        }
    }

//    private fun isClienteItau(clienteId: String): Boolean {
//        return ClienteItauValidator(consultaClienteHttpClient).run {
//            isValid(clienteId, null, null)
//        }
//    }
}