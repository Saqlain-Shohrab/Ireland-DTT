package com.saqqu.irelanddtt.ui.dtt

import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saqqu.irelanddtt.data.models.QuizDataModel
import com.saqqu.irelanddtt.data.repos.questions.QuestionsRepo
import com.saqqu.irelanddtt.ui._main.MainActivityInteractionListener
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

class DTTViewModel(
    private var repo: QuestionsRepo,
    val onQuestionsRetrieved: MediatorLiveData<MutableList<QuizDataModel>> = MediatorLiveData<MutableList<QuizDataModel>>(),
    val gotError: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>(),
    private var currentPosition: Int = -1,
    private val listener: MainActivityInteractionListener
) : ViewModel() {

    private val onQuestionsReceived = repo.notifyDataReceived()
    private var quizCountLimit: Int = 0
    private lateinit var endTime:Date
    private val perQuestionTimeLimitInSec = 60

    val onNextButtonTextChanged: MutableLiveData<String> = MutableLiveData()
    val onQuestionChanged: MutableLiveData<String> = MutableLiveData()
    val onOptionsChanged: MutableLiveData<List<String>> = MutableLiveData()
    val onOptionsCheckedChanged: MutableLiveData<Int> = MutableLiveData()
    val onClearChecked: MutableLiveData<Boolean> = MutableLiveData()
    val onImageChanged: MutableLiveData<Int> = MutableLiveData()
    val onImageVisibilityChanged: MutableLiveData<Int> = MutableLiveData()
    val onExplanationChanged: MutableLiveData<String> = MutableLiveData()
    val onTimeChanged: MutableLiveData<String> = MutableLiveData()
    val onTimeFinished: MutableLiveData<Boolean> = MutableLiveData()
    val onError: MutableLiveData<String> = MutableLiveData()

    //Timer objects
    private var handler = Handler()
    private val timerRunnable = object : Runnable {
        override fun run() {
            val timeDifference = endTime.time - Date().time
            if (timeDifference <= 0L) {
                onTimeFinished.value = true
                changeTime("Time Over")
                stopTimeRunner()
            } else {
                val currentTimeRemaining = SimpleDateFormat("mm:ss").format(Date(timeDifference))
                changeTime(currentTimeRemaining)
                handler.postDelayed(this, 1000)
            }
        }
    }


    private fun startTimer() {
        handler = Handler()
        handler.post(timerRunnable)
    }

    private fun changeTime(remainingTime: String) {
        onTimeChanged.value = remainingTime
    }

    fun maybeStartTimer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!handler.hasCallbacks(timerRunnable)) {
                startTimer()
            }
        } else {
            stopTimeRunner()
            startTimer()
        }
    }
    fun stopTimeRunner() {
        handler.removeCallbacks(timerRunnable)
    }

    fun requestData() {
        removeHomeDataSource()
        addHomeDataSource()
        repo.requestShuffledLimited()
    }

    fun goToNext() {
        currentPosition += 1
        if (currentPosition < quizCountLimit) {
            onChange()
        } else {
            submitForResult()
            reset()
        }
    }

    fun reset() {
        currentPosition = -1
        onQuestionsRetrieved.value = mutableListOf()
        removeHomeDataSource()
    }

    fun getCurrentProgress(): Int {
        val currentProgress = (currentPosition + 1).toDouble()
        val progressPercentage = (currentProgress/quizCountLimit.toDouble() * 100)
        return progressPercentage.toInt()
    }

    private fun changeImageVisibility(visibility: Int) {
        onImageVisibilityChanged.value = visibility
    }

    private fun changeImage(image: String?, position: Int) {

        val hasImage = image.takeIf { it != null }?.trim()?.isNotEmpty()

        if (hasImage == true) {
            val imageName = "image_${position}"
            val imageResId =
                listener.activity().resources.getIdentifier(imageName, "drawable", listener.activity().packageName)
            if (imageResId != 0) {
                changeImageVisibility(View.VISIBLE)
                onImageChanged.value = imageResId
            } else {
                // Handle the case where the image resource is not found
                Log.e("ImageViewHelper", "Resource not found for image: $imageName");
                changeImageVisibility(View.GONE)
            }

        } else {
            changeImageVisibility(View.GONE)
        }
    }

    fun goToPrevious() {
        if (currentPosition > 0) {
            currentPosition -= 1
            onChange()
        }
    }

    private fun onChange() {
        val currentQuestion = getCurrentQuestion() ?: QuizDataModel()
        changeQuestion(currentQuestion)
        maybeChangeNextButtonText()
        changeImage(currentQuestion.image, currentQuestion.position)
    }

    private fun changeQuestion(data: QuizDataModel?) {
        data?.let { it ->
            onQuestionChanged.value = ("Q${currentPosition + 1}: " + it.question) ?: ""
            val shuffledList = it.getOptionsShuffled()
            onOptionsChanged.value = shuffledList
            onExplanationChanged.value = it.explanation
            updateShuffled(shuffledList)
            maybeChangeCheckedOption(it.tempSelectedOption)
        }
    }

    private fun maybeChangeNextButtonText() {
        if (currentPosition == quizCountLimit - 1) {
            onNextButtonTextChanged.value = "Submit"
        } else if (currentPosition == quizCountLimit - 2) {
            onNextButtonTextChanged.value = "Next"
        }
    }

    private fun maybeChangeCheckedOption(selectedOption: Int) {
        onOptionsCheckedChanged.value = selectedOption
    }

    fun submitForResult() {
        onQuestionsRetrieved.value?.let { listener.submitResult(it) }
    }

    private fun addHomeDataSource() {

        onQuestionsRetrieved.addSource(onQuestionsReceived) { listQuiz ->
            quizCountLimit = listQuiz.size
            onQuestionsRetrieved.value = listQuiz as MutableList<QuizDataModel>?
            goToNext()
            setEndTime(quizCountLimit)
            startTimer()
        }

        gotError.addSource(repo.notifyErrorReceived()) { isError ->
            if (isError) {
                onError.value = "Error retrieving data"
            }
        }
    }

    private fun removeHomeDataSource() {
        onQuestionsRetrieved.removeSource(onQuestionsReceived)
    }

    private fun setEndTime(quizCount: Int) {
        val currentTime = Calendar.getInstance().time
        val calendar = Calendar.getInstance().apply {
            time = currentTime
            add(Calendar.SECOND, quizCount  * perQuestionTimeLimitInSec)
        }
        endTime = calendar.time
    }


    fun updateSelection(i: Int) {
        onQuestionsRetrieved.value?.let { currentList ->
            if (currentPosition in currentList.indices) {
                val updatedList = currentList.toMutableList()
                val updatedItem = currentList[currentPosition].copy(tempSelectedOption = i)
                updatedList[currentPosition] = updatedItem
                removeHomeDataSource()
                onQuestionsRetrieved.value = updatedList
                onOptionsCheckedChanged.value = i

            }
        }
    }

    private fun updateShuffled(shuffledList: MutableList<String>) {
        onQuestionsRetrieved.value?.let { currentList ->
            if (currentPosition in currentList.indices) {
                val updatedList = currentList.toMutableList()
                val updatedItem = currentList[currentPosition].copy(shuffledOptions = shuffledList)
                updatedList[currentPosition] = updatedItem
                removeHomeDataSource()
                onQuestionsRetrieved.value = updatedList

            }
        }
    }

    private fun getCurrentQuestion(): QuizDataModel? {
        return if (currentPosition in 0..<quizCountLimit) {onQuestionsRetrieved.value?.get(currentPosition)} else null
    }
}