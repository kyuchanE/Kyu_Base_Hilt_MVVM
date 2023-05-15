package kiu.dev.kyuhiltmvvm.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kiu.dev.kyuhiltmvvm.service.LotteryService
import kiu.dev.kyuhiltmvvm.service.RandomUserService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun lotteryService(@NetworkModule.LotteryModule lotteryRetrofit: Retrofit): LotteryService = lotteryRetrofit.create(LotteryService::class.java)

    @Provides
    @Singleton
    fun randomUserService(@NetworkModule.RandomUserModule randomUserService: Retrofit): RandomUserService = randomUserService.create(RandomUserService::class.java)
}