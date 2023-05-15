package kiu.dev.kyuhiltmvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.functions.Consumer
import io.reactivex.subscribers.DisposableSubscriber
import kiu.dev.kyuhiltmvvm.base.BaseViewModel
import kiu.dev.kyuhiltmvvm.model.LotteryCommonModel
import kiu.dev.kyuhiltmvvm.model.RandomUserModel
import kiu.dev.kyuhiltmvvm.model.repository.LotteryRepository
import kiu.dev.kyuhiltmvvm.model.repository.RandomRepository
import kiu.dev.kyuhiltmvvm.utils.L
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val lotteryRepository: LotteryRepository,
    private val randomRepository: RandomRepository
) : BaseViewModel() {

    private val _lotteryData = MutableLiveData<LotteryCommonModel>()
    val lotteryData: LiveData<LotteryCommonModel> get() = _lotteryData

    private val _randomUserData = MutableLiveData<RandomUserModel>()
    val randomUserData: LiveData<RandomUserModel> get() = _randomUserData

    fun reqLotteryData(
        method: String,
        drwNo: String
    ) {
        addDisposable(
            lotteryRepository.getLotteryData(
                method = method,
                drwNo = drwNo
            ).subscribeWith(object : DisposableSubscriber<LotteryCommonModel>() {
                    override fun onNext(t: LotteryCommonModel?) {
                        t?.let {
                            _lotteryData.value = it
                        }
                        L.d("reqLotteryData onNext t: $t")
                    }

                    override fun onError(t: Throwable?) {
                        L.d("reqRandomUserData onError t: $t")
                    }

                    override fun onComplete() {
                        L.d("reqLotteryData onComplete")
                    }

                })
        )
    }

    fun reqRandomUserData(
        results: Int
    ) {
        addDisposable(
            randomRepository.getRandomUserData(
                results = results
            ).subscribeWith(object: DisposableSubscriber<RandomUserModel>() {
                override fun onNext(t: RandomUserModel?) {
                    t?.let {
                        _randomUserData.value = it
                    }
                    L.d("reqRandomUserData onNext t: $t")
                }

                override fun onError(t: Throwable?) {
                    L.d("reqRandomUserData onError t: $t")
                }

                override fun onComplete() {
                    L.d("reqRandomUserData onComplete")
                }

            })
        )
    }
}