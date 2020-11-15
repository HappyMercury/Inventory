package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddCategory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        ImageView categoryImageView = findViewById(R.id.categoryImageView);
        TextInputLayout categoryNameTextInputLayout = findViewById(R.id.categoryActivityTextInputLayout);
        TextInputEditText categoryNameEditText = findViewById(R.id.categoryNameEditText);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            categoryNameEditText.setLineHeight(1);
        }*/
        categoryNameEditText.setSingleLine(true);

        FloatingActionButton addCategoryFloatingActionButton = findViewById(R.id.addCategoryFloatingActionButton);
        addCategoryFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddCategory.this, "New Category: "+categoryNameEditText.getText().toString()+" added", Toast.LENGTH_SHORT).show();
                //starting mainActivity after adding new category
                Intent addCategoryIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(addCategoryIntent);
            }
        });
    }
}