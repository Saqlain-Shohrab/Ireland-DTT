package com.saqqu.irelanddtt.data.repos

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.saqqu.irelanddtt.data.models.QuestionType
import com.saqqu.irelanddtt.data.models.QuizDataModel
import com.saqqu.irelanddtt.utils.Utility

class QuestionsRepo(private val context: Context, private val quizCount: Int, private val type: QuestionType) {

    private val onQuestionsRetrieved = MutableLiveData<List<QuizDataModel>>()

    private fun shouldGetFromServer(): Boolean {
        return false
    }

    private fun shouldIncludeFromServer(): Boolean {
        return false
    }

    private fun requestAllQuestions() {
        val mainModel = Utility.getJson(context)
        onQuestionsRetrieved.value = mainModel.questions
    }

    fun requestShuffledLimited() {
        //GlobalScope.launch (Dispatchers.Main){
            val mainModel = Utility.getJson(context)
            val questionsByType: List<QuizDataModel> = mainModel.questions
                .filter {
                it.type.startsWith((this@QuestionsRepo).type.type)
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
        //}
    }

    fun notifyDataReceived() = onQuestionsRetrieved
}