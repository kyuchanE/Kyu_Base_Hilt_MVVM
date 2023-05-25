package kiu.dev.kyuhiltmvvm.model.repository

import kiu.dev.kyuhiltmvvm.service.RandomUserService
import javax.inject.Inject

class RandomRepository @Inject constructor(
    private val randomUserService: RandomUserService
) {

    suspend fun getRandomUserData(
        results: Int = 1
    ) = randomUserService.getRandomUser(
        results = results
    )
}