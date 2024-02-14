package com.saqqu.irelanddtt.utils;

import android.content.Context;

import com.saqqu.irelanddtt.ui._main.MainActivityInteractionListener;

public class Helper {

    public MainActivityInteractionListener getNavigator(Context context) {
        try {
            return (MainActivityInteractionListener) context;
        } catch (Exception e) {
            //throw ();
        }

        return null;
    }
}
