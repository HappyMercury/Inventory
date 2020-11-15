package com.example.inventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

class ToDoListAdapter extends ArrayAdapter<ToDoListElement> {

    public ToDoListAdapter(@NonNull Context context, int resource, ArrayList<ToDoListElement> toDoListElementArrayList) {
        super(context, resource,toDoListElementArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View mToDoListItem = convertView;
        ToDoListElement currentItem = getItem(position);
        if(mToDoListItem==null)
        {
            mToDoListItem = LayoutInflater.from(getContext()).inflate(R.layout.dashboard_item,parent,false);
        }

        ImageView toDoListImageView = mToDoListItem.findViewById(R.id.toDoListImageView);
        toDoListImageView.setBackgroundColor(currentItem.getToDoListImageView());

        TextView toDoListTextView = mToDoListItem.findViewById(R.id.toDoListTextView);
        toDoListTextView.setText(currentItem.getToDoListTextView());

        CheckBox toDoListCheckBox = mToDoListItem.findViewById(R.id.toDoListCheckBox);

        ImageView reminderImageView = mToDoListItem.findViewById(R.id.reminderImageView);
        if(currentItem.getReminderImageView())
        {
            reminderImageView.setVisibility(View.VISIBLE);
        }

        return mToDoListItem;
    }
}
