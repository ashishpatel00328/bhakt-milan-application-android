package com.infravo.bhaktmilan.data.remote.repository

import com.infravo.bhaktmilan.data.network.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

open class BaseRepository {

    protected suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<T>
    ): NetworkResult<T> {

        return withContext(Dispatchers.IO) {

            try {

                val response = apiCall()

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body != null) {

                        NetworkResult.Success(body)

                    } else {

                        NetworkResult.Error(
                            code = response.code(),
                            message = "Response body is empty."
                        )

                    }

                } else {

                    NetworkResult.Error(
                        code = response.code(),
                        message = response.message().ifBlank {
                            "Something went wrong."
                        }
                    )

                }

            } catch (e: Exception) {

                NetworkResult.Error(
                    message = e.localizedMessage
                        ?: "Network error occurred."
                )

            }

        }

    }

}