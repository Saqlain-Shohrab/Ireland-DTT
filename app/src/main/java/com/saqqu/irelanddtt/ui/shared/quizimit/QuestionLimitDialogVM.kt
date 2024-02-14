package com.saqqu.irelanddtt.ui.shared.quizimit

import com.saqqu.irelanddtt.data.shared_prefs.Settings
import com.saqqu.irelanddtt.ui._main.MainActivityInteractionListener

class QuestionLimitDialogVM(private val listener: MainActivityInteractionListener) {

    fun saveQuestionLimit(limit: Int) {
        listener.showToast("Limit set to ${limit}")
    }
}