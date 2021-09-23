package com.github.zlbovolini.keymanager.comum

import com.github.zlbovolini.keymanager.comum.bancocentral.TipoContaBCBRequest

enum class TipoConta(
    val tipoContaBCBRequest: TipoContaBCBRequest
) {
    CONTA_CORRENTE(TipoContaBCBRequest.CACC),
    CONTA_POUPANCA(TipoContaBCBRequest.SVGS)
}
