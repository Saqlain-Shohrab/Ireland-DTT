package com.saqqu.irelanddtt.ui.results.rev

import com.saqqu.irelanddtt.data.models.ResultModel

interface ResultsToResultListener {

    fun onResultClicked(result: ResultModel)
}