package com.saqqu.irelanddtt.data.models

import com.google.gson.annotations.SerializedName
import com.saqqu.irelanddtt.data.db.QuizRealmModel
import java.util.Collections

data class QuizDataModel(
    @JvmField
    @SerializedName("Question")
    var question: String = "",

    @SerializedName("Options")
    var quizAnswers: MutableList<String> = mutableListOf(),

    @JvmField
    var explanation: String = "",

    @JvmField
    var type: String = "",

    var tempSelectedOption: Int = -1,
    var selectedOption: Int = -1,
    var image: String = "",
    var position: Int = 0,
    var correctOption: Int = 0,
    var shuffledOptions: MutableList<String> = ArrayList()
) {

    // This property is not serialized and is kept outside the primary constructor


    fun getOptionsShuffled(): MutableList<String> {
        if (shuffledOptions.isEmpty()) {
            shuffledOptions.addAll(quizAnswers)
            shuffledOptions.shuffle()
        }
        return shuffledOptions
    }

    // Secondary constructor to handle the creation from a QuizRealmModel
    constructor(quizModel: QuizRealmModel) : this(
        question = quizModel.question,
        quizAnswers = quizModel.quizAnswers.toMutableList(),
        explanation = quizModel.explanation,
        type = quizModel.type,
        selectedOption = quizModel.selectedOption,
        correctOption = quizModel.correctOption,
        image = quizModel.image,
        position = quizModel.id
    )

    constructor(question: String) : this (
        question = question,
        quizAnswers = arrayListOf(),
        explanation = "",
        type = "",
        selectedOption = -1,
        correctOption = -1,
        image = "",
        position = -1

    )

    fun hasCorrectSelection(): Boolean {
        val position = tempSelectedOption
        if (position < 0) {return false}
        return shuffledOptions[tempSelectedOption] == quizAnswers[0]
    }

    fun isSelectedCorrectly(): Boolean {
        return selectedOption == 0
    }
    fun getSelectedOptionActualPosition(): Int {
        try {
            return quizAnswers.indexOf(shuffledOptions[tempSelectedOption])
        } catch (_: Exception){}
        return -1
    }

    fun getSelectedOption(): String {
        try {
            return quizAnswers[selectedOption]
        } catch (_: Exception){}
        return "Not Answered"
    }

    fun getCorrectOption(): String {
        try {
            return "Correct answer: " + quizAnswers[0] + "\n\n${explanation}"
        } catch (_: Exception){}
        return ""
    }
}