package com.example.novariscyclemeeting2022.notifications;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.novariscyclemeeting2022.R;
import com.example.novariscyclemeeting2022.agenda.Agenda_item;

import java.util.ArrayList;
import java.util.List;


public class Notifications_adapter extends RecyclerView.Adapter<Notifications_adapter.ViewHolder> {

    private final List<Notification_item> items;

    private final Context mContext;

    public Notifications_adapter(Context context, ArrayList<Notification_item> items) {
        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(items.get(position).getTitle());
        holder.message.setText(Html.fromHtml(items.get(position).getMessage(), Html.FROM_HTML_MODE_COMPACT));
        holder.date.setText(items.get(position).getCreated_at());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView title,message, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            message = itemView.findViewById(R.id.message);
            date = itemView.findViewById(R.id.date);

        }
    }
}