package com.github.zlbovolini.keymanager.consultachavepix

import com.github.zlbovolini.keymanager.comum.ChavePix

data class ChavePixInfo(
    val chaveId: String,
    val clienteId: String,
    val tipoChave: String,
    val chave: String,
    val titular: TitularInfo,
    val conta: ContaInfo
) {

    companion object {
        fun of(chavePix: ChavePix): ChavePixInfo {
            return with(chavePix) {
                ChavePixInfo(
                    chaveId = uuid,
                    clienteId = titular.clienteId,
                    tipoChave = chave.tipoChave.name,
                    chave = chave.valor,
                    titular = TitularInfo(titular.nome, titular.cpf),
                    conta = ContaInfo(
                        instituicao = conta.instituicao,
                        ispb = conta.ispb,
                        tipoConta = conta.tipoConta.name,
                        agencia = conta.agencia,
                        numero = conta.numero
                    )
                )
            }
        }
    }
}

data class TitularInfo(
    val nome: String,
    val cpf: String
)

data class ContaInfo(
    val instituicao: String,
    val ispb: String,
    val tipoConta: String,
    val agencia: String,
    val numero: String
)
