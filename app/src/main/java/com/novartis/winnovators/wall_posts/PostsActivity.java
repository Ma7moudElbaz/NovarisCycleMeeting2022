package com.novartis.winnovators.wall_posts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novariscyclemeeting2022.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.novartis.winnovators.UserUtils;
import com.novartis.winnovators.webservice.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsActivity extends AppCompatActivity implements Posts_adapter.AdapterCallback{
    TextView screenTitle;

    RecyclerView recyclerView;
    ProgressBar loading;
    FloatingActionButton add_post;

    ArrayList<Post_item> items_list;
    Posts_adapter adapter;

    int currentPageNum = 1;
    int lastPageNum;
    boolean mHasReachedBottomOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        initFields();
        screenTitle.setOnClickListener(v -> onBackPressed());
        add_post.setOnClickListener(v -> startActivity(new Intent(getBaseContext(),AddPostActivity.class)));
    }

    private void initFields() {
        screenTitle = findViewById(R.id.screen_title);
        loading = findViewById(R.id.loading);
        add_post = findViewById(R.id.fab_add_post);
        recyclerView = findViewById(R.id.recycler_view);
        items_list = new ArrayList<>();

        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Posts_adapter(getBaseContext(),this, items_list);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && !mHasReachedBottomOnce) {
                    mHasReachedBottomOnce = true;

                    if (currentPageNum <= lastPageNum)
                        getData(currentPageNum);

                }
            }
        });
    }

    public void getData(int pageNum) {
        loading.setVisibility(View.VISIBLE);

        Webservice.getInstance().getApi().getPosts(UserUtils.getAccessToken(getBaseContext()), pageNum).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {
                    JSONObject responseObject = new JSONObject(response.body().string());
                    JSONArray dataArray = responseObject.getJSONArray("data");
                    setArray(dataArray);
                    JSONObject metaObject = responseObject.getJSONObject("meta");
                    lastPageNum = metaObject.getInt("last_page");

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


    public void setArray(JSONArray list) {
        try {
            for (int i = 0; i < list.length(); i++) {


                JSONObject currentObject = list.getJSONObject(i);
                final int id = currentObject.getInt("id");
                final int isliked = currentObject.getInt("is_liked");
                final String user_name = currentObject.getJSONObject("owner").getString("name");
                final String profile_img_url = currentObject.getJSONObject("owner").getString("photo");
                final String date = currentObject.getString("created_at");
                final String content = currentObject.getString("content");
                final String img_url = currentObject.getString("photo");
                final String likes_no = currentObject.getString("num_likes");
                final String comments_no = currentObject.getString("num_comments");

                items_list.add(new Post_item(id,isliked,user_name,profile_img_url,date,content,img_url,likes_no,comments_no));

            }

            adapter.notifyDataSetChanged();
            mHasReachedBottomOnce = false;
            currentPageNum++;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addLike(int post_id,int position) {
        loading.setVisibility(View.VISIBLE);

        Webservice.getInstance().getApi().addLike(UserUtils.getAccessToken(getBaseContext()), post_id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                loading.setVisibility(View.GONE);
                try {
                    if (response.isSuccessful()) {
                        updateLikesNoAndColor(position);
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

    @Override
    protected void onResume() {
        super.onResume();
        items_list.clear();
        currentPageNum =1;
        getData(currentPageNum);
    }

    @Override
    public void adapterCallback(String action, int post_id,int position) {
        addLike(post_id,position);

    }

    private void updateLikesNoAndColor(int position){
        int likesNo = Integer.parseInt(items_list.get(position).getLikes_no());
        if (items_list.get(position).getIsLiked() == 0){
            items_list.get(position).setIsLiked(1);
            items_list.get(position).setLikes_no(String.valueOf(likesNo+1));
        }else {
            items_list.get(position).setIsLiked(0);
            items_list.get(position).setLikes_no(String.valueOf(likesNo-1));
        }
        adapter.notifyDataSetChanged();
    }
}