package com.infravo.bhaktmilan.data.remote.interceptor

import com.infravo.bhaktmilan.data.storage.TokenManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(
        chain: Interceptor.Chain
    ): Response {

        val request = chain.request()

        val path = request.url.encodedPath

        // ==========================================
        // Public APIs
        // ==========================================

        val isPublicApi =
            path.endsWith("/auth/send-otp/") ||
                    path.endsWith("/auth/verify-otp/")

        // ==========================================
        // Build Request
        // ==========================================

        val requestBuilder =
            request.newBuilder()
                .addHeader(
                    "Accept",
                    "application/json"
                )

        // ==========================================
        // Authorization
        // ==========================================

        if (!isPublicApi) {

            val token = runBlocking {
                tokenManager.accessToken.firstOrNull()
            }

            if (!token.isNullOrBlank()) {

                requestBuilder.header(
                    "Authorization",
                    "Bearer $token"
                )
            }
        }

        return chain.proceed(
            requestBuilder.build()
        )
    }
}