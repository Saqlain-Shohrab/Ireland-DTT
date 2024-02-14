package com.saqqu.irelanddtt.ui.profile

import androidx.lifecycle.ViewModel
import com.saqqu.irelanddtt.data.shared_prefs.Settings
import com.saqqu.irelanddtt.ui._main.MainActivityInteractionListener

class SettingsVM(private val listener: MainActivityInteractionListener): ViewModel() {

    fun saveQuestionLimit(limit: Int) {
        Settings.currentSettings(listener.activity().baseContext).questionsLimit = limit
        listener.showToast("Limit set to ${limit}")
    }

}