package com.saqqu.irelanddtt.data.db

import com.saqqu.irelanddtt.data.models.QuestionType
import com.saqqu.irelanddtt.data.models.ResultModel
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.Date

@RealmClass
open class ResultRealmModel: RealmObject {

    @PrimaryKey
    var id: Int = -1
    var questions: RealmList<QuizRealmModel> = RealmList()
    var date: Date = Date()
    var scored: Int = 0
    var outOf: Int = 0
    var scoreDivision: RealmList<ScoreDivisionRealmModel> = RealmList()

    constructor()

    constructor(model: ResultRealmModel) {
        this.id = model.id
        this.questions = model.questions
        this.date = model.date
        this.scored = model.scored
        this.outOf = model.outOf
        this.scoreDivision = model.scoreDivision
    }

    constructor(result: ResultModel) {
        this.questions.addAll(result.questions.map { QuizRealmModel(it) })
        this.date = Date()
        this.scored = result.scored
        this.outOf = result.questions.size
        this.scoreDivision.addAll(result.scoreDivision.map { ScoreDivisionRealmModel(it) })
    }

}