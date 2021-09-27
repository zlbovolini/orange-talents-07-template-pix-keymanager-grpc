package com.github.zlbovolini.keymanager.comum

import com.github.zlbovolini.keymanager.comum.bancocentral.TipoContaBCB

enum class TipoConta(
    val tipoContaBCB: TipoContaBCB
) {
    CONTA_CORRENTE(TipoContaBCB.CACC),
    CONTA_POUPANCA(TipoContaBCB.SVGS)
}
