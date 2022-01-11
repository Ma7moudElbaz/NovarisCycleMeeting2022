package com.example.novariscyclemeeting2022.gm_message;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.novariscyclemeeting2022.R;

public class GmMessageActivity extends AppCompatActivity {

    TextView screenTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gm_message);
        initFields();
        screenTitle.setOnClickListener(v -> onBackPressed());
    }

    private void initFields() {
        screenTitle = findViewById(R.id.screen_title);
    }
}