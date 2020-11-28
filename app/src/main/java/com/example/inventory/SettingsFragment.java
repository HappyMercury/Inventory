package com.example.inventory;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    String localUserName = "";
    String localUserEmail = "";
    String localUserProfession="";
    String localUserImage="";

    SharedPreferences preferences;

    public SettingsFragment() {
    }


    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Uri profileImageURL = LoginActivity.signInAccount.getPhotoUrl();
        //Log.i("url",profileImageURL.toString());

        preferences = getActivity().getSharedPreferences("com.example.inventory", MODE_PRIVATE);

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        TextInputEditText nameEditText = rootView.findViewById(R.id.nameTextInputEditText);
        TextInputEditText emailEditText = rootView.findViewById(R.id.emailTextInputEditText);
        nameEditText.setText(LoginActivity.googleName);
        emailEditText.setText(LoginActivity.googleEmail);
        ImageView profileImageView = rootView.findViewById(R.id.profileImageView);


        Spinner professionSpinner = rootView.findViewById(R.id.profession);

        //making the get request to get the user details and setting the fields

//        JsonObjectRequest userDetailsRequest = new JsonObjectRequest(Request.Method.GET, ApiEndpoints.loginEndpoint, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//
//
//                    JSONObject data = response.getJSONObject("data");
//                    String name = data.getString("name");
//                    String profession = data.getString("profession");
//                    String image = data.getString("image");
//                    String email = data.getString("email");
//
//
//
//                }
//                catch(Exception e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        })
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError
//            {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("authorization", "bearer "+getContext().getSharedPreferences("com.example.inventory",MODE_PRIVATE).getString("idToken",""));
//                return headers;
//            }
//        };



        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("WORKING");
        arrayList.add("HOME");
        arrayList.add("JOB SEEKER");
        arrayList.add("BACHELOR");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        professionSpinner.setAdapter(arrayAdapter);

        //sending request to get user details
        JsonObjectRequest detailsRequest = new JsonObjectRequest(Request.Method.GET, ApiEndpoints.loginEndpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    localUserName = response.getJSONObject("data").getString("name");
                    localUserEmail = response.getJSONObject("data").getString("email");//have to take email later
                    localUserProfession = response.getJSONObject("data").getString("profession");
                    localUserImage = response.getJSONObject("data").getString("image");

                    //setting these values for the user in respective fields
                    try {
                        Glide.with(SettingsFragment.this)
                                .load(localUserImage)
                                .circleCrop()
                                .into(profileImageView);
                    }
                    catch(Exception e)
                    {
                        Log.i("Error: ",e.toString());
                    }

                    nameEditText.setText(localUserName);
                    emailEditText.setText(localUserEmail);

                    switch (localUserProfession)
                    {
                        case "home":
                            professionSpinner.setPrompt("HOME");
                            break;
                        case "working":
                            professionSpinner.setPrompt("WORKING");
                            break;
                        case "job_seekers":
                            professionSpinner.setPrompt("JOB SEEKER");
                            break;
                        case "bachelor":
                            professionSpinner.setPrompt("BACHELOR");
                            break;


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // String idToken = LoginActivity.prefs.getString("idToken", "");
                headers.put("authorization", "bearer " + preferences.getString("idToken",""));//LoginActivity.prefs.getString("idToken", "0"));
                return headers;
            }
        };

        Volley.newRequestQueue(getContext()).add(detailsRequest);


        try {
            Glide.with(this)
                    .load(LoginActivity.googlePhotoURL)
                    .circleCrop()
                    .into(profileImageView);
        }
        catch(Exception e)
        {
            Log.i("Error: ",e.toString());
        }

        return rootView;
    }

}