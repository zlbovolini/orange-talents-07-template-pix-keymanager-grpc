package com.github.zlbovolini.keymanager.comum.itau

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client

@Client(id = "\${itau-erp.url}", path = "/api/v1/clientes")
interface ConsultaClienteHttpClient {

    @Get("/{id}")
    fun porId(@PathVariable id: String): HttpResponse<ClienteResponse>
}