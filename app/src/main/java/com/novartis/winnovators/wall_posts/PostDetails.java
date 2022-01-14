package com.novartis.winnovators.wall_posts;

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

public class PostDetails extends AppCompatActivity {

    TextView screenTitle,likes_no,comments_no,user_name,date,content;
    ImageView img_post,img_profile;
    ProgressBar loading;
    int post_id;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        initFields();
        screenTitle.setOnClickListener(v -> onBackPressed());
        getData(post_id);
    }

    private void initFields() {
        post_id = getIntent().getIntExtra("post_id", 0);
        screenTitle = findViewById(R.id.screen_title);
        likes_no = findViewById(R.id.likes_no);
        comments_no = findViewById(R.id.comments_no);
        user_name = findViewById(R.id.user_name);
        date = findViewById(R.id.date);
        content = findViewById(R.id.content);
        img_post = findViewById(R.id.img_post);
        img_profile = findViewById(R.id.img_profile);
        loading = findViewById(R.id.loading);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please, Wait...");
        dialog.setCancelable(false);
    }

    public void getData(int post_id) {
        loading.setVisibility(View.VISIBLE);

        Webservice.getInstance().getApi().getSinglePost(UserUtils.getAccessToken(getBaseContext()), post_id).enqueue(new Callback<ResponseBody>() {
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

    private void setFields(JSONObject dataObj) throws JSONException {
        content.setText(dataObj.getString("content"));
        likes_no.setText(dataObj.getString("num_likes"));
//        comments_no.setText(dataObj.getString("num_comments"));
        date.setText(dataObj.getString("created_at"));
        user_name.setText(dataObj.getJSONObject("owner").getString("name"));

        Glide.with(getBaseContext())
                .load(dataObj.getJSONObject("owner").getString("photo"))
                .centerCrop()
                .into(img_profile);


        if (dataObj.getString("photo").equals("null")) {
            img_post.setVisibility(View.GONE);
        } else {
            img_post.setVisibility(View.VISIBLE);
            Glide.with(getBaseContext())
                    .load(dataObj.getString("photo"))
                    .centerCrop()
                    .into(img_post);
        }

//        JSONArray options_data_array = dataObj.getJSONArray("options");
//        for (int i = 0; i < options_data_array.length(); i++) {
//            JSONObject currentObject = options_data_array.getJSONObject(i);
//            options_id.add(currentObject.getString("id"));
//            options_text.add(currentObject.getString("option"));
//        }
//        // set Radio Buttons
//        initRadioGroup(options_text);
    }

}