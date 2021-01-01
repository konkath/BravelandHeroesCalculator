package com.soltysdev.bravelandheroescalculator.settings;

import android.os.Bundle;
import android.view.View;

import com.soltysdev.bravelandheroescalculator.R;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void onChangeLanguageButtonClick(View view) {
        String language = (String) view.getTag();
        finish();
        LanguageManager.updateLanguage(getApplicationContext(), language);
    }
}
