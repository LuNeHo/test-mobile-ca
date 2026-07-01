package com.luneho.testmobileca

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform