package com.example.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DashboardFragment initialDashboardFragment = new DashboardFragment();
        fragmentTransaction.replace(R.id.fragment_display,initialDashboardFragment);
        fragmentTransaction.commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch(item.getItemId())
                {
                    case R.id.dashboard:
                        item.setChecked(true);
                        Toast.makeText(MainActivity.this, "Dashboard", Toast.LENGTH_SHORT).show();

                        DashboardFragment dashboardFragment = new DashboardFragment();
                        fragmentTransaction.replace(R.id.fragment_display,dashboardFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.addToBackStack("dashboard");
                        fragmentTransaction.commit();
                        break;
                    case R.id.settings:
                        item.setChecked(true);
                        Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        SettingsFragment settingsFragment = new SettingsFragment();
                        fragmentTransaction.replace(R.id.fragment_display,settingsFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.addToBackStack("settings");
                        fragmentTransaction.commit();
                        break;
                    case R.id.todolist:
                        item.setChecked(true);
                        Toast.makeText(MainActivity.this, "TO-DO List", Toast.LENGTH_SHORT).show();
                        ToDoListFragment toDoListFragment = new ToDoListFragment();
                        fragmentTransaction.replace(R.id.fragment_display,toDoListFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.addToBackStack("to-do list");
                        fragmentTransaction.commit();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Uh-Oh", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
    }
}