package com.example.inventory;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView dashboardListView;
    FloatingActionButton dashboardFloatingActionButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<DashboardItem> dashboardList = new ArrayList<>();

    public DashboardFragment() {
        dashboardList.add(new DashboardItem("Groceries",R.drawable.ic_baseline_trial));
        dashboardList.add(new DashboardItem("Home Appliances",R.drawable.ic_baseline_trial));
        dashboardList.add(new DashboardItem("Kitchen",R.drawable.ic_baseline_trial));
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        DashboardAdapter dashboardAdapter = new DashboardAdapter(getActivity(),0,dashboardList);
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        dashboardListView = rootView.findViewById(R.id.dashboardListView);
        dashboardFloatingActionButton = rootView.findViewById(R.id.dashboardFloatingActionButton);
        dashboardFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addCategoryIntent = new Intent(getActivity().getApplicationContext(),AddCategory.class);
                startActivity(addCategoryIntent);
            }
        });
        dashboardListView.setAdapter(dashboardAdapter);
        return rootView;
    }
}