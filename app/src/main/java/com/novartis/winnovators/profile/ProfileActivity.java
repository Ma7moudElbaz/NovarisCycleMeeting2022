package com.novartis.winnovators.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.novariscyclemeeting2022.R;
import com.novartis.winnovators.UserUtils;
import com.novartis.winnovators.webservice.Webservice;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    TextView screenTitle,change_password,employer_code,name;
    ImageView change_pp,img_profile;
    ProgressBar loading;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initFields();
        screenTitle.setOnClickListener(v -> onBackPressed());
        getData();
    }

    private void initFields() {
        screenTitle = findViewById(R.id.screen_title);
        change_password = findViewById(R.id.change_password);
        employer_code = findViewById(R.id.employer_code);
        name = findViewById(R.id.name);
        change_pp = findViewById(R.id.change_pp);
        img_profile = findViewById(R.id.img_profile);
        loading = findViewById(R.id.loading);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please, Wait...");
        dialog.setCancelable(false);
    }


    private void setFields(JSONObject dataObj) throws JSONException {

        employer_code.setText(dataObj.getString("email"));
        name.setText(dataObj.getString("name"));


            Glide.with(getBaseContext())
                    .load(dataObj.getString("photo"))
                    .centerCrop()
                    .into(img_profile);

    }

    public void getData() {
        loading.setVisibility(View.VISIBLE);

        Webservice.getInstance().getApi().getProfileData(UserUtils.getAccessToken(getBaseContext())).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {
                    JSONObject responseObject = new JSONObject(response.body().string());
                    JSONObject dataObj = responseObject.getJSONObject("data");
                    setFields(dataObj);
                    loading.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.d("Error Throw", t.toString());
                Log.d("commit Test Throw", t.toString());
                Log.d("Call", t.toString());
                Toast.makeText(getBaseContext(), "Network Error", Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
            }
        });
    }
}