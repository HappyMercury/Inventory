package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class NewToDoListItem extends AppCompatActivity {

    int minute = 0,hour = 0,day = 0,month = 0,year = 0;
    boolean dateTimeOn = false;
    //public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm", Locale.ENGLISH); //Specify your locale
    long unixTime;
    String id="";
    String api = ApiEndpoints.toDoEndpoint;
    String title;
    String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_to_do_list_item);

        CardView cardView = findViewById(R.id.cardView);
        TextInputEditText toDoListTitleTextInput = findViewById(R.id.toDoListTitleTextInput);
        TextInputEditText toDoListDescriptionTextInput = findViewById(R.id.toDoListDescriptionTextInput);
        TextView reminderHeader = findViewById(R.id.reminderHeader);
        TimePicker timePicker = findViewById(R.id.reminderClock);
        DatePicker datePicker = findViewById(R.id.toDoListDatePicker);
        ToggleButton toggleButton = findViewById(R.id.toDoListToggleButton);

        Intent startedIntent = getIntent();
        String action = startedIntent.getStringExtra("action");

        if(action.equals("update"))
        {
            title = startedIntent.getStringExtra("title");
            description = startedIntent.getStringExtra("description");
            unixTime = startedIntent.getLongExtra("time",0);
            if(unixTime==0)
            {
                toggleButton.setChecked(false);
                dateTimeOn = false;
                timePicker.setVisibility(View.GONE);
                datePicker.setVisibility(View.GONE);
                datePicker.setEnabled(false);
                timePicker.setEnabled(false);
            }
            else {
                toggleButton.setChecked(true);
                toggleButton.setEnabled(true);
                dateTimeOn = true;
                timePicker.setEnabled(true);
                datePicker.setEnabled(true);
                timePicker.setVisibility(View.VISIBLE);
                datePicker.setVisibility(View.VISIBLE);
            }
            id = startedIntent.getStringExtra("id");

            api = api+"/update";

            System.out.println("Time:"+unixTime);
            System.out.println("Title: "+title);
            System.out.println("Description:"+description);

            toDoListDescriptionTextInput.setText(description);
            toDoListTitleTextInput.setText(title);

        }


        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == false)
                {
                    dateTimeOn = false;
                    timePicker.setVisibility(View.GONE);
                    datePicker.setVisibility(View.GONE);
                    datePicker.setEnabled(false);
                    timePicker.setEnabled(false);
                    unixTime = 0;
                    System.out.println(" after changing Time:"+unixTime);
                }
                else
                {
                    dateTimeOn = true;
                    timePicker.setEnabled(true);
                    datePicker.setEnabled(true);
                    timePicker.setVisibility(View.VISIBLE);
                    datePicker.setVisibility(View.VISIBLE);
                }
            }
        });

        //Saving note
        Button saveButton = findViewById(R.id.saveToDoListElement);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create new Item
                if(dateTimeOn)
                {
                    day = datePicker.getDayOfMonth();
                    month = datePicker.getMonth() + 1;
                    year = datePicker.getYear();

                    minute = timePicker.getCurrentMinute();
                    hour = timePicker.getCurrentHour();

                    try {
                        unixTime = timeConversion(Integer.toString(month)+"-"+Integer.toString(day)+"-"+Integer.toString(year)+" "+Integer.toString(hour)+":"+Integer.toString(minute)+":00");
                        //"yyyy-MM-dd HH:mm:ss.SSS"
                        //"mm-dd-yyyy hh:mm:ss"
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    unixTime = 0;
                }

                title = toDoListTitleTextInput.getText().toString();
                description = toDoListDescriptionTextInput.getText().toString();

                JSONObject data;

                if(action.equals("new")) {
                    data = new JSONObject();
                    try {
                        data.put("title", title);
                        data.put("description", description);
                        data.put("time", unixTime);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    data = new JSONObject();
                    try {
                        data.put("_id", id);
                        data.put("title", title);
                        data.put("description", description);
                        data.put("time", unixTime);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, api,data,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject responseArray) {
                                System.out.println("Saving time: "+unixTime);
                                Toast.makeText(NewToDoListItem.this, "Item Saved", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NewToDoListItem.this, "Item not saved", Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError
                    {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("authorization", "bearer "+getSharedPreferences("com.example.inventory",MODE_PRIVATE).getString("idToken",""));
                        return headers;
                    }
                };

                RequestQueue toDoListRequestQueue = Volley.newRequestQueue(getApplicationContext());
                toDoListRequestQueue.add(jsonArrayRequest);

                //Intent starting to do list fragment
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("FragmentToStart",MainActivity.FRAGMENT_TODOLIST);
                startActivity(intent);
            }
        });

    }

        public long timeConversion(String time) throws ParseException {
            long unixTime = 0;
//            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30")); //Specify your timezone
//            try {
//                unixTime = dateFormat.parse(time).getTime();
//                unixTime = unixTime / 1000;
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }

            SimpleDateFormat f = new SimpleDateFormat("mm-dd-yyyy hh:mm:ss");
            Date d = f.parse(time);
            String str = f.format(d);
            System.out.println("time is: "+str);
            System.out.println("time is 2nd time:"+ d.getTime());

//            String myDate = time;
//            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date date = dateFormat1.parse(myDate);
//            long epoch = date.getTime();
//            System.out.println(epoch);
            unixTime = d.getTime()/1000;
            return unixTime;
        }


}