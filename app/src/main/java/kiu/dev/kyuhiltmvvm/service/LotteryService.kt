package kiu.dev.kyuhiltmvvm.service

import io.reactivex.Flowable
import kiu.dev.kyuhiltmvvm.model.LotteryCommonModel
import kiu.dev.kyuhiltmvvm.utils.Config
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface LotteryService {
    @GET(Config.LotteryService.COMMON)
    fun getLotteryCommon(
        @Query("method") method: String,
        @Query("drwNo") drwNo: String,
        @HeaderMap headers: Map<String, String> = mapOf()
    ): Flowable<LotteryCommonModel>
}