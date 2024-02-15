package com.saqqu.irelanddtt.ui.shared.alert

import android.app.Dialog
import android.content.Context
import com.saqqu.irelanddtt.databinding.DialogCustomAlertBinding

class CustomAlertDialog(
    context: Context,
    viewModel: CustomAlertDialogVM,
    private val completionLeft: (Dialog, Any?) -> Unit,
    private val completionRight: (Dialog, Any?) -> Unit
): Dialog(context) {

    private var binding: DialogCustomAlertBinding = DialogCustomAlertBinding.inflate(layoutInflater)

    init {

        setContentView(binding.root)

        operations()

        initData(viewModel)

    }

    private fun initData(viewModel: CustomAlertDialogVM) {
        binding.customADTitle.text = viewModel.title
        binding.customADRightBtn.text = viewModel.rightButtonText
        binding.customADLeftBtn.text = viewModel.leftButtonText
    }

    private fun operations() {

        binding.customADLeftBtn.setOnClickListener {
            completionLeft(this, null)
        }
        binding.customADRightBtn.setOnClickListener {
            completionRight(this, null)
        }
    }

}