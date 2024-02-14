package com.saqqu.irelanddtt.data

import io.realm.DynamicRealm
import io.realm.FieldAttribute
import io.realm.RealmMigration
import java.util.Date


class DTTRealmMigration : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {

        /*val schema = realm.schema
        var version = oldVersion

        if (version == 0L) {
            //Adding source field to Quiz table
            val quizTable = schema.create(RealmTables.QUIZ.name)
            val quizOptionsTable = schema.create(RealmTables.QUIZ_OPTIONS.name)
            quizTable
                ?.addField("position", Int::class.java, FieldAttribute.PRIMARY_KEY)
                ?.addField("question", String::class.java)
                ?.addRealmListField("options", quizOptionsTable)
                ?.addField("explanation", String::class.java)
                ?.addField("type", String::class.java)
                ?.addField("selectedOption", Int::class.java)
                ?.addField("image", String::class.java)

            val results = schema.create(RealmTables.RESULTS.name)
            val resultQuestionsTable = schema.get(RealmTables.QUIZ.name)

            results.addField("id", Int::class.java, FieldAttribute.PRIMARY_KEY)
            results.addField("date", Date::class.java)
            results.addRealmListField("questions", resultQuestionsTable)


            //version++
        }

        if (version == 1L) {
            version++
        }*/

    }

}
