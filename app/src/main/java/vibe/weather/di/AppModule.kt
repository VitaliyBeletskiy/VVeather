package vibe.weather.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vibe.weather.data.IGeocodingRepository
import vibe.weather.domain.GetLocationsUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideGetLocationsUseCase(
        geocodingRepository: IGeocodingRepository,
    ): GetLocationsUseCase = GetLocationsUseCase(geocodingRepository)
}
