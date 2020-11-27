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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditFragment extends Fragment {
    private EditText dateF, idF, cepF;
    private Button   sendB;

    public List<String> list;

    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    String dateS;

    Context context;

    public EditFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit, container, false);

        context = getContext();

        idF = rootView.findViewById(R.id.crimeID);
        cepF = rootView.findViewById(R.id.cep);
        dateF = rootView.findViewById(R.id.timeOfEvent);
        sendB = rootView.findViewById(R.id.sendB);

        idF.setText(list.get(0));
        dateF.setText(str2date(list.get(1)));
        cepF.setText(list.get(2));

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
                                dateS = myYear + "-" +
                                        String.format("%2s", myMonth).replace(" ", "0") + "-" +
                                        String.format("%2s", myday).replace(" ", "0") + " " +
                                        String.format("%2s", myHour).replace(" ", "0") + ":" +
                                        String.format("%2s", myMinute).replace(" ", "0") + ":00";
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
                SharedPreferences sharedPref = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
                String url = sharedPref.getString("url", "http://localhost:8080");
                new UpdateTask().execute(url+"/api/reports/"+list.get(0));
            }
        });

        return rootView;
    }

    private class UpdateTask extends AsyncTask<String, Void, ResponseEntity<String>> {

        @Override
        protected ResponseEntity<String> doInBackground(String... urls) {

            JSONObject content = new JSONObject();
            try {
                content.put("crimeID", idF.getText().toString());
                content.put("timeOfEvent", dateF.getText().toString());
                content.put("cep", cepF.getText().toString());
            }catch(Exception e) { e.printStackTrace(); }

            RequestCreator creator;
            creator = new RequestCreator();
            creator.json = content;
            creator.url = urls[0];
            return creator.postRequest(HttpMethod.PUT, context);
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
