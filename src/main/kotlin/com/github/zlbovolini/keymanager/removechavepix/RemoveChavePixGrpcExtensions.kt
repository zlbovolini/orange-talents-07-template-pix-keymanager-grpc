package com.github.zlbovolini.keymanager.removechavepix

import com.github.zlbovolini.keymanager.grpc.RemoveChavePixRequest

fun RemoveChavePixRequest.toRemoveChave() = RemoveChavePix(chaveId, clienteId)