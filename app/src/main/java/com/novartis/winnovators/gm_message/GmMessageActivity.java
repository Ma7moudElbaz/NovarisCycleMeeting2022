package com.novartis.winnovators.gm_message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.novariscyclemeeting2022.R;
import com.novartis.winnovators.utils.UserUtils;
import com.novartis.winnovators.webservice.Webservice;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GmMessageActivity extends AppCompatActivity {

    TextView screenTitle, name, role, title, message;
    ImageView img;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gm_message);
        initFields();
        screenTitle.setOnClickListener(v -> onBackPressed());
        getData();
    }

    private void initFields() {
        screenTitle = findViewById(R.id.screen_title);
        name = findViewById(R.id.name);
        role = findViewById(R.id.role);
        title = findViewById(R.id.title);
        message = findViewById(R.id.message);
        img = findViewById(R.id.img);
        loading = findViewById(R.id.loading);
    }


    private void setFields(JSONObject dataObj) throws JSONException {
        name.setText(dataObj.getString("name"));
        role.setText(dataObj.getString("role"));
        title.setText(dataObj.getString("title"));
        message.setText(Html.fromHtml(dataObj.getString("message"), Html.FROM_HTML_MODE_COMPACT));

        String image_url = "https://winnovators.eventonlineregister.com/"+dataObj.getString("profile_image");
        Glide.with(getBaseContext())
                .load(image_url)
                .centerCrop()
                .placeholder(R.drawable.ic_account)
                .error(R.drawable.ic_account)
                .into(img);
    }

    public void getData() {
        loading.setVisibility(View.VISIBLE);

        Webservice.getInstance().getApi().getGmMessage(UserUtils.getAccessToken(getBaseContext())).enqueue(new Callback<ResponseBody>() {
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