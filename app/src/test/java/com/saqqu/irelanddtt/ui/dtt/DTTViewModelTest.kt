package com.saqqu.irelanddtt.ui.dtt

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.saqqu.irelanddtt.data.models.QuizDataModel
import com.saqqu.irelanddtt.data.repos.questions.QuestionsRepo
import com.saqqu.irelanddtt.ui._main.MainActivityInteractionListener
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class DTTViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: DTTViewModel
    private lateinit var repo: QuestionsRepoMock
    private lateinit var listener: MainActivityInteractionListener
    private lateinit var lifecycleOwner: LifecycleOwner

    @Before
    fun setup() {
        repo = QuestionsRepoMock()
        listener = mock(MainActivityInteractionListener::class.java)

        lifecycleOwner = mock(LifecycleOwner::class.java)
        val lifecycle = LifecycleRegistry(lifecycleOwner)
        `when`(lifecycleOwner.lifecycle).thenReturn(lifecycle)

        viewModel = DTTViewModel(repo, listener = listener)
    }

    @Test
    fun `Check if API is successful and question changed`() {
        // Mock data
        val question = "What is the capital of France?"
        val options = listOf("Paris", "Berlin", "London", "Rome")
        val quizDataModel = QuizDataModel(
            question = question
        )

        // Mock repo data emission
        val data = mutableListOf<QuizDataModel>()
        data.add(quizDataModel)
        repo.questions = ArrayList()
        repo.questions?.add(quizDataModel)

        viewModel.onQuestionChanged.observe(lifecycleOwner) { questionR ->
            assert(questionR == "Q1: $question")
        }

        viewModel.requestData()

    }

    @Test
    fun `Check if error triggered if calling API fails`() {
        val errorMessageShouldBe = "Error retrieving data"
        viewModel.onError.observe(lifecycleOwner) { errorMessage ->
            assert(errorMessage == errorMessageShouldBe)
        }
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