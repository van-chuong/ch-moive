package com.example.chmovie.data.source

import com.example.chmovie.data.models.Cast
import com.example.chmovie.data.models.CastsResponse
import com.example.chmovie.data.models.MoviesProviderResponse

interface ProviderDataSource {
    interface Local {
    }

    interface Remote {

        suspend fun getPopularProvider(): MoviesProviderResponse

        suspend fun getPopularPerson(): CastsResponse

        suspend fun getPersonById(personId: Int): Cast
    }
}