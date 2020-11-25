package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_to_do_list_item);

        CardView cardView = findViewById(R.id.cardView);
        TextInputEditText toDoListTitleTextInput = findViewById(R.id.toDoListTitleTextInput);
        TextInputEditText toDoListDescriptionTextInput = findViewById(R.id.toDoListTitleTextInput);
        TextView reminderHeader = findViewById(R.id.reminderHeader);
        TimePicker timePicker = findViewById(R.id.reminderClock);
        DatePicker datePicker = findViewById(R.id.toDoListDatePicker);
        ToggleButton toggleButton = findViewById(R.id.toDoListToggleButton);
        toggleButton.setChecked(false);

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

                    unixTime = timeConversion(Integer.toString(year)+Integer.toString(month)+Integer.toString(day)+Integer.toString(hour)+Integer.toString(minute));
                }
                else
                {
                    unixTime = 0;
                }

                String title = toDoListTitleTextInput.getText().toString();
                String description = toDoListDescriptionTextInput.getText().toString();

                JSONObject data = new JSONObject();
                try {
                    data.put("title",title);
                    data.put("description",description);
                    data.put("time",unixTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, ApiEndpoints.toDoEndpoint,data,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject responseArray) {
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
                        headers.put("authorization", "bearer "+LoginActivity.idToken);
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

        public long timeConversion(String time) {
            long unixTime = 0;
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30")); //Specify your timezone
            try {
                unixTime = dateFormat.parse(time).getTime();
                unixTime = unixTime / 1000;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return unixTime;
        }

}