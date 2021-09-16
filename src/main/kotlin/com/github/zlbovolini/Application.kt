package com.github.zlbovolini

import io.micronaut.runtime.Micronaut.*

fun main(args: Array<String>) {
    build()
        .args(*args)
        .packages("com.github.zlbovolini")
        .start()
}

