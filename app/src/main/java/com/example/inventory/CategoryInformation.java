package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class CategoryInformation extends AppCompatActivity {

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_information);

        ArrayList<ItemListElement> itemArrayList = new ArrayList<ItemListElement>();
        itemArrayList.add(new ItemListElement("Papita",true));
        itemArrayList.add(new ItemListElement("Banana",false));

        ItemListElementAdapter itemListElementAdapter = new ItemListElementAdapter(this,0,itemArrayList);

        ListView itemListView = findViewById(R.id.itemListView);
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
                startActivity(intent);
            }
        });

    }
}