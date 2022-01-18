package com.novartis.winnovators.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novariscyclemeeting2022.R;
import com.novartis.winnovators.HomeActivity;
import com.novartis.winnovators.utils.UserUtils;
import com.novartis.winnovators.webservice.Webservice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements BottomSheet_forgot_password.EmailSubmitListener {
    public void showForgotPassSheet() {
        BottomSheet_forgot_password passBottomSheet =
                new BottomSheet_forgot_password(getBaseContext());
        passBottomSheet.show(getSupportFragmentManager(), "forgot_password");
    }

    Button login;
    EditText employerCode, password;
    TextView forgotPassword;
    CheckBox rememberMe;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initFields();
        employerCode.setText(UserUtils.getLoginName(getBaseContext()));
        password.setText(UserUtils.getLoginPassword(getBaseContext()));

        login.setOnClickListener(v -> login());

        forgotPassword.setOnClickListener(v -> showForgotPassSheet());

    }

    private void initFields() {
        login = findViewById(R.id.btn_login);
        employerCode = findViewById(R.id.et_employer_code);
        password = findViewById(R.id.et_password);
        forgotPassword = findViewById(R.id.tv_forgot_password);
        rememberMe = findViewById(R.id.chk_remember_me);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please, Wait...");
        dialog.setCancelable(false);
    }

    public void login() {
        Map<String, String> map = new HashMap<>();
        final String emailtxt = employerCode.getText().toString();
        final String passwordtxt = password.getText().toString();

        if (emailtxt.length() == 0 || passwordtxt.length() == 0) {
            Toast.makeText(getBaseContext(), "Please dill all fields", Toast.LENGTH_SHORT).show();
        } else {
            map.put("email", emailtxt);
            map.put("password", passwordtxt);
            dialog.show();
            Webservice.getInstance().getApi().login(map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.code() == 200) {
                            JSONObject res = new JSONObject(response.body().string());
                            UserUtils.setAccessToken(getBaseContext(), res.getString("access_token"));
                            UserUtils.setUserId(getBaseContext(), res.getJSONObject("account").getInt("id"));
                            UserUtils.setUserName(getBaseContext(), res.getJSONObject("account").getString("name"));
                            UserUtils.setUserEmail(getBaseContext(), res.getJSONObject("account").getString("email"));
                            UserUtils.setUserPhoto(getBaseContext(), res.getJSONObject("account").getString("photo"));
                            UserUtils.setLoginData(getBaseContext(), emailtxt, passwordtxt);
                            startActivity(new Intent(getBaseContext(), HomeActivity.class));
                            finish();
                        } else {

                            JSONObject res = new JSONObject(response.errorBody().string());
                            Toast.makeText(getBaseContext(), res.getString("data"), Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getBaseContext(), "Network Error", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
    }


    public void forgotPassword(String email) {
        Map<String, String> map = new HashMap<>();

        map.put("email", email);
        dialog.show();
        Webservice.getInstance().getApi().updatePassword(UserUtils.getAccessToken(getBaseContext()), map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.code() == 200) {
                        JSONObject res = new JSONObject(response.body().string());
                        Toast.makeText(getBaseContext(), res.getString("data"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Network Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }


    @Override
    public void emailSubmitListener(String emailAddress) {
        forgotPassword(emailAddress);
    }
}