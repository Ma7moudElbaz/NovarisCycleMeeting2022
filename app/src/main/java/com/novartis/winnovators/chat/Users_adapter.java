package com.novartis.winnovators.chat;

import android.content.Context;
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

        Glide.with(mContext)
                .load(items.get(position).getImg_profile())
                .centerCrop()
                .into(holder.img_profile);

        holder.parent_layout.setOnClickListener(v -> {
//            Intent i = new Intent(mContext, VotingDetails.class);
//            i.putExtra("voting_id",items.get(position).getId());
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            mContext.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView user_name, date;
        final ImageView img_profile;
        final RelativeLayout parent_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.user_name);
            date = itemView.findViewById(R.id.date);
            img_profile = itemView.findViewById(R.id.img_profile);
            parent_layout = itemView.findViewById(R.id.parent_layout);

        }
    }
}