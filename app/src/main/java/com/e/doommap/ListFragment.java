package com.e.doommap;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    private FragmentTransaction ft;
    public  RecyclerView gridView;
    private FloatingActionButton fab;
    private Context context;

    public ListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.content_main, container, false);
        context = getContext();

        gridView   = rootView.findViewById(R.id.gridview);
        fab = rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.container, new AddFragment()).addToBackStack(null).commit();
            }
        });

        new ListTask().execute();

        return rootView;
    }

    private class ListTask extends AsyncTask<String, Void, ResponseEntity<String>> {

        @Override
        protected ResponseEntity<String> doInBackground(String... urls) {
            RequestCreator creator;
            creator = new RequestCreator();
            creator.url = "http://192.168.0.22:8080/api/reports";
            return creator.getRequest(HttpMethod.GET, context);
        }

        @Override
        protected void onPostExecute(ResponseEntity<String> r) {
            System.out.println("RESPONSE: "+r.getBody());
            gridView.setAdapter(new ListAdapter(context, data2list(r.getBody())));
            gridView.setLayoutManager(new LinearLayoutManager(context));
            gridView.setNestedScrollingEnabled(false);
        }
    }

    private List<List<String>> data2list(String result){
        List<List<String>> main_list = new ArrayList<>();

        try {
            JSONArray arr = new JSONArray(result);

            for(int i=0; i<arr.length(); i++){
                List<String> list = new ArrayList<>();
                list.add(arr.getJSONObject(i).getString("id"));
                list.add(arr.getJSONObject(i).getJSONArray("reportCrimes").getJSONObject(0).getString("timeOfEvent"));
                list.add(arr.getJSONObject(i).getJSONArray("reportCrimes").getJSONObject(0).getJSONObject("address").getString("cep"));
                main_list.add(list);
            }
        } catch(Exception e) { e.printStackTrace(); }
        System.out.println("RESPONSE JSON: "+main_list);
        return main_list;
    }
}
