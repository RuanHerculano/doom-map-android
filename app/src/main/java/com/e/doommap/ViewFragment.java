package com.e.doommap;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ViewFragment extends Fragment {
    private TextView dateT, idT, cepT;
    private Button   editB, deleteB;
    private List<String> list;
    public  int      id = 0;

    FragmentTransaction ft;
    Context context;

    public ViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view, container, false);

        context = getContext();

        idT     = rootView.findViewById(R.id.crimeID);
        cepT    = rootView.findViewById(R.id.cep);
        dateT   = rootView.findViewById(R.id.timeOfEvent);
        deleteB = rootView.findViewById(R.id.deleteB);
        editB = rootView.findViewById(R.id.editB);

        SharedPreferences sharedPref = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        String url = sharedPref.getString("url", "http://localhost:8080");
        new viewTask().execute(url+"/api/reports/"+id);

        deleteB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
                String url = sharedPref.getString("url", "http://localhost:8080");
                new deleteTask().execute(url+"/api/reports/"+id);
            }
        });

        editB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditFragment eFragment = new EditFragment();
                eFragment.list = list;

                ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.container, eFragment).addToBackStack(null).commit();
            }
        });

        return rootView;
    }

    private class deleteTask extends AsyncTask<String, Void, ResponseEntity<String>> {

        @Override
        protected ResponseEntity<String> doInBackground(String... urls) {
            RequestCreator creator;
            creator = new RequestCreator();
            creator.url = urls[0];
            return creator.deleteRequest(HttpMethod.DELETE, context);
        }

        @Override
        protected void onPostExecute(ResponseEntity<String> r) {
            try {
                r.getBody();
            } catch (Exception e) {
                Toast.makeText(context, "Não foi possível executar a operação", Toast.LENGTH_LONG).show();
            }
            getFragmentManager().popBackStack();
        }
    }

    private class viewTask extends AsyncTask<String, Void, ResponseEntity<String>> {

        @Override
        protected ResponseEntity<String> doInBackground(String... urls) {
            RequestCreator creator;
            creator = new RequestCreator();
            creator.url = urls[0];
            return creator.getRequest(HttpMethod.GET, context);
        }

        @Override
        protected void onPostExecute(ResponseEntity<String> r) {
            try {
                list = data2list(r.getBody());
                idT.setText(list.get(0));
                dateT.setText(str2date(list.get(1)));
                cepT.setText(list.get(2));
            } catch (Exception e) {
                Toast.makeText(context, "Não foi possível executar a operação", Toast.LENGTH_LONG).show();
            }
        }
    }

    private List<String> data2list(String result){
        List<String> list = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(result);

            list.add(obj.getString("id"));
            list.add(obj.getJSONArray("reportCrimes").getJSONObject(0).getString("timeOfEvent"));
            list.add(obj.getJSONArray("reportCrimes").getJSONObject(0).getJSONObject("address").getString("cep"));

        } catch(Exception e) { e.printStackTrace(); }
        return list;
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
