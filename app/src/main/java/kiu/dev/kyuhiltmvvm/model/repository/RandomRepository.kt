package kiu.dev.kyuhiltmvvm.model.repository

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kiu.dev.kyuhiltmvvm.model.RandomUserModel
import kiu.dev.kyuhiltmvvm.service.RandomUserService
import javax.inject.Inject

class RandomRepository @Inject constructor(
    private val randomUserService: RandomUserService
) {
    fun getRandomUserData(
        results: Int = 1
    ): Flowable<RandomUserModel> {
        return randomUserService.getRandomUser(
            results = results
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}