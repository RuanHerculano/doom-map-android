package com.e.doommap;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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

public class AddFragment extends Fragment {
    private EditText dateF, idF, cepF;
    private Button   sendB;

    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    String dateS;

    Context context;

    public AddFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);

        context = getContext();

        idF = rootView.findViewById(R.id.crimeID);
        cepF = rootView.findViewById(R.id.cep);
        dateF = rootView.findViewById(R.id.timeOfEvent);
        sendB = rootView.findViewById(R.id.sendB);

        dateF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        myYear = year;
                        myday = day;
                        myMonth = month;
                        Calendar c = Calendar.getInstance();
                        hour = c.get(Calendar.HOUR);
                        minute = c.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                myHour = hourOfDay;
                                myMinute = minute;
                                dateS = myYear + "-" + myMonth + "-" + myday + " " + myHour + ":" + myMinute + ":00";
                                dateF.setText(dateS);
                            }
                        }, hour, minute, DateFormat.is24HourFormat(getContext()));
                        timePickerDialog.show();
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        sendB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddTask().execute("http://192.168.0.22:8080/api/reports");
            }
        });

        return rootView;
    }

    private class AddTask extends AsyncTask<String, Void, ResponseEntity<String>> {

        @Override
        protected ResponseEntity<String> doInBackground(String... urls) {

            JSONObject content = new JSONObject();
            try {
                content.put("crimeID", idF.getText().toString());
                content.put("timeOfEvent", dateS);
                content.put("cep", cepF.getText().toString());
            }catch(Exception e) { e.printStackTrace(); }

            RequestCreator creator;
            creator = new RequestCreator();
            creator.json = content;
            creator.url = urls[0];
            return creator.postRequest(HttpMethod.POST, context);
        }

        @Override
        protected void onPostExecute(ResponseEntity<String> r) {
            getFragmentManager().popBackStack();
        }
    }
}
