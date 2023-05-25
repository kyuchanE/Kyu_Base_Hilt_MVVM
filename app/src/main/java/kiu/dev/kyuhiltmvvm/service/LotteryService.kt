package kiu.dev.kyuhiltmvvm.service

import kiu.dev.kyuhiltmvvm.model.LotteryCommonModel
import kiu.dev.kyuhiltmvvm.utils.Config
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

interface LotteryService {
    @GET(Config.LotteryService.COMMON)
    suspend fun getLotteryCommon(
        @Query("method") method: String,
        @Query("drwNo") drwNo: String,
        @HeaderMap headers: Map<String, String> = mapOf()
    ): Response<LotteryCommonModel>
}