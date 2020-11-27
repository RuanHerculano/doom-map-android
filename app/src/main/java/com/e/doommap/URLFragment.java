package com.e.doommap;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;

public class URLFragment extends Fragment {
    private EditText urlF;
    private Button   sendB;

    Context context;

    public URLFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_url, container, false);

        context = getContext();

        urlF = rootView.findViewById(R.id.url);
        sendB = rootView.findViewById(R.id.sendB);

        SharedPreferences sharedPref = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        String url = sharedPref.getString("url", "http://localhost:8080");
        urlF.setText(url);

        sendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("url", urlF.getText().toString());
                editor.apply();

                getFragmentManager().popBackStack();
            }
        });

        return rootView;
    }
}
