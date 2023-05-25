package kiu.dev.kyuhiltmvvm.service

import kiu.dev.kyuhiltmvvm.model.RandomUserModel
import kiu.dev.kyuhiltmvvm.utils.Config
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserService {
    @GET(Config.RandomUserService.RANDOM)
    suspend fun getRandomUser(
        @Query("results") results: Int
    ): Response<RandomUserModel>
}