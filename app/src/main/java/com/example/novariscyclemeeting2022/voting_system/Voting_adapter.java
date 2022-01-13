package com.example.novariscyclemeeting2022.voting_system;

import android.content.Context;
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


public class Voting_adapter extends RecyclerView.Adapter<Voting_adapter.ViewHolder> {

    private final List<Voting_item> items;

    private final Context mContext;

    public Voting_adapter(Context context, ArrayList<Voting_item> items) {
        this.mContext = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.title.setText(items.get(position).getTitle());
        holder.date.setText(items.get(position).getCraeated_at());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView title, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);

        }
    }
}