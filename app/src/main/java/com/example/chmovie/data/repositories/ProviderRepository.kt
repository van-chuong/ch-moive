package com.example.chmovie.data.repositories

import com.example.chmovie.data.models.Cast
import com.example.chmovie.data.models.CastsResponse
import com.example.chmovie.data.models.MoviesProviderResponse
import com.example.chmovie.shared.scheduler.DataResult

interface ProviderRepository {
    /**
     * Remote
     */
    suspend fun getPopularProvider(): DataResult<MoviesProviderResponse>

    suspend fun getPopularPerson(): DataResult<CastsResponse>

    suspend fun getPersonDetail(personId: Int): DataResult<Cast>
}