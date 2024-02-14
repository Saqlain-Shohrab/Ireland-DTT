package com.saqqu.irelanddtt.data.results.utils

import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.RealmResults

fun <T: RealmModel> RealmResults<T>.asLiveData() = LiveRealmData<T>(this)

fun createOrGetRealmObject(realm: Realm, userId: String, realmClass: Class<out RealmModel>) =
    helperCreateOrGetRealmObject(realm, userId, realmClass, false)

fun createOrGetUserSharedRealmObject(realm: Realm, userId: String, realmClass: Class<out RealmModel>) =
    helperCreateOrGetRealmObject(realm, userId, realmClass, true)

private fun helperCreateOrGetRealmObject(realm: Realm, userId: String, realmClass: Class<out RealmModel>, isShared: Boolean): RealmObject? {

    // To create a shared object, we just use a common id instead of userId

    val id: Int = if (isShared) {
        0
    } else {
        try {
            Integer.parseInt(userId)
        } catch (e: NumberFormatException) {
            1
        }
    }

    var realmObject = realm.where(realmClass).equalTo("id", id).findFirst() as RealmObject?

    if (realmObject == null) {

        realm.executeTransaction {
            realmObject = realm.createObject(realmClass, id) as RealmObject
        }
    }

    return realmObject
}