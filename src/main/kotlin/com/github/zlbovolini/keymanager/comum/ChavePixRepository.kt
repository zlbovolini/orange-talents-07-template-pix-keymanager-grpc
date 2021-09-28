package com.github.zlbovolini.keymanager.comum

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface ChavePixRepository : JpaRepository<ChavePix, Long> {
    fun existsByChaveValor(valor: String): Boolean

    fun findByUuid(uuid: String): ChavePix?

    fun findByChaveValor(chave: String): ChavePix?

    fun findAllByTitularClienteId(clienteId: String): List<ChavePix>
}