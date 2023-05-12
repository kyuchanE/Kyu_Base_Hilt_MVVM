package kiu.dev.kyuhiltmvvm.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kiu.dev.kyuhiltmvvm.databinding.LoadingBinding
import kiu.dev.kyuhiltmvvm.utils.asInt
import kiu.dev.kyuhiltmvvm.utils.asString
import kiu.dev.kyuhiltmvvm.utils.bind
import kiu.dev.kyuhiltmvvm.utils.setOnEvents
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.atomic.AtomicInteger

abstract class BaseFragment<B: ViewDataBinding>: Fragment() {
    protected lateinit var binding: B

    abstract val layoutId: Int

    // 로딩 뷰 사용 카운트
    private val loadingCount = AtomicInteger()

    // 기본 에러 핸들러 on/off
    var useHandleError = true

    // Rx 핸들러
    private val compositeDisposable = CompositeDisposable()

    val baseActivity: BaseActivity<B>
        get() = activity as BaseActivity<B>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflater.bind(layoutId, container)
        binding.setOnEvents()
        binding.lifecycleOwner = this

        return binding.root
    }

    fun disposableClear() {
        compositeDisposable.clear()
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

    /**
     * Rx 핸들을 핸들러에 등록
     *
     * @param disposable Rx 핸들
     */
    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    /**
     * Rx 핸들을 핸들러에 제외
     *
     * @param disposable Rx 핸들
     */
    fun deleteDisposable(disposable: Disposable) {
        compositeDisposable.delete(disposable)
    }

    /**
     * Rx 핸들을 중지하고 핸들러에서 제외
     *
     * @param disposable Rx 핸들
     */
    fun removeDisposable(disposable: Disposable) {
        compositeDisposable.remove(disposable)
    }


    /**
     * 버튼 이벤트 처리
     *
     * @param v
     */
    open fun onBtnEvents(v: View) {}

    open fun initObserve() {}

    open fun onBackBtnEvent() {

    }
}