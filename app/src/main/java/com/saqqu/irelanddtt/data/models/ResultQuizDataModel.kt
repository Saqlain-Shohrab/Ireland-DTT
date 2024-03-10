package com.saqqu.irelanddtt.data.models

class ResultQuizDataModel {

    var result: QuizDataModel? = null
    var isExplanationHidden: Boolean = false

    constructor(result: QuizDataModel) {
        this.result = result
        this.isExplanationHidden = false
    }
}