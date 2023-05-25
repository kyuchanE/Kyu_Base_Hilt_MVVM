package kiu.dev.kyuhiltmvvm.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kiu.dev.kyuhiltmvvm.R
import kiu.dev.kyuhiltmvvm.base.BaseActivity
import kiu.dev.kyuhiltmvvm.databinding.ActivityMainBinding
import kiu.dev.kyuhiltmvvm.utils.L
import kiu.dev.kyuhiltmvvm.view.dialog.ResultDialog
import kiu.dev.kyuhiltmvvm.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutId: Int
    = R.layout.activity_main

    private val viewModel: MainViewModel by viewModels()

    private lateinit var resultDialog: ResultDialog

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
        lifecycleScope.launch {
            with(viewModel) {
                this.lotteryData.collect { lotteryData ->
                    L.d("Main collect lottery : $lotteryData")
                    resultDialog = ResultDialog(this@MainActivity).apply {
                        initContents(lotteryData.toString())
                        initBtnClickEvent(
                            confirmAction = { L.d("lottery confirm") },
                            closeAction = { L.d("lottery close") }
                        )
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.randomUserData.collect{ randomUserData ->
                L.d("Main collect randomUser : $randomUserData")
                resultDialog = ResultDialog(this@MainActivity).apply {
                    initContents(randomUserData.toString())
                    initBtnClickEvent(
                        confirmAction = { L.d("randomUser confirm") },
                        closeAction = { L.d("randomUser close") }
                    )
                }
            }
        }

    }
}