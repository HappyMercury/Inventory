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
import android.widget.ToggleButton;

import com.google.android.material.textfield.TextInputEditText;

public class NewToDoListItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_to_do_list_item);

        CardView cardView = findViewById(R.id.cardView);
        TextInputEditText toDoListTextInput = findViewById(R.id.toDoListTitleTextInput);
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
                    timePicker.setVisibility(View.GONE);
                    datePicker.setVisibility(View.GONE);
                }
                else
                {
                    timePicker.setVisibility(View.VISIBLE);
                    datePicker.setVisibility(View.VISIBLE);
                }
            }
        });

        Button saveButton = findViewById(R.id.saveToDoListElement);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("FragmentToStart",MainActivity.FRAGMENT_TODOLIST);
                startActivity(intent);
            }
        });

    }
}