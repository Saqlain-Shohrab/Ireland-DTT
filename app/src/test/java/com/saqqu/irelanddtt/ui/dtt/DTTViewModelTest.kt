package com.saqqu.irelanddtt.ui.dtt

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.saqqu.irelanddtt.data.models.QuizDataModel
import com.saqqu.irelanddtt.data.repos.questions.QuestionsRepo
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DTTViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: DTTViewModel
    private lateinit var repo: QuestionsRepoMock

    @Before
    fun setup() {
        repo = QuestionsRepoMock()
        viewModel = DTTViewModel(repo)
    }

    @Test
    fun `Check if API is successful and question changed`() {
        // Mock data
        val question = "What is the capital of France?"
        val options = listOf("Paris", "Berlin", "London", "Rome")
        val quizDataModel = QuizDataModel(
            question = question
        )
        val data = mutableListOf<QuizDataModel>()
        data.add(quizDataModel)
        repo.questions = ArrayList()
        repo.questions?.add(quizDataModel)

        val observer = Observer<String> { questionR ->
            assert(questionR == "Q1: $question")
        }
        viewModel.onQuestionChanged.observeForever(observer)

        viewModel.requestData()

    }

    @Test
    fun `Check if error triggered if calling API fails`() {
        val errorMessageShouldBe = "Error retrieving data"
        val observer = Observer<String> { errorMessage ->
            assert(errorMessage == errorMessageShouldBe)
        }
        viewModel.onError.observeForever(observer)
        viewModel.requestData()
    }
}

class QuestionsRepoMock() :
    QuestionsRepo() {

    var questions: MutableList<QuizDataModel>? = null
    override fun requestShuffledLimited() {
        if (questions != null) {
            onQuestionsRetrieved.value = questions
        } else {
            onError.value = true
        }
    }

}