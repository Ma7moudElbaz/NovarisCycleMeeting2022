package com.novartis.winnovators.chat.users;

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
import com.novartis.winnovators.chat.messages.MessagesActivity;

import java.util.ArrayList;
import java.util.List;


public class Users_adapter extends RecyclerView.Adapter<Users_adapter.ViewHolder> {

    private final List<User_item> items;

    private final Context mContext;

    public Users_adapter(Context context, ArrayList<User_item> items) {
        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.user_name.setText(items.get(position).getName());
        holder.date.setText(items.get(position).getUpdated_at());

        if (items.get(position).getOnline().equalsIgnoreCase("y")){
            holder.img_online.setVisibility(View.VISIBLE);
        }else {
            holder.img_online.setVisibility(View.GONE);
        }

        Glide.with(mContext)
                .load(items.get(position).getImg_profile())
                .centerCrop()
                .into(holder.img_profile);

        holder.parent_layout.setOnClickListener(v -> {
            Intent i = new Intent(mContext, MessagesActivity.class);
            i.putExtra("to_user_id",items.get(position).getId());
            i.putExtra("to_user_name",items.get(position).getName());
            i.putExtra("to_image_url",items.get(position).getImg_profile());
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView user_name, date;
        final ImageView img_profile,img_online;
        final RelativeLayout parent_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.user_name);
            date = itemView.findViewById(R.id.date);
            img_profile = itemView.findViewById(R.id.img_profile);
            img_online = itemView.findViewById(R.id.img_online);
            parent_layout = itemView.findViewById(R.id.parent_layout);

        }
    }
}