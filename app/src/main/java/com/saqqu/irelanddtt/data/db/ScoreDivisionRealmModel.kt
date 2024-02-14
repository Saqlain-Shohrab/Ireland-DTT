package com.saqqu.irelanddtt.data.db

import com.saqqu.irelanddtt.data.models.ScoreDivisionModel
import io.realm.RealmObject
import io.realm.annotations.RealmClass

//TODO: Check -> enum QuestionType
//COV = CONTROL_OF_VEHICLE("Control of vehicle"),
//LM_ROTH = LEGAL_MATTER__RULES_OF_THE_ROAD("Legal Matters/Rules of the Road"),
//MR = MANAGING_RISK("Managing Risk"),
//SARD = SAFE_AND_RESPONSIBLE_DRIVING("Safe and Responsible Driving"),
//TM = TECHNICAL_MATTERS("Technical Matters")

@RealmClass
open class ScoreDivisionRealmModel: RealmObject {

    var divisionName: String = ""
    var correct: Int = 0
    var total: Int = 0

    constructor()

    constructor(divisionName: String,
                correct: Int,
                total: Int) {
        this.divisionName = divisionName
        this.correct = correct
        this.total = total
    }

    constructor(model: ScoreDivisionRealmModel) {
        this.divisionName = model.divisionName
        this.correct = model.correct
        this.total = model.total
    }

    constructor(model: ScoreDivisionModel) {
        this.divisionName = model.divisionName
        this.correct = model.correct
        this.total = model.total

    }

}
