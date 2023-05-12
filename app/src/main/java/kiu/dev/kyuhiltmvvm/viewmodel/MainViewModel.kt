package kiu.dev.kyuhiltmvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.functions.Consumer
import io.reactivex.subscribers.DisposableSubscriber
import kiu.dev.kyuhiltmvvm.base.BaseViewModel
import kiu.dev.kyuhiltmvvm.model.LotteryCommonModel
import kiu.dev.kyuhiltmvvm.model.repository.LotteryRepository
import kiu.dev.kyuhiltmvvm.utils.L
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val lotteryRepository: LotteryRepository
) : BaseViewModel() {

    private val _lotteryData = MutableLiveData<LotteryCommonModel>()
    val lotteryData: LiveData<LotteryCommonModel> get() = _lotteryData

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
                        L.d("reqLotteryData onError t: ${t?.message}")
                    }

                    override fun onComplete() {
                        L.d("reqLotteryData onComplete")
                    }

                })
        )
    }
}