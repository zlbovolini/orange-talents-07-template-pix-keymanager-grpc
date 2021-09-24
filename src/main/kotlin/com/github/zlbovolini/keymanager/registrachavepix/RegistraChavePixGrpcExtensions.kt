package com.github.zlbovolini.keymanager.registrachavepix

import com.github.zlbovolini.keymanager.comum.TipoChave
import com.github.zlbovolini.keymanager.comum.TipoConta
import com.github.zlbovolini.keymanager.grpc.RegistraChavePixRequest

fun RegistraChavePixRequest.toNovaChave(): NovaChavePix {
    return NovaChavePix(
        clienteId = clienteId,
        tipoChave = TipoChave.valueOf(tipoChave.name),
        chave = chave,
        tipoConta = TipoConta.valueOf(tipoConta.name)
    )
}