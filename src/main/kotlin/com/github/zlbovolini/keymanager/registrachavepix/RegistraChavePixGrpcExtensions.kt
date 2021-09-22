package com.github.zlbovolini.keymanager.registrachavepix

import com.github.zlbovolini.keymanager.comum.TipoChave
import com.github.zlbovolini.keymanager.comum.TipoConta
import com.github.zlbovolini.keymanager.grpc.RegistraChavePixRequest
import com.github.zlbovolini.keymanager.grpc.TipoChaveRequest
import java.util.*

fun RegistraChavePixRequest.toNovaChave(): NovaChavePix {
    return NovaChavePix(
        clienteId = clienteId,
        tipoChave = TipoChave.valueOf(tipoChave.name),
        chave = if (tipoChave == TipoChaveRequest.ALEATORIA) UUID.randomUUID().toString() else chave,
        tipoConta = TipoConta.valueOf(tipoConta.name)
    )
}