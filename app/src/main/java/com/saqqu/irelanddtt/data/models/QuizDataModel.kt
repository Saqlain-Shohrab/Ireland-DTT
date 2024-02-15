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
    var selectedOption: Int = 0,
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

    fun hasCorrectSelection(): Boolean {
        val position = tempSelectedOption
        if (position < 0) {return false}
        return shuffledOptions[tempSelectedOption] == quizAnswers[0]
    }
}