package com.saqqu.irelanddtt.ui.dtt

import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saqqu.irelanddtt.R
import com.saqqu.irelanddtt.data.models.QuizDataModel
import com.saqqu.irelanddtt.data.repos.QuestionsRepo
import com.saqqu.irelanddtt.ui._main.MainActivityInteractionListener
import java.text.SimpleDateFormat
import java.util.Date

class DTTViewModel(
    private val repo: QuestionsRepo,
    val onQuestionsRetrieved: MediatorLiveData<MutableList<QuizDataModel>> = MediatorLiveData<MutableList<QuizDataModel>>(),
    private var currentPosition: Int = -1,
    private val listener: MainActivityInteractionListener
) : ViewModel() {

    private val onQuestionsReceived = repo.notifyDataReceived()
    private var quizCountLimit: Int = 0

    val onNextButtonTextChanged: MutableLiveData<String> = MutableLiveData()
    val onQuestionChanged: MutableLiveData<String> = MutableLiveData()
    val onOptionsChanged: MutableLiveData<List<String>> = MutableLiveData()
    val onOptionsCheckedChanged: MutableLiveData<Int> = MutableLiveData()
    val onClearChecked: MutableLiveData<Boolean> = MutableLiveData()
    val onImageChanged: MutableLiveData<Int> = MutableLiveData()
    val onImageVisibilityChanged: MutableLiveData<Int> = MutableLiveData()
    val onExplanationChanged: MutableLiveData<String> = MutableLiveData()
    val onTimeChanged: MutableLiveData<String> = MutableLiveData()

    private var timer: CountDownTimer? = null

    private fun startTimer() {

        if (this::timer != null) return

        val totalTime: Long = 1000L * 60 * quizCountLimit
        val interval: Long = 1000
        timer = object : CountDownTimer(totalTime, interval) {
            override fun onTick(p0: Long) {
                val currentTimeRemaining = SimpleDateFormat("mm:ss").format( Date(p0))
                onTimeChanged.value = currentTimeRemaining
            }

            override fun onFinish() {

            }

        }.start()
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
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
        val checkedId = getCheckButtonId(selectedOption)
        onOptionsCheckedChanged.value = checkedId
    }

    private fun submitForResult() {
        onQuestionsRetrieved.value?.let { listener.submitResult(it) }
    }

    private fun addHomeDataSource() {

        onQuestionsRetrieved.addSource(onQuestionsReceived) { listQuiz ->
            quizCountLimit = listQuiz.size
            onQuestionsRetrieved.value = listQuiz as MutableList<QuizDataModel>?
            goToNext()
            startTimer()
        }
    }

    private fun removeHomeDataSource() {
        onQuestionsRetrieved.removeSource(onQuestionsReceived)
    }


    fun updateSelection(i: Int) {
        //onQuestionsRetrieved.value?.get(i)?.tempSelectedOption = i
        onQuestionsRetrieved.value?.let { currentList ->
            if (currentPosition in currentList.indices) {
                // Create a deep copy of the list to ensure immutability
                val updatedList = currentList.toMutableList()
                // Create a copy of the item with the updated property
                val updatedItem = currentList[currentPosition].copy(tempSelectedOption = i)
                // Update the list with the modified item
                updatedList[currentPosition] = updatedItem
                // Post the updated list back to the LiveData
                removeHomeDataSource()
                onQuestionsRetrieved.value = updatedList

            }
        }
    }

    private fun updateShuffled(shuffledList: MutableList<String>) {
        //onQuestionsRetrieved.value?.get(i)?.tempSelectedOption = i
        onQuestionsRetrieved.value?.let { currentList ->
            if (currentPosition in currentList.indices) {
                // Create a deep copy of the list to ensure immutability
                val updatedList = currentList.toMutableList()
                // Create a copy of the item with the updated property
                val updatedItem = currentList[currentPosition].copy(shuffledOptions = shuffledList)
                // Update the list with the modified item
                updatedList[currentPosition] = updatedItem
                removeHomeDataSource()
                // Post the updated list back to the LiveData
                onQuestionsRetrieved.value = updatedList

            }
        }
    }

    private fun getCurrentQuestion(): QuizDataModel? {
        return if (currentPosition in 0..<quizCountLimit) {onQuestionsRetrieved.value?.get(currentPosition)} else null
    }

    private fun getCheckButtonId(selectedOption: Int): Int {
        return when(selectedOption) {
            1 -> R.id.option1
            2 -> R.id.option2
            3 -> R.id.option3
            4 -> R.id.option4
            else -> 0
        }
    }

    fun optionsIndexOutOfBound(position: Int) {
        Log.e("{${this::class.java.name}}","Issue with current position {$currentPosition} with option {$position}")
    }
}