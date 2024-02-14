package com.saqqu.irelanddtt.ui.shared.quizimit

import android.app.Dialog
import android.content.Context
import com.saqqu.irelanddtt.data.shared_prefs.Settings
import com.saqqu.irelanddtt.databinding.DialogQuestionLimitBinding

class QuestionLimitDialog(
    private val context: Context,
    private val viewModel: QuestionLimitDialogVM,
    private val completion: (Dialog, Int) -> Unit
): Dialog(context) {

    private var binding: DialogQuestionLimitBinding = DialogQuestionLimitBinding.inflate(layoutInflater)

    init {

        setContentView(binding.root)

        operations()
    }

    private fun operations() {

        binding.questionSetLimitNP.maxValue = 40
        binding.questionSetLimitNP.minValue = 3
        binding.questionSetLimitNP.value = Settings.currentSettings(context).questionsLimit

        binding.questionSetLimitSave.setOnClickListener {
            viewModel.saveQuestionLimit(binding.questionSetLimitNP.value)
            completion(this@QuestionLimitDialog, binding.questionSetLimitNP.value)
        }

    }

}