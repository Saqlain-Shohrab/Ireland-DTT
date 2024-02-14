package com.saqqu.irelanddtt.ui._main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saqqu.irelanddtt.data.db.ResultRealmModel
import com.saqqu.irelanddtt.data.models.QuizDataModel
import com.saqqu.irelanddtt.data.models.ResultModel
import com.saqqu.irelanddtt.data.results.OfflineResults
import com.saqqu.irelanddtt.utils.Helper
import io.realm.Realm

class MainViewModel (
    private val context: Context,
    private val realm: Realm,
    private val resultDB: OfflineResults = OfflineResults(realm),
    private val listener: MainActivityInteractionListener = Helper().getNavigator(context)
): ViewModel() {

    fun addResult(quizSet: MutableList<QuizDataModel>): MutableLiveData<Boolean> {
        val result = ResultModel(questions = quizSet)
        val realmResult = ResultRealmModel(result)
        return resultDB.addResult(realmResult)
    }

    fun getResults(): MutableLiveData<MutableList<ResultModel>> {

        return resultDB.getAllResults()
    }

    fun getResultsDB(): OfflineResults {
        return resultDB
    }

}