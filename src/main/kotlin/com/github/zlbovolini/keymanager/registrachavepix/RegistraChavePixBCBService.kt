package com.github.zlbovolini.keymanager.registrachavepix

import com.github.zlbovolini.keymanager.comum.ChavePix
import com.github.zlbovolini.keymanager.comum.bancocentral.BancoCentralPixClient
import com.github.zlbovolini.keymanager.comum.bancocentral.toRegistraChavePixBCBRequest
import jakarta.inject.Singleton

@Singleton
class RegistraChavePixBCBService(
    private val bancoCentralPixClient: BancoCentralPixClient
) : RegistraChavePixEvent {

    override fun executa(chavePix: ChavePix) {
        val request = chavePix.toRegistraChavePixBCBRequest()
        val response = bancoCentralPixClient.registra(request)
    }
}