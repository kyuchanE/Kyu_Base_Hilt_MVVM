package kiu.dev.kyuhiltmvvm.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kiu.dev.kyuhiltmvvm.base.BaseViewModel
import kiu.dev.kyuhiltmvvm.model.LotteryCommonModel
import kiu.dev.kyuhiltmvvm.model.RandomUserModel
import kiu.dev.kyuhiltmvvm.model.repository.LotteryRepository
import kiu.dev.kyuhiltmvvm.model.repository.RandomRepository
import kiu.dev.kyuhiltmvvm.utils.L
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val lotteryRepository: LotteryRepository,
    private val randomRepository: RandomRepository
) : BaseViewModel() {

    private val _lotteryData = MutableSharedFlow<LotteryCommonModel>()
    val lotteryData = _lotteryData.asSharedFlow()

    private val _randomUserData = MutableSharedFlow<RandomUserModel>()
    val randomUserData= _randomUserData.asSharedFlow()

    fun reqLotteryData(
        method: String,
        drwNo: String
    ) {
        viewModelScope.launch {
            lotteryRepository.getLotteryData(
                method = method,
                drwNo = drwNo
            ).let { response ->
                if (response.isSuccessful) {
                    response.body()?.let { lotteryResponseData ->
                        _lotteryData.emit(lotteryResponseData)
                    }
                } else {
                    L.d("reqLotteryData unSuccessful : ${response.errorBody()}")
                }
            }
        }

    }

    fun reqRandomUserData(
        results: Int
    ) {
        viewModelScope.launch {
            randomRepository.getRandomUserData(
                results = results
            ).let { response ->
                if (response.isSuccessful) {
                    response.body()?.let { randomUserResponseData ->
                        _randomUserData.emit(randomUserResponseData)
                    }
                } else {
                    L.d("reqRandomUserDta unSuccessful : ${response.errorBody()}")
                }
            }
        }
    }
}