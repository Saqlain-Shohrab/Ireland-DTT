package com.saqqu.irelanddtt.data.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.saqqu.irelanddtt.data.models.QuestionType;

public class Settings {

    private static final String SETTINGS_KEY = "dtt_settings";
    private static final String SETTINGS_QUESTION_LIMIT_KEY = "dtt_q_limit_settings";
    private static final String SETTINGS_QUESTION_TYPE_KEY = "dtt_q_type_settings";

    private Context context;
    private Settings() {}

    public static Settings currentSettings(Context context) {
        Settings settings = new Settings();
        settings.context = context;
        return settings;
    }

    public int getQuestionsLimit() {
        SharedPreferences settings = context.getSharedPreferences(SETTINGS_KEY, android.content.Context.MODE_PRIVATE);
        return settings.getInt(SETTINGS_QUESTION_LIMIT_KEY, 40);
    }

    public void setQuestionsLimit(int limit) {
        SharedPreferences settings = context.getSharedPreferences(SETTINGS_KEY, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SETTINGS_QUESTION_LIMIT_KEY, limit);
        editor.apply();
        editor.commit();
    }

    public QuestionType getQuestionsType() {
        SharedPreferences settings = context.getSharedPreferences(SETTINGS_KEY, android.content.Context.MODE_PRIVATE);
        String savedValue = settings.getString(SETTINGS_QUESTION_TYPE_KEY, "");
        return QuestionType.Companion.construct(savedValue);
    }

    public void setQuestionsType(QuestionType type) {
        SharedPreferences settings = context.getSharedPreferences(SETTINGS_KEY, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SETTINGS_QUESTION_TYPE_KEY, type.getType());
        editor.apply();
        editor.commit();
    }
}
