package com.github.zlbovolini.keymanager.registrachavepix

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface ChavePixRepository : JpaRepository<ChavePix, Long> {
    fun existsByChaveValor(valor: String): Boolean
}