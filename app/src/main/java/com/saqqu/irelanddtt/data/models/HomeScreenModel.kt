package com.saqqu.irelanddtt.data.models

class HomeScreenModel {

    val id: Int? = null
    val name: String? = null

}

enum class QuestionType(val type: String) {
    CONTROL_OF_VEHICLE("Control of Vehicle"),
    LEGAL_MATTER__RULES_OF_THE_ROAD("Legal Matters/Rules of the Road"),
    MANAGING_RISK("Managing Risk"),
    SAFE_AND_RESPONSIBLE_DRIVING("Safe and Responsible Driving"),
    TECHNICAL_MATTERS("Technical Matters"),
    ALL("");


    companion object {
        fun construct(type: String): QuestionType {
            return entries.find { it.type == type } ?: ALL
        }
    }
}