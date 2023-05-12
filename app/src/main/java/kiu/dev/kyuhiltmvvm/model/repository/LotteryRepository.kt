package kiu.dev.kyuhiltmvvm.model.repository

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kiu.dev.kyuhiltmvvm.model.LotteryCommonModel
import kiu.dev.kyuhiltmvvm.service.LotteryService
import kiu.dev.kyuhiltmvvm.utils.Config
import javax.inject.Inject

class LotteryRepository @Inject constructor(
    private val lotteryService: LotteryService
) {

    fun getLotteryData(
        method: String = "",
        drwNo: String = "",
        header: Map<String, String> = mapOf()
    ): Flowable<LotteryCommonModel> {
        return lotteryService.getLotteryCommon(
            method = method,
            drwNo = drwNo,
            headers = header
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}