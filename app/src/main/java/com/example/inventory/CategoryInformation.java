package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.OkHttpClient;
import pl.droidsonroids.gif.GifImageView;

public class CategoryInformation extends AppCompatActivity {

    RequestQueue requestQueue;
    String categoryName;
    ArrayList<String> itemNamesList = new ArrayList<>();
    ArrayList<Integer> itemQuantityList = new ArrayList<>();
    ArrayList<String> itemIdList = new ArrayList<>();
    Intent intent;

    SharedPreferences preferences;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_information);

        GifImageView emptyImageView = findViewById(R.id.emptyImageView);
        emptyImageView.setVisibility(View.GONE);

        ArrayList<ItemListElement> itemArrayList = new ArrayList<ItemListElement>();
        ItemListElementAdapter itemListElementAdapter = new ItemListElementAdapter(CategoryInformation.this,0,itemArrayList);

        ListView itemListView = findViewById(R.id.itemListView);
        itemListView.setAdapter(itemListElementAdapter);

        intent = getIntent();
        String categoryName = intent.getStringExtra("category name");

        //for getting information about the category
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiEndpoints.inventoryEndpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    for(int i=0;i<response.getJSONArray("data").length();i++) {
                        System.out.println("Value of i: " + i);
                        if (response.getJSONArray("data").getJSONObject(i).getString("category").equals(categoryName)) {
                            for (int j = 0; j < response.getJSONArray("data").getJSONObject(i).getJSONArray("items").length(); j++) {
                                itemNamesList.add(response.getJSONArray("data").getJSONObject(i).getJSONArray("items").getJSONObject(j).getString("name"));
                                itemQuantityList.add(response.getJSONArray("data").getJSONObject(i).getJSONArray("items").getJSONObject(j).getInt("quantity"));
                                itemIdList.add(response.getJSONArray("data").getJSONObject(i).getJSONArray("items").getJSONObject(j).getString("_id"));
                            }
                        }
                    }

                    System.out.println("Size hai bhiay size: "+itemNamesList.size());

                    for(int i=0;i<itemNamesList.size();i++)
                    {
                        itemArrayList.add(new ItemListElement(itemNamesList.get(i),itemQuantityList.get(i)<=2?true:false));
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                itemListElementAdapter.notifyDataSetChanged();
                itemListView.setAdapter(itemListElementAdapter);
                System.out.println("Size hai bhiay size: "+itemArrayList.size());
                if(itemArrayList.size()>0)
                {
                    itemListView.setVisibility(View.VISIBLE);
                    emptyImageView.setVisibility(View.GONE);
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CategoryInformation.this, "Category Name error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // String idToken = LoginActivity.prefs.getString("idToken", "");
                headers.put("authorization", "bearer " + preferences.getString("idToken",""));//LoginActivity.prefs.getString("idToken", "0"));
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(request);

        //when started from category name to get category name info

        System.out.println(itemNamesList.size());


        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),AddNewItem.class);
                intent.putExtra("action","update");
                intent.putExtra("item name",itemNamesList.get(position));
                intent.putExtra("item id",itemIdList.get(position));
                intent.putExtra("item quantity",itemQuantityList.get(position));
                intent.putExtra("category name",categoryName);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });


        itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryInformation.this)
                        .setTitle("Are you sure you want to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletionRequest(itemIdList.get(position));
                                itemArrayList.remove(position);
                                itemListElementAdapter.notifyDataSetChanged();
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

        FloatingActionButton newItemFAB = findViewById(R.id.addItemFloatingActionButton);

       if(itemArrayList.size()<=0)
        {
            itemListView.setVisibility(View.GONE);
            emptyImageView.setVisibility(View.VISIBLE);

        }
        else
        {
            itemListView.setVisibility(View.VISIBLE);
            emptyImageView.setVisibility(View.GONE);
        }
        newItemFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddNewItem.class);
                intent.putExtra("action","new");
                intent.putExtra("category name",categoryName);
                startActivity(intent);
            }
        });

    }

    void deletionRequest(String id)
    {
        JSONObject data = new JSONObject();
        try {
            data.put("_id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiEndpoints.inventoryEndpoint+"/delete", data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(CategoryInformation.this, "Item deleted", Toast.LENGTH_SHORT).show();
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
                headers.put("authorization", "bearer "+preferences.getString("idToken",""));//LoginActivity.prefs.getString("idToken","0"));
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

}