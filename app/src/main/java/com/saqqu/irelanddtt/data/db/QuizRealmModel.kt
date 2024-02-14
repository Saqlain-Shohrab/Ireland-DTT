package com.saqqu.irelanddtt.data.db

import com.saqqu.irelanddtt.data.models.QuizDataModel
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class QuizRealmModel: RealmObject {

    //Primary Key
    var id: Int = -1

    var question = ""
    var quizAnswers: RealmList<String> = RealmList()
    var explanation = ""
    var type = ""
    var selectedOption = -1
    var correctOption = -1
    var image = ""

    constructor()

    constructor(quizModel: QuizDataModel) {
        this.id = quizModel.position
        this.question = quizModel.question
        for (item in quizModel.quizAnswers) {
            this.quizAnswers.add(item)
        }
        this.explanation = quizModel.explanation
        this.type = quizModel.type
        this.selectedOption = quizModel.selectedOption
        this.correctOption = quizModel.correctOption
        this.image = quizModel.image
    }

    constructor(quizRealmModel: QuizRealmModel) {
        this.id = quizRealmModel.id
        this.question = quizRealmModel.question
        this.quizAnswers = quizRealmModel.quizAnswers
        this.explanation = quizRealmModel.explanation
        this.type = quizRealmModel.type
        this.selectedOption = quizRealmModel.selectedOption
        this.correctOption = quizRealmModel.correctOption
        this.image = quizRealmModel.image
    }
}