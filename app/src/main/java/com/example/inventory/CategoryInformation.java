package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CategoryInformation extends AppCompatActivity {

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_information);

        ArrayList<String> itemArrayList = new ArrayList<String>();
        itemArrayList.add("Harsh");
        itemArrayList.add("Harsh");
        itemArrayList.add("Harsh");
        itemArrayList.add("Harsh");

        ArrayAdapter<String> itemArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,itemArrayList);

        ListView itemListView = findViewById(R.id.itemListView);
        itemListView.setAdapter(itemArrayAdapter);
        FloatingActionButton newItemFAB = findViewById(R.id.addItemFloatingActionButton);
        ImageView emptyImageView = findViewById(R.id.emptyImageView);

        emptyImageView.setVisibility(View.GONE);

       if(itemArrayList.size()<0)
        {
            itemListView.setVisibility(View.GONE);
            emptyImageView.setVisibility(View.VISIBLE);
            emptyImageView.setImageResource(R.drawable.ic_baseline_empty);
        }
        else
        {
            itemListView.setVisibility(View.VISIBLE);
            emptyImageView.setVisibility(View.GONE);
        }
        newItemFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CategoryInformation.this, "Creating new item", Toast.LENGTH_SHORT).show();
            }
        });

    }
}