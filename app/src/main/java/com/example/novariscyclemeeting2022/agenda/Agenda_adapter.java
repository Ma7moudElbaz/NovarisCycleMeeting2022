package com.example.novariscyclemeeting2022.agenda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.novariscyclemeeting2022.R;

import java.util.ArrayList;
import java.util.List;


public class Agenda_adapter extends RecyclerView.Adapter<Agenda_adapter.ViewHolder> {

    private final List<Agenda_item> items;

    private final Context mContext;

    public Agenda_adapter(Context context, ArrayList<Agenda_item> items) {
        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.title.setText(items.get(position).getTitle());
        holder.from.setText(items.get(position).getFrom());
        holder.to.setText(String.valueOf(items.get(position).getTo()));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView title, from, to;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            from = itemView.findViewById(R.id.from);
            to = itemView.findViewById(R.id.to);

        }
    }
}