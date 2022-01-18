package com.novartis.winnovators.voting_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novariscyclemeeting2022.R;
import com.novartis.winnovators.utils.UserUtils;
import com.novartis.winnovators.webservice.Webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VotingDetails extends AppCompatActivity {
    TextView screenTitle;
    TextView title;
    RadioGroup radio_answers;
    Button submit;
    ProgressBar loading;
    int voting_id;
    private ProgressDialog dialog;

    List<String> options_text = new ArrayList<>();
    List<String> options_id = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_details);
        initFields();
        screenTitle.setOnClickListener(v -> onBackPressed());
        getData(voting_id);

        submit.setOnClickListener(v -> {

            if (radio_answers.getCheckedRadioButtonId() != -1) {
                RadioButton selectedRadioButton = findViewById(radio_answers.getCheckedRadioButtonId());
                final String selectedValue = selectedRadioButton.getText().toString();
                int selectedIndex = radio_answers.indexOfChild(selectedRadioButton);
                submitAnswer(options_id.get(selectedIndex));
            } else {
                Toast.makeText(getBaseContext(), "Please, Choose Answer first", Toast.LENGTH_SHORT).show();

            }


        });
    }

    private void initFields() {
        screenTitle = findViewById(R.id.screen_title);
        voting_id = getIntent().getIntExtra("voting_id", 0);
        title = findViewById(R.id.title);
        radio_answers = findViewById(R.id.radio_answers);
        submit = findViewById(R.id.btn_submit);
        loading = findViewById(R.id.loading);


        dialog = new ProgressDialog(this);
        dialog.setMessage("Please, Wait...");
        dialog.setCancelable(false);
    }

    private void initRadioGroup(List<String> data) {
//        String[] currency_symbols_options_array = new String[]{"Answer 1","Answer 2","Answer 3"};
        for (String this_option : data) {
            RadioButton rb = new RadioButton(this);
            rb.setTextSize(16);
            rb.setText(this_option);
            radio_answers.addView(rb);
        }
    }


    private void setFields(JSONObject dataObj) throws JSONException {
        title.setText(dataObj.getString("title"));

        JSONArray options_data_array = dataObj.getJSONArray("options");
        for (int i = 0; i < options_data_array.length(); i++) {
            JSONObject currentObject = options_data_array.getJSONObject(i);
            options_id.add(currentObject.getString("id"));
            options_text.add(currentObject.getString("option"));
        }
        // set Radio Buttons
        initRadioGroup(options_text);
    }

    public void getData(int voting_id) {
        loading.setVisibility(View.VISIBLE);

        Webservice.getInstance().getApi().getSingleVoting(UserUtils.getAccessToken(getBaseContext()), voting_id).enqueue(new Callback<ResponseBody>() {
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

    public void submitAnswer(String option_id) {
        Map<String, String> map = new HashMap<>();


        map.put("poll_id", String.valueOf(voting_id));
        map.put("poll_options_id", option_id);
        dialog.show();
        Webservice.getInstance().getApi().submitAnswer(UserUtils.getAccessToken(getBaseContext()),map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        JSONObject res = new JSONObject(response.body().string());
                        Toast.makeText(getBaseContext(), res.getString("data"), Toast.LENGTH_LONG).show();
                        onBackPressed();
                    } else if (response.code() == 400){
                        JSONObject res = new JSONObject(response.errorBody().string());
                        Toast.makeText(getBaseContext(), res.getString("data"), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }else {
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
}