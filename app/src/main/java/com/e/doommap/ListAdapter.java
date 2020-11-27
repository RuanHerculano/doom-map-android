package com.e.doommap;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.Holder> {
    private Context             context;
    private List<List<String>>  list;
    private Holder[]            h;
    private FragmentTransaction ft;

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
        holder.dateView.setText(str2date(list.get(position).get(1)));
        holder.cepView.setText(list.get(position).get(2));

        holder.baseV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewFragment vFragment = new ViewFragment();
                vFragment.id = Integer.parseInt(list.get(position).get(0));

                ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.container, vFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView        idView, dateView, cepView;
        LinearLayout    baseV;

        public Holder(View view) {
            super(view);
            idView   = view.findViewById(R.id.crimeID);
            dateView = view.findViewById(R.id.timeOfEvent);
            cepView  = view.findViewById(R.id.cep);
            baseV    = view.findViewById(R.id.base);
        }
    }

    private String str2date(String s){
        String result = "";
        List<String> list = new ArrayList<>();

        try {
            JSONArray arr = new JSONArray(s);
            list.add(arr.getString(0));
            list.add(String.format("%2s", arr.getString(1)).replace(" ", "0"));
            list.add(String.format("%2s", arr.getString(2)).replace(" ", "0"));
            list.add(String.format("%2s", arr.getString(3)).replace(" ", "0"));
            list.add(String.format("%2s", arr.getString(4)).replace(" ", "0"));
        } catch (Exception e) {}

        result = list.get(0) + "-" + list.get(1) + "-" + list.get(2) + " " + list.get(3) + ":" + list.get(4) + ":00";
        return result;
    }
}
