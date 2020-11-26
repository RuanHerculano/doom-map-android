package com.e.doommap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.Holder> {
    private Context             context;
    private List<List<String>>  list;
    private Holder[]            h;

    public ListAdapter(Context c, List<List<String>> l) {
        context = c;
        list    = l;
        h       = new Holder[list.size()];
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_element, parent, false);

        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        h[position] = holder;

        holder.idView.setText(list.get(position).get(0));
        holder.dateView.setText(list.get(position).get(1));
        holder.cepView.setText(list.get(position).get(2));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView        idView, dateView, cepView;

        public Holder(View view) {
            super(view);
            idView   = view.findViewById(R.id.crimeID);
            dateView = view.findViewById(R.id.timeOfEvent);
            cepView  = view.findViewById(R.id.cep);
        }
    }
}
