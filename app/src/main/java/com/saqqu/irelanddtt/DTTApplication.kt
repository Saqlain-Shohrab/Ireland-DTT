package com.saqqu.irelanddtt

import android.app.Application
import android.content.Context
import com.saqqu.irelanddtt.data.DTTRealmMigration
import io.realm.Realm
import io.realm.RealmConfiguration

class DTTApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .schemaVersion(0)
            .migration(DTTRealmMigration())
            .build()
        Realm.setDefaultConfiguration(config)

        try {
            Realm.getDefaultInstance()
        } catch (e: java.lang.Exception) {

            e.printStackTrace()
            print("*** DTT_IRELAND CRITICAL ERROR ***\n MIGRATION NEEDED.\n\nMIGRATION NEEDED.\n\nMIGRATION NEEDED.\n\nMIGRATION NEEDED.\n\nMIGRATION NEEDED.\n\nMIGRATION NEEDED.\n\n")
            print("*** DTT_IRELAND CRITICAL ERROR ***\n MIGRATION NEEDED.\n\nMIGRATION NEEDED.\n\nMIGRATION NEEDED.\n\nMIGRATION NEEDED.\n\nMIGRATION NEEDED.\n\nMIGRATION NEEDED.\n\n")
            print("*** DTT_IRELAND CRITICAL ERROR ***\n MIGRATION NEEDED.\n\nMIGRATION NEEDED.\n\nMIGRATION NEEDED.\n\nMIGRATION NEEDED.\n\nMIGRATION NEEDED.\n\nMIGRATION NEEDED.\n\n")
            print("*** DTT_IRELAND CRITICAL ERROR ***\n MIGRATION NEEDED.\n\nMIGRATION NEEDED.\n\nMIGRATION NEEDED.\n\nMIGRATION NEEDED.\n\nMIGRATION NEEDED.\n\nMIGRATION NEEDED.\n\n")

            val legacyAppsConfig = RealmConfiguration.Builder()
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build()

            Realm.setDefaultConfiguration(legacyAppsConfig)
        }

        context = this
        instance = this
    }

    companion object {

        private val TAG = DTTApplication::class.java.simpleName

        var context: Context? = null
            private set

        @get:Synchronized
        var instance: DTTApplication? = null
            private set
    }
}