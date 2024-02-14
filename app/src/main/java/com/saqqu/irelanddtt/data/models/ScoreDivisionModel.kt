package com.saqqu.irelanddtt.data.models

import com.saqqu.irelanddtt.data.db.ScoreDivisionRealmModel

class ScoreDivisionModel {

    var divisionName: String = ""
    var correct: Int = 0
    var total: Int = 0

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

}
