package com.example.novariscyclemeeting2022;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    Button login;
    EditText employerCode, password;
    TextView forgotPassword;
    CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initFields();

        login.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), HomeActivity.class)));

    }

    private void initFields() {
        login = findViewById(R.id.btn_login);
        employerCode = findViewById(R.id.et_employer_code);
        password = findViewById(R.id.et_password);
        forgotPassword = findViewById(R.id.tv_forgot_password);
        rememberMe = findViewById(R.id.chk_remember_me);
    }
}