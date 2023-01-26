package com.taetae.taetaesocialservice.network

import AuthResponse
import com.taetae.taetaesocialservice.network.data.AuthRequest
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*


object AuthRepository {
    //회원가입 URL
    private const val registerUrl = "https://hkqnznjnnjgkylcscuyy.supabase.co/auth/v1/signup"
    //로그인인URL
    private const val loginUrl = "https://hkqnznjnnjgkylcscuyy.supabase.co/auth/v1/token?"

    private const val TAG: String = "AuthRepository"

    //회원가입
    suspend fun register(email : String, password : String) : HttpResponse {
        return KtorClient.httpClient.post(registerUrl) {
            setBody(AuthRequest(email, password))
        }
    }

    //로그인
    suspend fun login(email : String, password : String) : HttpResponse {
        return KtorClient.httpClient.post(loginUrl) {
            url {
                parameters.append("grant_type","password")
            }
            setBody(AuthRequest(email, password))
        }
    }

}