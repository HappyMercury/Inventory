package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserDetailsSelect extends AppCompatActivity {

    Button registerbtn;
    EditText username;
    String Profession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_select);
        registerbtn = findViewById(R.id.btnContinue);
        username = findViewById(R.id.editTxtUsersNamenew);
        Spinner professionSpinner = findViewById(R.id.ProfessionSelect);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("WORKING");
        arrayList.add("HOME");
        arrayList.add("JOB SEEKER");
        arrayList.add("BACHELOR");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        professionSpinner.setAdapter(arrayAdapter);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getApplicationContext(),MainActivity.class);

                switch (professionSpinner.getSelectedItemPosition())
                {
                    case 0:
                        Profession = "working";
                        break;
                    case 1:
                        Profession = "home";
                        break;
                    case 2:
                        Profession = "job_seekers";
                        break;
                    case 3:
                        Profession = "bachelors";
                        break;


                }

                JSONObject data = new JSONObject();
                try {
                    data.put("profession",Profession);
                    data.put("name",username.getText().toString());
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiEndpoints.loginEndpoint,data,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject responseObject) {

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getActivity(), "Item not saved", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError
                    {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        // String idToken = LoginActivity.prefs.getString("idToken", "");
                        headers.put("authorization", "bearer "+LoginActivity.prefs.getString("idToken","0"));
                        return headers;
                    }
                };

                Volley.newRequestQueue(UserDetailsSelect.this).add(jsonObjectRequest);
                startActivity(new Intent(UserDetailsSelect.this, MainActivity.class));
            }

        });
    }
}