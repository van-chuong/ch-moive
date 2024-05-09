package com.example.chmovie.data.source.remote

import com.example.chmovie.data.models.Cast
import com.example.chmovie.data.models.CastsResponse
import com.example.chmovie.data.models.MoviesProviderResponse
import com.example.chmovie.data.source.ProviderDataSource
import com.example.chmovie.data.source.remote.api.MovieApiClient

class ProviderRemoteImpl : ProviderDataSource.Remote {
    override suspend fun getPopularProvider(): MoviesProviderResponse = MovieApiClient.instance.getPopularProvider()

    override suspend fun getPopularPerson(): CastsResponse = MovieApiClient.instance.getPopularPerson()
    override suspend fun getPersonById(personId: Int): Cast = MovieApiClient.instance.getPersonById(personId)
}