package com.saqqu.irelanddtt.data.repos.questions

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.saqqu.irelanddtt.DTTApplication
import com.saqqu.irelanddtt.data.models.QuestionType
import com.saqqu.irelanddtt.data.models.QuizDataModel
import com.saqqu.irelanddtt.data.shared_prefs.Settings
import com.saqqu.irelanddtt.utils.Utility

open class QuestionsRepo(private val context: Context? = DTTApplication.context) {

    protected val onQuestionsRetrieved = MutableLiveData<List<QuizDataModel>>()
    protected val onError = MutableLiveData<Boolean>()

    private fun shouldGetFromServer(): Boolean {
        return false
    }

    private fun shouldIncludeFromServer(): Boolean {
        return false
    }

    private fun requestAllQuestions() {
        val mainModel = Utility.getJson(context)
        onQuestionsRetrieved.value = mainModel?.questions
    }

    open fun requestShuffledLimited() {
        //GlobalScope.launch (Dispatchers.Main){
        val mainModelNullable = Utility.getJson(context)
        val quizCount = Settings.currentSettings(context).questionsLimit
        val type = Settings.currentSettings(context).questionsType
        mainModelNullable?.let { mainModel ->
            val questionsByType: List<QuizDataModel> = mainModel.questions
                .filter {
                    it.type.startsWith(type.type)
                }
            val wrongAnswers = questionsByType.filter { it.quizAnswers.size > 4 }
            Log.e("Errors in Data", "*************************************")
            wrongAnswers.forEach {
                Log.e("Position of Error", "${it.position} ${it.question}")
            }
            Log.e("Errors in Data", "*************************************")

            //For index safety, in case of data loss and lesser data
            val limiter = if (questionsByType.size < quizCount) questionsByType.size else quizCount

            val shuffledList = questionsByType.shuffled().subList(0, limiter)
            onQuestionsRetrieved.value = shuffledList
            return
        }
        onError.value = true
        //}
    }

    fun notifyDataReceived() = onQuestionsRetrieved
    fun notifyErrorReceived() = onError
}