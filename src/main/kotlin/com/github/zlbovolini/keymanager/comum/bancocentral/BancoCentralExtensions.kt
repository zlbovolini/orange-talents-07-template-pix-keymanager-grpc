package com.github.zlbovolini.keymanager.comum.bancocentral

import com.github.zlbovolini.keymanager.comum.ChavePix
import com.github.zlbovolini.keymanager.comum.Conta
import com.github.zlbovolini.keymanager.comum.Titular

fun ChavePix.toRegistraChavePixBCBRequest(): RegistraChavePixBCBRequest {
    return RegistraChavePixBCBRequest(
        tipoChave = chave.tipoChave.tipoChaveBCBRequest,
        chave = chave.valor,
        conta = conta.toContaCBCRequest(),
        dono = titular.toTitularChavePixBCBRequest()
    )
}

fun Conta.toContaCBCRequest(): ContaBCBRequest {
    return ContaBCBRequest(
        agencia = agencia,
        numero = numero,
        tipoConta = tipoConta.tipoContaBCBRequest
    )
}

fun Titular.toTitularChavePixBCBRequest(): TitularChavePixBCBRequest {
    return TitularChavePixBCBRequest(
        tipoCliente = TipoClienteBCBRequest.NATURAL_PERSON,
        nome = nome,
        cpf = cpf
    )
}