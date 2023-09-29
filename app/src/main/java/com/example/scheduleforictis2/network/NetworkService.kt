package com.example.scheduleforictis2.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {
    private const val BASE_URL = "https://webictis.sfedu.ru/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: Api = getRetrofit().create(Api::class.java)

    fun isConnectedToInternet(): Boolean {
        return true
    }

    class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause)

    sealed class NetworkResult<out T> {
        data object Loading: NetworkResult<Nothing>()
        data class Success<T>(val data: T) : NetworkResult<T>()
        data class Failure(val error: NetworkException) : NetworkResult<Nothing>()
    }
}