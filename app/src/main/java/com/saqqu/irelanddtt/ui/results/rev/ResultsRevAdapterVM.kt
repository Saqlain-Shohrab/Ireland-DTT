package com.saqqu.irelanddtt.ui.results.rev

import com.saqqu.irelanddtt.data.models.ResultModel

class ResultsRevAdapterVM {

    fun getScoreDivision(model: ResultModel): String {
        var resultDivision = ""

        for (item in model.scoreDivision) {
            resultDivision += "${item.divisionName}: ${item.correct}/${item.total} \n"
        }

        return resultDivision
    }
}