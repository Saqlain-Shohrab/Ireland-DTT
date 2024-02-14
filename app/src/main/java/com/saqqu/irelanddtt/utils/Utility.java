package com.saqqu.irelanddtt.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.saqqu.irelanddtt.R;
import com.saqqu.irelanddtt.data.models.QuizModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utility {
    public static QuizModel getJson(Context context) {
        //AssetManager assetManager = getAssets();
        InputStream is = context.getResources().openRawResource(R.raw.output_final_img_);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while (true) {
            try {
                if ((line = reader.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stringBuilder.append(line).append('\n');
        }
        String jsonString = stringBuilder.toString();
        Gson gson = new Gson();
        QuizModel data = gson.fromJson(jsonString, QuizModel.class);
        return data;
    }
}
