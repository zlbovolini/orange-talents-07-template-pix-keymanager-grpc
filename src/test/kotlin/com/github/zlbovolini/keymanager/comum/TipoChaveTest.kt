package com.github.zlbovolini.keymanager.comum

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class TipoChaveTest {

    @Nested
    inner class CPF {

        @Test
        fun `deve validar cpf`() {
            val cpf = "02467781054"

            val isValid = TipoChave.CPF.valida(cpf)

            assertTrue(isValid)
        }

        @Test
        fun `nao deve validar cpf nulo`() {
            val cpf = null

            val isValid = TipoChave.CPF.valida(cpf)

            assertFalse(isValid)
        }

        @Test
        fun `nao deve validar cpf invalido`() {
            val cpf = "11111111112"

            val isValid = TipoChave.CPF.valida(cpf)

            assertFalse(isValid)
        }
    }

    @Nested
    inner class CELULAR {

        @Test
        fun `deve validar celular`() {
            val celular = "+55996179999"

            val isValid = TipoChave.CELULAR.valida(celular)

            assertTrue(isValid)
        }

        @Test
        fun `nao deve validar celular nulo`() {
            val celular = null

            val isValid = TipoChave.CELULAR.valida(celular)

            assertFalse(isValid)
        }

        @Test
        fun `nao deve validar celular invalido`() {
            val celular = "99999999999"

            val isValid = TipoChave.CELULAR.valida(celular)

            assertFalse(isValid)
        }
    }

    @Nested
    inner class EMAIL {

        @Test
        fun `deve validar email`() {
            val email = "lucas.bovolini@zup.com.br"

            val isValid = TipoChave.EMAIL.valida(email)

            assertTrue(isValid)
        }

        @Test
        fun `nao deve validar email nulo`() {
            val email = null

            val isValid = TipoChave.EMAIL.valida(email)

            assertFalse(isValid)
        }

        @Test
        fun `nao deve validar email invalido`() {
            val email = "lucas.bovolini.zup.com.br"

            val isValid = TipoChave.EMAIL.valida(email)

            assertFalse(isValid)
        }
    }

    @Nested
    inner class ALEATORIA {

        @Test
        fun `deve validar chave aleatoria nula`() {
            val chave = null

            val isValid = TipoChave.ALEATORIA.valida(chave)

            assertTrue(isValid)
        }

        @Test
        fun `deve validar chave aleatoria vazia`() {
            val chave = ""

            val isValid = TipoChave.ALEATORIA.valida(chave)

            assertTrue(isValid)
        }

        @Test
        fun `nao deve validar chave aleatoria invalida`() {
            val chave = "a"

            val isValid = TipoChave.ALEATORIA.valida(chave)

            assertFalse(isValid)
        }
    }
}