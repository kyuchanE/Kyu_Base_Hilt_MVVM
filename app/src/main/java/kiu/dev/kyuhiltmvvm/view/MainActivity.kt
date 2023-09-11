package kiu.dev.kyuhiltmvvm.view

import android.content.Intent
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutId: Int
    = R.layout.activity_main

    private val viewModel: MainViewModel by viewModels()

    private lateinit var resultDialog: ResultDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.result = ""

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

        binding.btnTest.setOnClickListener {
            viewModel.reqTestData()
        }

        binding.btnGoSub.setOnClickListener {
            startActivity(
                Intent(this, SubActivity::class.java)
            )
        }

    }

    private fun initObserve() {
        // TODO chan liefcycleScope 'UNRECHANGEABLE CODE'
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
                binding.result = "${randomUserData.results.get(0).name.first} ${randomUserData.results.get(0).name.last}"
                resultDialog = ResultDialog(this@MainActivity).apply {
                    initContents(randomUserData.toString())
                    initBtnClickEvent(
                        confirmAction = { L.d("randomUser confirm") },
                        closeAction = { L.d("randomUser close") }
                    )
                }
            }
        }

        lifecycleScope.launch {
            viewModel.oneTestData.collect{
                L.d("Main liefcycleScope oneTestData : $it")
            }
        }

        lifecycleScope.launch {
            viewModel.twoTestData.collect{
                L.d("Main liefcycleScope twoTestData : $it")
            }
        }

    }
}