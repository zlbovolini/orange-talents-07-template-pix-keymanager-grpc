package com.github.zlbovolini.keymanager.registrachavepix

import io.micronaut.validation.validator.constraints.EmailValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator


enum class TipoChave {
    CPF {
        override fun valida(chave: String?): Boolean {
            if (chave.isNullOrBlank()) {
                return false
            }

            if (!chave.matches("^[0-9]{11}\$".toRegex())) {
                return false
            }

            if (!CPFValidator().run {
                    initialize(null)
                    isValid(chave, null)
                }) {
                return false
            }

//            val response = try {
//                consultaClienteHttpClient.porId(clienteId)
//            } catch (e: HttpClientResponseException) {
//                return false
//            }
//
//            if (response.status().equals(HttpStatus.NOT_FOUND)) {
//                return false;
//            }
//
//            val body = response.body() ?: return false
//
//            if (body.cpf != pix.chave.valor) {
//                return false
//            }

            return true
        }
    },
    CELULAR {
        override fun valida(chave: String?): Boolean {
            if (chave.isNullOrBlank()) {
                return false
            }

            return chave.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },
    EMAIL {
        override fun valida(chave: String?): Boolean {
            if (chave.isNullOrBlank()) {
                return false
            }

            return EmailValidator().run {
                initialize(null)
                isValid(chave, null)
            }
        }
    },
    ALEATORIA {
        override fun valida(chave: String?) = chave.isNullOrBlank()
    };

    abstract fun valida(chave: String?): Boolean
}