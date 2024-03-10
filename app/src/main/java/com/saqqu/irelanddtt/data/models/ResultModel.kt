package com.saqqu.irelanddtt.data.models

import com.saqqu.irelanddtt.data.db.ResultRealmModel
import java.util.Date

class ResultModel {

    var id: Int = 0
    var date: Date? = null
    var scored: Int = 0
    var outOf: Int = 0
    var scoreDivision: MutableList<ScoreDivisionModel> = ArrayList()
    var questions: MutableList<QuizDataModel> = ArrayList()

    constructor(questions: MutableList<QuizDataModel>) {
        this.questions = questions
        this.date = Date()

        outOf = questions.size

        for (model in questions) {
            val scored = model.hasCorrectSelection()
            addOrUpdateScoreDivision(model.type, if (scored) 1 else 0)
            this.scored += if (scored) 1 else 0
        }
    }

    constructor(model: ResultRealmModel) {

        this.id = model.id
        this.date = model.date
        this.scored = model.scored
        this.outOf = model.outOf
        this.scoreDivision.addAll(model.scoreDivision.map { ScoreDivisionModel(it) })
        this.questions.addAll(model.questions.map { QuizDataModel(it) })

    }

    private fun addOrUpdateScoreDivision(divisionName: String, withCorrect: Int) {
        val items = scoreDivision.filter { it.divisionName == divisionName }
        if (items.isNullOrEmpty()) {
            scoreDivision.add(ScoreDivisionModel(divisionName, withCorrect, 1))
        } else {
            scoreDivision.first { it.divisionName == divisionName }.total += 1
            scoreDivision.first { it.divisionName == divisionName }.total += withCorrect
        }
    }

    fun getScoreDivision(): String {
        var resultDivision = ""

        for (item in scoreDivision) {
            resultDivision += "${item.divisionName}: ${item.correct}/${item.total} \n"
        }

        return resultDivision
    }
}