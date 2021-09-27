package com.github.zlbovolini.keymanager.consultachavepix

import com.google.rpc.BadRequest
import io.grpc.Status
import io.grpc.protobuf.StatusProto
import io.micronaut.validation.validator.Validator
import jakarta.inject.Singleton
import javax.validation.ConstraintViolation

@Singleton
class ValidaConsultaChavePixService(
    private val validator: Validator
) {

    fun <T> valida(consulta: T, onError: (Throwable) -> Unit): Boolean {

        val constraintViolations = validator.validate(consulta)

        if (constraintViolations.isNotEmpty()) {
            handleValidationErrors(constraintViolations, onError)
        }

        return constraintViolations.isEmpty()
    }

    private fun <T> handleValidationErrors(
        constraintViolations: MutableSet<ConstraintViolation<T>>,
        onError: (Throwable) -> Unit
    ) {
        val violations = constraintViolations.map {
            BadRequest.FieldViolation.newBuilder()
                .setField(it.propertyPath.last().name)
                .setDescription(it.message)
                .build()
        }

        val details = BadRequest.newBuilder()
            .addAllFieldViolations(violations)
            .build()

        val statusProto = com.google.rpc.Status.newBuilder()
            .setCode(Status.INVALID_ARGUMENT.code.value())
            .setMessage("Parâmetros de entrada inválidos")
            .addDetails(com.google.protobuf.Any.pack(details))
            .build()

        onError.invoke(StatusProto.toStatusRuntimeException(statusProto))
    }
}