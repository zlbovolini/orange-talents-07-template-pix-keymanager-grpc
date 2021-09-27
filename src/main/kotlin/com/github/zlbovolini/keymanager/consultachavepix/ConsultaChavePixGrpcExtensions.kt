package com.github.zlbovolini.keymanager.consultachavepix

import com.github.zlbovolini.keymanager.grpc.ConsultaPorIdOuChaveRequest
import com.github.zlbovolini.keymanager.grpc.ConsultaPorIdOuChaveRequest.FiltroCase.*
import com.github.zlbovolini.keymanager.grpc.DadosChavePixResponse
import com.github.zlbovolini.keymanager.grpc.DadosContaResponse
import com.github.zlbovolini.keymanager.grpc.TitularResponse
import io.micronaut.validation.validator.Validator
import javax.validation.ConstraintViolationException

fun toResponse(chavePixInfo: ChavePixInfo): DadosChavePixResponse {
    return with(chavePixInfo) {
        DadosChavePixResponse.newBuilder()
            .setChaveId(chaveId)
            .setClienteId(clienteId)
            .setTipoChave(tipoChave)
            .setChave(chave)
            .setTitular(
                TitularResponse.newBuilder()
                    .setNome(titular.nome)
                    .setCpf(titular.cpf)
                    .build()
            )
            .setConta(
                DadosContaResponse.newBuilder()
                    .setInstituicao(conta.instituicao)
                    .setAgencia(conta.agencia)
                    .setNumero(conta.numero)
                    .setTipoConta(conta.tipoConta)
                    .build()
            )
            .build()
    }
}

fun ConsultaPorIdOuChaveRequest.toFiltro(validator: Validator): Filtro {
    val filtro = when (filtroCase) {
        PORID -> porId.let {
            Filtro.ConsultaPorId(it.pixId, it.clienteId)
        }
        CHAVE -> Filtro.ConsultaPorChave(chave)
        FILTRO_NOT_SET -> Filtro.Invalido()
    }

    val violations = validator.validate(filtro)
    if (violations.isNotEmpty()) {
        throw ConstraintViolationException(violations)
    }

    return filtro
}