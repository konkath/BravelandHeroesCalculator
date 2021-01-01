package com.soltysdev.bravelandheroescalculator.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

import androidx.preference.PreferenceManager;

abstract public class LanguageManager {
    private static final String SELECTED_LANGUAGE = "LanguageManager.SelectedLanguage";
    private static OnLanguageChangedCallback mLanguageChangedCallback;

    static void updateLanguage(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();

        mLanguageChangedCallback.languageChanged();
    }

    public static void initLanguageForContext(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String language = preferences.getString(SELECTED_LANGUAGE, null);

        if (language != null) {
            setLanguage(context, language);
        }
    }

    private static void setLanguage(Context context, String language) {
        Locale locale = new Locale(language);
        Resources res = context.getResources();
        Configuration conf = res.getConfiguration();
        conf.setLocale(locale);
        res.updateConfiguration(conf, res.getDisplayMetrics());
    }

    public interface OnLanguageChangedCallback {
        void languageChanged();
    }

    public static void setLanguageChangedCallback(OnLanguageChangedCallback callback) {
        mLanguageChangedCallback = callback;
    }
}
