package com.saqqu.irelanddtt.ui._main

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.saqqu.irelanddtt.data.models.QuizDataModel
import com.saqqu.irelanddtt.data.results.OfflineResults
import com.saqqu.irelanddtt.ui.utils.Screens

interface MainActivityInteractionListener {

    fun activity(): MainActivity
    fun showToast(message: String)
    fun showCustomPopup(dialog: Dialog)
    fun navigateToFragment(fragment: Fragment)
    fun navigateTo(screen: Screens)
    fun submitResult(questions: MutableList<QuizDataModel>)
    fun getOfflineResult(): OfflineResults
    fun showHideBottomNav(show: Boolean)
}