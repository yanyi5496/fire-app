package com.yanyi.fire.ui.login

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val userId: Long? = null,
    val username: String? = null,
    val token: String? = null
)