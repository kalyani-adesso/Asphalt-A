package com.asphalt.android

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform