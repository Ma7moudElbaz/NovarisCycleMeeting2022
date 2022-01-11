package com.example.novariscyclemeeting2022.voting_system;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.novariscyclemeeting2022.R;

public class VotingActivity extends AppCompatActivity {

    TextView screenTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);
        initFields();
        screenTitle.setOnClickListener(v -> onBackPressed());
    }

    private void initFields() {
        screenTitle = findViewById(R.id.screen_title);
    }
}