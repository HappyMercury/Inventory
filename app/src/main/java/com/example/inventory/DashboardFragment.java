package com.example.inventory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView dashboardListView;
    FloatingActionButton dashboardFloatingActionButton;

    private String mParam1;
    private String mParam2;

    ArrayList<DashboardItem> dashboardList = new ArrayList<>();

    public DashboardFragment() {
        dashboardList.add(new DashboardItem("Groceries",5));
        dashboardList.add(new DashboardItem("Home Appliances",6));
        dashboardList.add(new DashboardItem("Kitchen",7));
        // Required empty public constructor
    }

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
                //Intent addCategoryIntent = new Intent(getActivity().getApplicationContext(),AddCategory.class);
                //startActivity(addCategoryIntent);

                AlertDialog.Builder addCategoryAlertDialogBuilder =new  AlertDialog.Builder(getContext());
                addCategoryAlertDialogBuilder.setTitle("Add Category");

                View alertDialogLayout = getLayoutInflater().inflate(R.layout.add_category_alertdialog,null);
                addCategoryAlertDialogBuilder.setView(alertDialogLayout);

                addCategoryAlertDialogBuilder.setCancelable(true)
                        .setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity().getBaseContext(), "Category Created", Toast.LENGTH_SHORT).show();
                                Intent addCategoryIntent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                                startActivity(addCategoryIntent);
                            }
                        });

                AlertDialog dialog = addCategoryAlertDialogBuilder.create();
                dialog.show();
            }
        });
        dashboardListView.setAdapter(dashboardAdapter);
        dashboardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),CategoryInformation.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}