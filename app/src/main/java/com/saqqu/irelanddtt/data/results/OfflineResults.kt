package com.saqqu.irelanddtt.data.results

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.saqqu.irelanddtt.data.db.ResultRealmModel
import com.saqqu.irelanddtt.data.models.ResultModel
import io.realm.Realm


class OfflineResults(private val realm: Realm) {

    fun getAllResults(): MutableLiveData<MutableList<ResultModel>> {

        val results: MutableLiveData<MutableList<ResultModel>> = MutableLiveData()

        realm.executeTransactionAsync ({ realm ->
            val realmResults = realm.where(ResultRealmModel::class.java).findAll()
            val dataList: MutableList<ResultModel> = ArrayList()
            realmResults.forEach { result ->
                dataList.add(ResultModel(result))
            }
            dataList.reverse()
            results.postValue(dataList)
        }, {
           Log.e("Realm Data get success", "*************Realm Data get success****************")
        }, {
            Log.e("Realm Data get failure", "*************Realm Data get failure****************\n${it.message}")
        })

        return results
    }

    fun addResult(result: ResultRealmModel): MutableLiveData<Boolean> {

        val updateStatus: MutableLiveData<Boolean> = MutableLiveData()

        realm.executeTransactionAsync ({
            val currentIdNum = it.where(ResultRealmModel::class.java).max("id")
            val nextId = if (currentIdNum == null) 1 else currentIdNum.toInt() + 1
            result.id = nextId

            it.copyToRealmOrUpdate(result)
        }, {
            Log.e("Realm Data update success", "**************Realm Data update success***************")
            updateStatus.value = true
        }, {
            Log.e("Realm Data update failure", "**************Realm Data update failure**************\n${it.message}")
        })
        return updateStatus
    }
}