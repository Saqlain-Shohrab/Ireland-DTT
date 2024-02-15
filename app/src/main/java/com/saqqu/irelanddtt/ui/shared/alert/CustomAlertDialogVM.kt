package com.saqqu.irelanddtt.ui.shared.alert

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saqqu.irelanddtt.ui._main.MainActivityInteractionListener

class CustomAlertDialogVM(

    private val listener: MainActivityInteractionListener,
    val title: String,
    val leftButtonText: String,
    val rightButtonText: String

): ViewModel() {


}