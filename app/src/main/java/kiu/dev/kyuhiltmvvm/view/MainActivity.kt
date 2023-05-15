package kiu.dev.kyuhiltmvvm.view

import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kiu.dev.kyuhiltmvvm.R
import kiu.dev.kyuhiltmvvm.base.BaseActivity
import kiu.dev.kyuhiltmvvm.databinding.ActivityMainBinding
import kiu.dev.kyuhiltmvvm.utils.L
import kiu.dev.kyuhiltmvvm.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutId: Int
    = R.layout.activity_main
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initObserve()

        binding.btnLottery.setOnClickListener {
            viewModel.reqLotteryData(
                method = "getLottoNumber",
                drwNo = "111"
            )
        }

        binding.btnUser.setOnClickListener {
            viewModel.reqRandomUserData(
                results = 3
            )
        }

    }

    private fun initObserve() {
        with(viewModel) {
            lotteryData.observe(this@MainActivity) {
                L.d("Main observe : $it")
            }

            randomUserData.observe(this@MainActivity) {
                L.d("Main observe : $it")
            }
        }
    }
}