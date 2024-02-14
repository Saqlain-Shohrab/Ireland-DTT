package com.saqqu.irelanddtt.ui.home

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.saqqu.irelanddtt.data.shared_prefs.Settings
import com.saqqu.irelanddtt.ui._main.MainActivityInteractionListener
import com.saqqu.irelanddtt.ui.shared.quizimit.QuestionLimitDialog
import com.saqqu.irelanddtt.ui.shared.quizimit.QuestionLimitDialogVM

class HomeScreenVM(private val listener: MainActivityInteractionListener): ViewModel() {

    fun showDialog(fragment: Fragment) {

        val dialog = QuestionLimitDialog(listener.activity(), QuestionLimitDialogVM(listener)) { dialog, quizLimit ->
            Settings.currentSettings(listener.activity().baseContext).questionsLimit = quizLimit
            dialog.dismiss()
            listener.navigateToFragment(fragment)
        }
        dialog.show()
    }

}