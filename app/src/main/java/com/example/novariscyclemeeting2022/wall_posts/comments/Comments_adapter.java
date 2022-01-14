package com.example.novariscyclemeeting2022.wall_posts.comments;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.novariscyclemeeting2022.R;
import com.example.novariscyclemeeting2022.wall_posts.PostDetails;
import com.example.novariscyclemeeting2022.wall_posts.Post_item;

import java.util.ArrayList;
import java.util.List;


public class Comments_adapter extends RecyclerView.Adapter<Comments_adapter.ViewHolder> {

    private final List<Comment_item> items;

    private final Context mContext;

    public Comments_adapter(Context context, ArrayList<Comment_item> items) {
        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        holder.user_name.setText(items.get(position).getUser_name());
        holder.content.setText(items.get(position).getContent());

        Glide.with(mContext)
                .load(items.get(position).getProfile_img_url())
                .centerCrop()
                .into(holder.img_profile);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView user_name, content;
        final ImageView img_profile;
        final RelativeLayout parent_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.user_name);
            content = itemView.findViewById(R.id.content);
            img_profile = itemView.findViewById(R.id.img_profile);
            parent_layout = itemView.findViewById(R.id.parent_layout);

        }
    }
}