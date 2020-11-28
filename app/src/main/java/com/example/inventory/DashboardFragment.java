package com.example.inventory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView dashboardListView;
    FloatingActionButton dashboardFloatingActionButton;

    private String mParam1;
    private String mParam2;

    ArrayList<DashboardItem> dashboardList = new ArrayList<>();
    ArrayList<String> categoryNameList = new ArrayList<>();
    ArrayList<Integer> categoryCount = new ArrayList<>();
    DashboardAdapter dashboardAdapter;
    HashMap<String, ArrayList<String>> itemNameList = new HashMap<>();
    HashMap<String, ArrayList<Integer>> itemQuantityList = new HashMap<>();
    HashMap<String, ArrayList<String>> itemIDList = new HashMap<>();

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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



        // Inflate the layout for this fragment

        Bundle b = getArguments();
        String token = b.getString("token");
        System.out.println("Dashboard token:"+token);

        SharedPreferences preferences = getContext().getSharedPreferences("com.example.inventory", Context.MODE_PRIVATE);

        dashboardAdapter = new DashboardAdapter(getActivity(),0,dashboardList);
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiEndpoints.inventoryEndpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ArrayList<String> categoryNames = new ArrayList<>();
                try
                {
                    for(int i=0;i<response.getJSONArray("data").length();i++)
                    {
                            System.out.println("Value of i: "+i);
                            categoryNames.add(response.getJSONArray("data").getJSONObject(i).getString("category"));
                            categoryCount.add(response.getJSONArray("data").getJSONObject(i).getJSONArray("items").length());
                            dashboardList.add(new DashboardItem(categoryNames.get(i), categoryCount.get(i)));
                            ArrayList<String> itemNames = new ArrayList<>();
                            ArrayList<Integer> itemQuantity = new ArrayList<>();
                            ArrayList<String> itemID = new ArrayList<>();
                            for (int j = 0; j < response.getJSONArray("data").getJSONObject(i).getJSONArray("items").length(); j++) {
                                itemNames.add(response.getJSONArray("data").getJSONObject(i).getJSONArray("items").getJSONObject(j).getString("name"));
                                itemQuantity.add(response.getJSONArray("data").getJSONObject(i).getJSONArray("items").getJSONObject(j).getInt("quantity"));
                                itemID.add(response.getJSONArray("data").getJSONObject(i).getJSONArray("items").getJSONObject(j).getString("_id"));
                            }
                            itemNameList.put(response.getJSONArray("data").getJSONObject(i).getString("category"), itemNames);
                            itemQuantityList.put(response.getJSONArray("data").getJSONObject(i).getString("category"), itemQuantity);
                            itemIDList.put(response.getJSONArray("data").getJSONObject(i).getString("category"), itemID);
                    }
                    categoryNameList.addAll(categoryNames);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                dashboardAdapter = new DashboardAdapter(getContext(),0,dashboardList);
                dashboardListView.setAdapter(dashboardAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), "Category Name error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // String idToken = LoginActivity.prefs.getString("idToken", "");
                headers.put("authorization", "bearer " + preferences.getString("idToken",token));//LoginActivity.prefs.getString("idToken", "0"));
                return headers;
            }
        };
        Volley.newRequestQueue(getActivity()).add(request);

        dashboardListView = rootView.findViewById(R.id.dashboardListView);
        dashboardFloatingActionButton = rootView.findViewById(R.id.dashboardFloatingActionButton);
        dashboardFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent addCategoryIntent = new Intent(getActivity().getApplicationContext(),AddCategory.class);
                //startActivity(addCategoryIntent);

                AlertDialog.Builder addCategoryAlertDialogBuilder =new  AlertDialog.Builder(getContext());
                addCategoryAlertDialogBuilder.setTitle("Add Category");

                View alertDialogLayout = getLayoutInflater().inflate(R.layout.add_category_alertdialog,null);
                addCategoryAlertDialogBuilder.setView(alertDialogLayout);

                TextInputEditText categoryNameEditText = alertDialogLayout.findViewById(R.id.categoryNameEditText);

                addCategoryAlertDialogBuilder.setCancelable(true)
                        .setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity().getBaseContext(), "Category Created", Toast.LENGTH_SHORT).show();
                                Intent addCategoryIntent = new Intent(getActivity().getApplicationContext(),AddNewItem.class);
                                addCategoryIntent.putExtra("category name",categoryNameEditText.getText().toString());
                                addCategoryIntent.putExtra("action","new");
                                startActivity(addCategoryIntent);
                            }
                        });

                AlertDialog dialog = addCategoryAlertDialogBuilder.create();
                dialog.show();
            }
        });

        dashboardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),CategoryInformation.class);
                intent.putExtra("category name",categoryNameList.get(position));
                startActivity(intent);
            }
        });

        dashboardListView.setAdapter(dashboardAdapter);

        return rootView;
    }


    public String getMessage()
    {
        String message="";
        for(int i=0;i<categoryNameList.size();i++)
        {
            message += Integer.toString(i+1)+") Category: "+categoryNameList.get(i);
            ArrayList<String> itemNames = itemNameList.get(categoryNameList.get(i));
            ArrayList<Integer> itemQuantities = itemQuantityList.get(categoryNameList.get(i));
            for(int j=0;j<itemNameList.get(categoryNameList.get(i)).size();j++)
            {
                message+="\n"+Integer.toString(j+1)+") "+itemNames.get(j)+" : "+itemQuantities.get(j);
            }
            message+="\n\n**************************\n\n";
        }
            return message;
    }


}