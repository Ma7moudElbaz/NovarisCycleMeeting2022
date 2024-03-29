package com.novartis.winnovators.chat.messages;

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
import com.novartis.winnovators.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;


public class Messages_adapter extends RecyclerView.Adapter<Messages_adapter.ViewHolder> {

    private final List<Message_item> items;

    private final Context mContext;
    private final String toImageUrl;

    public Messages_adapter(Context context, ArrayList<Message_item> items, String toImageUrl) {
        this.mContext = context;
        this.toImageUrl = toImageUrl;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getFromUserId() == UserUtils.getUserId(mContext)) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.content.setText(items.get(position).getMessage());

        if (items.get(position).getFromUserId() == UserUtils.getUserId(mContext)) {
            Glide.with(mContext)
                    .load(UserUtils.getUserPhoto(mContext))
                    .centerCrop()
                    .into(holder.img_profile);
        } else {
            Glide.with(mContext)
                    .load(toImageUrl)
                    .centerCrop()
                    .into(holder.img_profile);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView content;
        final ImageView img_profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.content);
            img_profile = itemView.findViewById(R.id.img_profile);

        }
    }
}