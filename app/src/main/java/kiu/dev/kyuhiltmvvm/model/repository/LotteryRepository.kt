package kiu.dev.kyuhiltmvvm.model.repository

import kiu.dev.kyuhiltmvvm.service.LotteryService
import javax.inject.Inject

class LotteryRepository @Inject constructor(
    private val lotteryService: LotteryService
) {

    suspend fun getLotteryData(
        method: String = "",
        drwNo: String = "",
        header: Map<String, String> = mapOf()
    ) = lotteryService.getLotteryCommon(
        method = method,
        drwNo = drwNo,
        headers = header
    )

}