package com.novartis.winnovators.wall_posts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.novariscyclemeeting2022.R;
import com.novartis.winnovators.UserUtils;
import com.novartis.winnovators.wall_posts.comments.Comment_item;
import com.novartis.winnovators.wall_posts.comments.Comments_adapter;
import com.novartis.winnovators.webservice.Webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetails extends AppCompatActivity {

    public static void hideKeyboardActivity(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    TextView screenTitle, likes_no, comments_no, user_name, date, content;
    ImageView img_post, img_profile, add_comment, like;
    EditText et_comment;
    ProgressBar loading;
    int post_id;
    private ProgressDialog dialog;

    RecyclerView recyclerView;

    ArrayList<Comment_item> items_list;
    Comments_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        initFields();
        screenTitle.setOnClickListener(v -> onBackPressed());
        getData(post_id);

        like.setOnClickListener(v -> addLike());
        add_comment.setOnClickListener(v -> {
            if (et_comment.length() == 0) {
                Toast.makeText(getBaseContext(), "sorry, you can't add empty comment", Toast.LENGTH_SHORT).show();
            } else {
                addComment(et_comment.getText().toString());
                et_comment.setText("");
                hideKeyboardActivity(this);
            }
        });
    }

    public void addComment(String comment) {
        loading.setVisibility(View.VISIBLE);

        Webservice.getInstance().getApi().addComment(UserUtils.getAccessToken(getBaseContext()), post_id, comment).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                loading.setVisibility(View.GONE);
                try {
                    if (response.isSuccessful()) {
                        getData(post_id);
                    } else {
                        Toast.makeText(getBaseContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }


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

    public void addLike() {
        loading.setVisibility(View.VISIBLE);

        Webservice.getInstance().getApi().addLike(UserUtils.getAccessToken(getBaseContext()), post_id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                loading.setVisibility(View.GONE);
                try {
                    if (response.isSuccessful()) {
                        getData(post_id);
                    } else {
                        Toast.makeText(getBaseContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }

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

    private void initFields() {
        post_id = getIntent().getIntExtra("post_id", 0);
        screenTitle = findViewById(R.id.screen_title);
        likes_no = findViewById(R.id.likes_no);
        like = findViewById(R.id.like);
        comments_no = findViewById(R.id.comments_no);
        user_name = findViewById(R.id.user_name);
        date = findViewById(R.id.date);
        content = findViewById(R.id.content);
        img_post = findViewById(R.id.img_post);
        img_profile = findViewById(R.id.img_profile);
        loading = findViewById(R.id.loading);

        et_comment = findViewById(R.id.et_comment);
        add_comment = findViewById(R.id.add_comment);

        recyclerView = findViewById(R.id.comment_recycler);
        items_list = new ArrayList<>();

        initRecyclerView();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please, Wait...");
        dialog.setCancelable(false);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Comments_adapter(getBaseContext(), items_list);
        recyclerView.setAdapter(adapter);

    }

    private void setFields(JSONObject dataObj) throws JSONException {
        setArray(dataObj.getJSONArray("comments"));

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

        if (dataObj.getInt("is_liked") == 0){
            like.setImageResource(R.drawable.ic_like);
        }else {
            like.setImageResource(R.drawable.ic_like_active);
        }
    }

    public void setArray(JSONArray list) {
        items_list.clear();
        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);

                final int id = 0;
                final String content = currentObject.getString("content");
                final String user_name = currentObject.getJSONObject("user").getString("name");
                final String profile_img_url = currentObject.getJSONObject("user").getString("photo");

                items_list.add(new Comment_item(id, user_name, profile_img_url, content));

            }

            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}