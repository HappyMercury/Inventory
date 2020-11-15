package com.example.inventory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;


public class ToDoListFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ArrayList<ToDoListElement> toDoListElementArrayList = new ArrayList<>();

    public ToDoListFragment() {

        toDoListElementArrayList.add(new ToDoListElement("Get Groceries",true));
        toDoListElementArrayList.add(new ToDoListElement("Get Groceries",false));
        toDoListElementArrayList.add(new ToDoListElement("Get Groceries",false));
        toDoListElementArrayList.add(new ToDoListElement("Get Groceries",true));
        toDoListElementArrayList.add(new ToDoListElement("Get Groceries",false));
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_to_do_list, container, false);

        ListView toDoListListView = rootView.findViewById(R.id.toDoListListView);

        FloatingActionButton toDoListFloatingActionButton = rootView.findViewById(R.id.toDoListFloatingActionButton);
        toDoListFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),NewToDoListItem.class);
                startActivity(intent);
            }
        });

        ToDoListAdapter toDoListArrayAdapter = new ToDoListAdapter(getActivity(),0,toDoListElementArrayList);
        toDoListListView.setAdapter(toDoListArrayAdapter);
        toDoListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return rootView;
    }



}