package kiu.dev.kyuhiltmvvm.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kiu.dev.kyuhiltmvvm.service.LotteryService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun lotteryService(lotteryRetrofit: Retrofit): LotteryService = lotteryRetrofit.create(LotteryService::class.java)
}