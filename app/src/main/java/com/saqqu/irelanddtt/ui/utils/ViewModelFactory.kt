package com.saqqu.irelanddtt.ui.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.saqqu.irelanddtt.data.models.QuestionType
import com.saqqu.irelanddtt.data.repos.QuestionsRepo
import com.saqqu.irelanddtt.data.results.OfflineResults
import com.saqqu.irelanddtt.ui._main.MainActivity
import com.saqqu.irelanddtt.ui._main.MainActivityInteractionListener
import com.saqqu.irelanddtt.ui._main.MainViewModel
import com.saqqu.irelanddtt.ui.dtt.DTTViewModel
import com.saqqu.irelanddtt.ui.home.HomeScreenVM
import com.saqqu.irelanddtt.ui.profile.SettingsVM
import io.realm.Realm

class ViewModelFactory {

    private inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(aClass: Class<T>): T = f() as T
        }

    fun setupMainViewModel(activity: MainActivity): MainViewModel {
        val realm = Realm.getDefaultInstance()
        return ViewModelProvider(activity, ViewModelFactory().viewModelFactory { MainViewModel(activity, realm) })[MainViewModel::class.java]
    }

    fun setupHomeScreenViewModel(listener: MainActivityInteractionListener): HomeScreenVM {
        val activity = listener.activity()
        return ViewModelProvider(activity, ViewModelFactory().viewModelFactory { HomeScreenVM(listener) })[HomeScreenVM::class.java]
    }

    fun setupDTTViewModel(listener: MainActivityInteractionListener, quizCount: Int, type: QuestionType): DTTViewModel {

        val activity = listener.activity()
        val questionsRepo = QuestionsRepo(activity.baseContext, quizCount, type = type)

        return ViewModelProvider(activity, ViewModelFactory().viewModelFactory {
            DTTViewModel(questionsRepo,
                listener = listener)
        })[DTTViewModel::class.java]
    }

    fun setUpSettingsModel(listener: MainActivityInteractionListener): SettingsVM {
        val activity = listener.activity()
        return ViewModelProvider(activity, ViewModelFactory().viewModelFactory { SettingsVM(listener) })[SettingsVM::class.java]
    }
}