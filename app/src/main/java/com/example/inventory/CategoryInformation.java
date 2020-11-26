package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.Map;

import okhttp3.OkHttpClient;
import pl.droidsonroids.gif.GifImageView;

public class CategoryInformation extends AppCompatActivity {

    RequestQueue requestQueue;
    String categoryName;
    ArrayList<String> itemNamesList;
    ArrayList<Integer> itemQuantity;
    ArrayList<String> itemId;
    Intent intent;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_information);

        requestQueue = Volley.newRequestQueue(this);
        //when started from category name to get category name info

        intent = getIntent();
        String categoryName = intent.getStringExtra("category name");
        ArrayList<String> itemNamesList = intent.getStringArrayListExtra("item name list added/updated");
        ArrayList<Integer> itemQuantity = intent.getIntegerArrayListExtra("item quantity list added/updated");
        ArrayList<String> itemId = intent.getStringArrayListExtra("item id list added/updated");

        System.out.println(itemNamesList.size());

        ArrayList<ItemListElement> itemArrayList = new ArrayList<ItemListElement>();
//        itemArrayList.add(new ItemListElement("Papita",true));
//        itemArrayList.add(new ItemListElement("Banana",false));
        for(int i=0;i<itemNamesList.size();i++)
        {
            itemArrayList.add(new ItemListElement(itemNamesList.get(i),itemQuantity.get(i)<=2?true:false));
        }

        ItemListElementAdapter itemListElementAdapter = new ItemListElementAdapter(this,0,itemArrayList);

        ListView itemListView = findViewById(R.id.itemListView);

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),AddNewItem.class);
                intent.putExtra("action","update");
//                intent.putExtra("item name",itemNamesList.get(position));
//                intent.putExtra("item id",itemId.get(position));
//                intent.putExtra("item quantity",itemQuantity.get(position));
                intent.putExtra("category name",categoryName);
                intent.putStringArrayListExtra("item name list",itemNamesList);
                intent.putIntegerArrayListExtra("item quantity list",itemQuantity);
                intent.putStringArrayListExtra("item id list",itemId);
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
                                deletionRequest(itemId.get(position));
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

        itemListView.setAdapter(itemListElementAdapter);

        FloatingActionButton newItemFAB = findViewById(R.id.addItemFloatingActionButton);
        GifImageView emptyImageView = findViewById(R.id.emptyImageView);
        emptyImageView.setVisibility(View.GONE);

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
                intent.putStringArrayListExtra("item names",itemNamesList);
                intent.putIntegerArrayListExtra("item quantity",itemQuantity);
                intent.putStringArrayListExtra("item name list",itemNamesList);
                intent.putIntegerArrayListExtra("item quantity list",itemQuantity);
                intent.putStringArrayListExtra("item id list",itemId);
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
                headers.put("authorization", "bearer "+LoginActivity.prefs.getString("idToken","0"));
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        intent = getIntent();
        categoryName = intent.getStringExtra("category name");
        itemNamesList = intent.getStringArrayListExtra("item name list added/updated");
        itemQuantity = intent.getIntegerArrayListExtra("item quantity list added/updated");
        itemId = intent.getStringArrayListExtra("item id list added/updated");
    }
}