package kiu.dev.kyuhiltmvvm.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kiu.dev.kyuhiltmvvm.R
import kiu.dev.kyuhiltmvvm.base.BaseActivity
import kiu.dev.kyuhiltmvvm.databinding.ActivityMainBinding
import kiu.dev.kyuhiltmvvm.databinding.ActivitySubBinding
import kiu.dev.kyuhiltmvvm.utils.L
import kiu.dev.kyuhiltmvvm.view.dialog.ResultDialog
import kiu.dev.kyuhiltmvvm.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubActivity : BaseActivity<ActivitySubBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_sub

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.result = ""

        initObserve()

        binding.btnUser.setOnClickListener {
            viewModel.reqRandomUserData(
                results = 3
            )
        }

    }

    private fun initObserve() {
        // TODO chan liefcycleScope 'UNRECHANGEABLE CODE'
        lifecycleScope.launch {
            viewModel.randomUserData.collect{ randomUserData ->
                L.d("Sub collect randomUser : $randomUserData")
                binding.result = "${randomUserData.results.get(0).name.first} ${randomUserData.results.get(0).name.last}"
            }
        }

    }
}