package com.github.zlbovolini.keymanager.registrachavepix

import com.github.zlbovolini.keymanager.comum.ChavePix

interface RegistraChavePixEvent {

    fun executa(chavePix: ChavePix)
}