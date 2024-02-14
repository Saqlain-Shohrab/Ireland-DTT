package com.saqqu.irelanddtt.ui.utils

import androidx.fragment.app.Fragment
import com.saqqu.irelanddtt.data.models.QuestionType
import com.saqqu.irelanddtt.data.results.OfflineResults
import com.saqqu.irelanddtt.ui.results.ResultsFragment
import com.saqqu.irelanddtt.ui.dtt.DTTFragment
import com.saqqu.irelanddtt.ui.home.HomeScreen
import com.saqqu.irelanddtt.ui.results.ResultFragment
import io.realm.Realm

class FragmentFactory {

    fun createFragment(screens: Screens): Fragment? {
        return when(screens) {
            Screens.HOME -> {
                null
            }

            Screens.DTT -> {
                DTTFragment(QuestionType.ALL)
            }

            Screens.RESULTS -> {
                ResultsFragment(OfflineResults(Realm.getDefaultInstance()))
            }

            Screens.RESULT -> {
                ResultFragment(null)
            }

            else -> {
                null
            }
        }

    }
}