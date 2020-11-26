package com.example.inventory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionSpec;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import pl.droidsonroids.gif.GifImageView;


public class ToDoListFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    JSONArray dataArray;

    private String mParam1;
    private String mParam2;

    ArrayList<ToDoListElement> toDoListElementArrayList = new ArrayList<>();
    ArrayList<String> itemIdArrayList = new ArrayList<>();
    ToDoListAdapter toDoListArrayAdapter;
    boolean reminder = false;
    int length = 0;

    Call post(MediaType mediaType,int position, OkHttpClient client, Callback callback) {
        RequestBody body = RequestBody.create(mediaType, "_id="+itemIdArrayList.get(position));
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ApiEndpoints.toDoEndpoint)
                .method("DELETE", body)
                .addHeader("authorization", "Bearer "+LoginActivity.prefs.getString("idToken","0"))
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public ToDoListFragment() {

    }

    public static ToDoListFragment newInstance(String param1, String param2) {
        ToDoListFragment fragment = new ToDoListFragment();
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
        RequestQueue toDoListRequestQueue = Volley.newRequestQueue(getContext());
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_to_do_list, container, false);

        ListView toDoListListView = rootView.findViewById(R.id.toDoListListView);
        GifImageView emptyToDoListImageView = rootView.findViewById(R.id.emptyToDoListImageView);

        FloatingActionButton toDoListFloatingActionButton = rootView.findViewById(R.id.toDoListFloatingActionButton);
        toDoListFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),NewToDoListItem.class);
                intent.putExtra("action","new");
                startActivity(intent);
            }
        });

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ApiEndpoints.toDoEndpoint,null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject responseObject) {
                        try {
                            dataArray = responseObject.getJSONArray("data");
                            length = dataArray.length();
                            for(int i=0;i<length;i++) {
                                if(dataArray.getJSONObject(i).getInt("time")==0)
                                {
                                    reminder = false;
                                }
                                else
                                {
                                    reminder = true;
                                }
                                itemIdArrayList.add(dataArray.getJSONObject(i).getString("_id"));
                                toDoListElementArrayList.add(new ToDoListElement(dataArray.getJSONObject(i).getString("title"), reminder));
                            }
                            toDoListArrayAdapter = new ToDoListAdapter(getContext(),0,toDoListElementArrayList);
                            if(toDoListElementArrayList.size()>=1) {
                                toDoListListView.setVisibility(View.VISIBLE);
                                emptyToDoListImageView.setVisibility(View.GONE);
                            }
                            toDoListListView.setAdapter(toDoListArrayAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

        toDoListRequestQueue.add(jsonObjectRequest);

        toDoListArrayAdapter = new ToDoListAdapter(getActivity().getApplicationContext(),0,toDoListElementArrayList);

        toDoListListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setTitle("Are you sure you want to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletionRequest(itemIdArrayList.get(position));
                                toDoListElementArrayList.remove(position);
                                toDoListArrayAdapter = new ToDoListAdapter(getContext(),0,toDoListElementArrayList);
                                toDoListListView.setAdapter(toDoListArrayAdapter);
                                //add code for deletion here
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });

        //settings item click listener
        toDoListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),AddNewItem.class);
                intent.putExtra("action","update");
                intent.putExtra("id",itemIdArrayList.get(position));
                startActivity(intent);
            }
        });

        if(toDoListElementArrayList.size()<1)
        {
            toDoListListView.setVisibility(View.GONE);
            emptyToDoListImageView.setVisibility(View.VISIBLE);
        }
        else
        {
            emptyToDoListImageView.setVisibility(View.GONE);
            toDoListListView.setVisibility(View.VISIBLE);
        }

        toDoListListView.setAdapter(toDoListArrayAdapter);

        return rootView;
    }

    void deletionRequest(String id)
    {
        JSONObject data = new JSONObject();
        try {
            data.put("_id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiEndpoints.toDoEndpoint+"/delete", data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                // String idToken = LoginActivity.prefs.getString("idToken", "");
                headers.put("authorization", "bearer "+LoginActivity.prefs.getString("idToken","0"));
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }


}