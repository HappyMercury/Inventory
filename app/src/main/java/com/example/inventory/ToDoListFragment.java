package com.example.inventory;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToDoListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToDoListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<ToDoListElement> toDoListElementArrayList = new ArrayList<>();

    public ToDoListFragment() {

        toDoListElementArrayList.add(new ToDoListElement("Get Groceries",R.color.main_green_color,true));
        toDoListElementArrayList.add(new ToDoListElement("Get Groceries",R.color.blue_btn_bg_color,true));
        toDoListElementArrayList.add(new ToDoListElement("Get Groceries",R.color.red_btn_bg_color,false));
        toDoListElementArrayList.add(new ToDoListElement("Get Groceries",R.color.main_green_color,false));
        toDoListElementArrayList.add(new ToDoListElement("Get Groceries",R.color.main_green_color,true));
        toDoListElementArrayList.add(new ToDoListElement("Get Groceries",R.color.main_green_color,false));
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToDoListFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        ListView toDoListListView = rootView.findViewById(R.id.toDoListTextView);


        ToDoListAdapter toDoListArrayAdapter = new ToDoListAdapter(getContext(),0,toDoListElementArrayList);
        toDoListListView.setAdapter(toDoListArrayAdapter);

        return rootView;
    }
}