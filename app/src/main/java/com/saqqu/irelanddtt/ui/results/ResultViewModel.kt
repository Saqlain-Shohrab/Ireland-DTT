package com.saqqu.irelanddtt.ui.results

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saqqu.irelanddtt.data.models.ResultModel

class ResultViewModel(val result: ResultModel): ViewModel() {

    val resultDataRetrieved = MutableLiveData<ResultModel>()

    fun requestResultData()  {
        resultDataRetrieved.value = result
    }

}