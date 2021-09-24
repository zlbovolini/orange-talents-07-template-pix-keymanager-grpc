package com.github.zlbovolini.keymanager.comum.bancocentral

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

@Client(id = "\${banco-central.url}", path = "/api/v1/pix/keys")
interface BancoCentralPixClient {

    @Post(consumes = [MediaType.APPLICATION_XML], produces = [MediaType.APPLICATION_XML])
    fun registra(@Body request: RegistraChavePixBCBRequest): HttpResponse<Map<String, String>>

    @Delete(value = "/{chave}", consumes = [MediaType.APPLICATION_XML], produces = [MediaType.APPLICATION_XML])
    fun remove(
        @PathVariable chave: String,
        @Body request: RemoveChavePixBCBRequest
    ): HttpResponse<Map<String, String>>
}