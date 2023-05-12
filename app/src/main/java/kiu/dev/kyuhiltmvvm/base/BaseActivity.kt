package kiu.dev.kyuhiltmvvm.base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ViewDataBinding
import com.google.gson.JsonObject
import com.trello.rxlifecycle3.android.ActivityEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kiu.dev.kyuhiltmvvm.R
import kiu.dev.kyuhiltmvvm.databinding.LoadingBinding
import kiu.dev.kyuhiltmvvm.utils.L
import kiu.dev.kyuhiltmvvm.utils.asBoolean
import kiu.dev.kyuhiltmvvm.utils.bind
import kiu.dev.kyuhiltmvvm.utils.bindView
import kiu.dev.kyuhiltmvvm.utils.gone
import kiu.dev.kyuhiltmvvm.utils.navigationHeight
import kiu.dev.kyuhiltmvvm.utils.setOnEvents
import kiu.dev.kyuhiltmvvm.utils.show
import kiu.dev.kyuhiltmvvm.utils.statusBarHeight
import kiu.dev.kyuhiltmvvm.utils.toast
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {
    companion object {
        /** 이벤트 주기 **/
        private const val THROTTLE_FIRST_DURATION = 500L
    }

    /** view data binding **/
    protected lateinit var binding: B
        private set

    /** data binding layoutId **/
    abstract val layoutId: Int

    /** Rx handler **/
    private val compositeDisposable = CompositeDisposable()

    /** Rx lifecycle **/
    val rxLifeCycle = BehaviorSubject.create<ActivityEvent>()

    /** Rx event 버튼 이벤트 **/
    private val btnEventsSubject = PublishSubject.create<View>()

    /** applicationContext **/
    val context: Context get() = applicationContext

    /** loading **/
    private lateinit var loadingBinding: LoadingBinding

    /** 로딩 뷰 사용 카운트 **/
    private val loadingCount = AtomicInteger()

    /** 기본 에러 핸들러 on/off **/
    var useHandleError = true

    /** dialogList **/
    private val dialogList = mutableListOf<Dialog>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = bind(layoutId)
        // 뷰 모델을ㄹ LifeCycle에 종속시킴, LifeCycle 동안 옵저버 역할을 함
        binding.lifecycleOwner = this
        binding.setOnEvents()

        loadingBinding = bindView(R.layout.loading)
        (binding.root as ViewGroup).addView(
            loadingBinding.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        rxLifeCycle.onNext(ActivityEvent.CREATE)

        btnEventsSubject
            .default()
            .doOnNext(::onBtnEvents)
            .subscribe()
    }

    override fun onResume() {
        super.onResume()
        rxLifeCycle.onNext(ActivityEvent.RESUME)
    }

    override fun onStart() {
        super.onStart()
        rxLifeCycle.onNext(ActivityEvent.START)
    }

    override fun onStop() {
        super.onStop()
        rxLifeCycle.onNext(ActivityEvent.STOP)
    }

    override fun onDestroy() {
        // rx clear : 등록된 모든 핸들 중지
        compositeDisposable.clear()

        super.onDestroy()
        rxLifeCycle.onNext(ActivityEvent.DESTROY)
    }

    /**
     * 상태바 숨김시 해당 높이값을 구해서 패딩 적용
     */
    open fun defaultPadding(container: ConstraintLayout) {
        container.setPadding(
            0,
            statusBarHeight(),
            0,
            navigationHeight()
        )
    }

    /**
     * 로딩표시
     */
    fun showLoading() {
        if (loadingCount.incrementAndGet() == 1) {
            runOnUiThread {
                loadingBinding.root.show()
            }
        }
    }

    /**
     * 로딩숨김
     */
    fun hideLoading() {
        if (loadingCount.decrementAndGet() == 0) {
            runOnUiThread {
                loadingBinding.root.gone()
            }
        }
    }

    /**
     * 다이얼로그 띄우기
     *
     * @param dialog
     */
    @Synchronized
    fun showDialog(dialog: Dialog) {
        if (!isFinishing) {
            dialogList.forEach {
                if (it.isShowing) it.dismiss()
                dialogList.remove(it)
            }
            dialogList.add(dialog)
            dialog.show()
        }
    }

    /**
     * 다이얼로그 숨김
     *
     * @param dialog
     */
    @Synchronized
    fun hideDialog(dialog: Dialog) {
        if (!isFinishing) {
            dialog.dismiss()
            dialogList.remove(dialog)
        }
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

    fun <T> PublishSubject<T>.default(): Observable<T> {
        return this.observeOn(AndroidSchedulers.mainThread())
            .throttleFirst(THROTTLE_FIRST_DURATION, TimeUnit.MILLISECONDS)
            .doOnSubscribe(::addDisposable)
    }

    /**
     * 버튼 이벤트 처리
     *
     * @param v
     */
    open fun onBtnEvents(v: View) {}

    /**
     * 버튼 이벤트 처리 RX
     *
     * @param v
     */
    fun onRxBtnEvents(v: View) {
        btnEventsSubject.onNext(v)
    }

    fun isIntentAvailable(intent: Intent): Boolean {
        val list: List<ResolveInfo> = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        L.d("isIntentAvailable : ${list.isNotEmpty()} ")
        return list.isNotEmpty()
    }
}