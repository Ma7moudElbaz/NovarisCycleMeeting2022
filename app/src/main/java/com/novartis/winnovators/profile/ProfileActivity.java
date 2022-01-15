package com.novartis.winnovators.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.novariscyclemeeting2022.R;

public class ProfileActivity extends AppCompatActivity {

    TextView screenTitle,change_password;
    ImageView change_pp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initFields();
        screenTitle.setOnClickListener(v -> onBackPressed());
    }

    private void initFields() {
        screenTitle = findViewById(R.id.screen_title);
        change_password = findViewById(R.id.change_password);
        change_pp = findViewById(R.id.change_pp);
    }
}