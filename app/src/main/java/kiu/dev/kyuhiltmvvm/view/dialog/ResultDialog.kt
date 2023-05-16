package kiu.dev.kyuhiltmvvm.view.dialog

import kiu.dev.kyuhiltmvvm.R
import kiu.dev.kyuhiltmvvm.base.BaseActivity
import kiu.dev.kyuhiltmvvm.base.BaseDialog
import kiu.dev.kyuhiltmvvm.databinding.DialogResultBinding

class ResultDialog(
    activity: BaseActivity<*>
): BaseDialog<DialogResultBinding>(activity) {

    override val layoutId: Int
        get() = R.layout.dialog_result

    fun initContents(str: String) {
        showDialog()
        binding.tvMessage.text = str
    }

    fun initBtnClickEvent(confirmAction: () -> Unit, closeAction: () -> Unit) {
        showDialog()
        binding.btnConfirm.setOnClickListener {
            hideDialog()
            confirmAction()
        }

        binding.btnClose.setOnClickListener {
            hideDialog()
            closeAction()
        }
    }

}