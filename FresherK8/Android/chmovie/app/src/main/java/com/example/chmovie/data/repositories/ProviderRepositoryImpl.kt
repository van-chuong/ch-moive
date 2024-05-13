package com.example.chmovie.data.repositories

import com.example.chmovie.data.models.Cast
import com.example.chmovie.data.models.CastsResponse
import com.example.chmovie.data.models.MoviesProviderResponse
import com.example.chmovie.data.source.ProviderDataSource
import com.example.chmovie.shared.scheduler.DataResult

class ProviderRepositoryImpl(private val providerDataSource: ProviderDataSource.Remote) : ProviderRepository {
    override suspend fun getPopularProvider(): DataResult<MoviesProviderResponse> {
        return try {
            val response = providerDataSource.getPopularProvider()
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun getPopularPerson(): DataResult<CastsResponse> {
        return try {
            val response = providerDataSource.getPopularPerson()
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun getPersonDetail(personId: Int): DataResult<Cast> {
        return try {
            val response = providerDataSource.getPersonById(personId)
            DataResult.Success(response)
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }
}