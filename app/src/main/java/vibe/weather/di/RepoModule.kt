package vibe.weather.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vibe.weather.data.GeocodingRepositoryImpl
import vibe.weather.data.IGeocodingRepository
import vibe.weather.data.network.IGeocodingDataSource
import vibe.weather.data.network.NetworkGeocodingDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    @Singleton
    abstract fun bindGeocodingRepository(
        geocodingRepositoryImpl: GeocodingRepositoryImpl,
    ): IGeocodingRepository

    @Binds
    @Singleton
    abstract fun bindGeocodingDataSource(
        networkGeocodingDataSource: NetworkGeocodingDataSource,
    ): IGeocodingDataSource
}
