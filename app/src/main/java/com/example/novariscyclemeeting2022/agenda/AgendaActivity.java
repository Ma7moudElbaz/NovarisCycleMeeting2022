package com.example.novariscyclemeeting2022.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.novariscyclemeeting2022.R;

public class AgendaActivity extends AppCompatActivity {

    TextView screenTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        initFields();
        screenTitle.setOnClickListener(v -> onBackPressed());
    }

    private void initFields() {
        screenTitle = findViewById(R.id.screen_title);
    }
}