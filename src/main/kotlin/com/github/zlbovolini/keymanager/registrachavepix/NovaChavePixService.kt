package com.github.zlbovolini.keymanager.registrachavepix

import com.github.zlbovolini.keymanager.comum.exception.ChavePixJaExistenteException
import com.github.zlbovolini.keymanager.comum.itau.ConsultaClienteHttpClient
import com.github.zlbovolini.keymanager.comum.validacao.ClienteItauValidator
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import javax.validation.Valid

@Singleton
@Validated
class NovaChavePixService(
    private val pixRepository: ChavePixRepository,
    private val consultaClienteHttpClient: ConsultaClienteHttpClient
) {

    fun registra(@Valid novaChavePix: NovaChavePix): ChavePix {

        if (pixRepository.existsByChaveValor(novaChavePix.chave)) {
            throw ChavePixJaExistenteException("Chave já registrada");
        }

        val chavePix = novaChavePix.toModel()
        return pixRepository.save(chavePix)
    }


    private fun isClienteItau(clienteId: String): Boolean {
        return ClienteItauValidator(consultaClienteHttpClient).run {
            isValid(clienteId, null, null)
        }
    }
}