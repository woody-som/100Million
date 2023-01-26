package com.taetae.taetaesocialservice.network.data

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest (
    val email: String? = null,
    val password: String? = null
)
