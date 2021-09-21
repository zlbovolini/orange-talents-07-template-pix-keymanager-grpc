package com.github.zlbovolini.keymanager.comum.itau

import com.github.zlbovolini.keymanager.registrachavepix.TipoConta
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client(id = "\${itau-erp.url}", path = "/api/v1/clientes")
interface ConsultaContaHttpClient {

    @Get("/{clienteId}/contas")
    fun porClienteTipoConta(
        @PathVariable clienteId: String,
        @QueryValue tipo: TipoConta
    ): HttpResponse<ContaResponse>
}