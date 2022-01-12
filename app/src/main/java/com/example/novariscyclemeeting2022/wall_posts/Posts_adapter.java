package com.example.novariscyclemeeting2022.wall_posts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.novariscyclemeeting2022.R;

import java.util.ArrayList;
import java.util.List;


public class Posts_adapter extends RecyclerView.Adapter<Posts_adapter.ViewHolder> {

    private final List<Post_item> items;

    private final Context mContext;

    public Posts_adapter(Context context, ArrayList<Post_item> items) {
        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        if (items.get(position).getImg_url().equals("null")) {
            holder.img_post.setVisibility(View.GONE);
        } else {
            holder.img_post.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(items.get(position).getImg_url())
                    .centerCrop()
                    .into(holder.img_post);
        }
        if (items.get(position).getIsLiked() == 0){
            holder.like.setImageResource(R.drawable.ic_like);
        }else {
            holder.like.setImageResource(R.drawable.ic_like_active);
        }

        holder.user_name.setText(items.get(position).getUser_name());
        holder.date.setText(items.get(position).getDate());
        holder.content.setText(items.get(position).getContent());
        holder.likes_no.setText(items.get(position).getLikes_no());
        holder.comments_no.setText(items.get(position).getComments_no());

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

        final TextView user_name, date, content, likes_no, comments_no, share;
        final ImageView img_profile, img_post,like;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.user_name);
            date = itemView.findViewById(R.id.date);
            content = itemView.findViewById(R.id.content);
            likes_no = itemView.findViewById(R.id.likes_no);
            like = itemView.findViewById(R.id.like);
            comments_no = itemView.findViewById(R.id.comments_no);
            share = itemView.findViewById(R.id.share);
            img_profile = itemView.findViewById(R.id.img_profile);
            img_post = itemView.findViewById(R.id.img_post);

        }
    }
}