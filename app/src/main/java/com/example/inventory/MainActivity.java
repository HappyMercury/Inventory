package com.example.inventory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //List of fragments
    public static final int FRAGMENT_DASHBOARD = 1;
    public static final int FRAGMENT_TODOLIST = 2;
    public static final int FRAGMENT_SETTINGS = 3;
    int i = 0;
    int itemID = 0;
    public static HashMap<String,ArrayList<String>> itemNameMap;
    public static HashMap<String,ArrayList<Integer>> itemQuantityMap;
    public static HashMap<String,ArrayList<String>> itemIDMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Intent intent = getIntent();
        i = intent.getIntExtra("FragmentToStart", FRAGMENT_DASHBOARD);
        //getCategoryInfo();
        showFragment(i);
        switch(i)
        {
            case FRAGMENT_DASHBOARD:
                bottomNavigationView.setSelectedItemId(R.id.dashboard);
                break;
            case FRAGMENT_SETTINGS:
                bottomNavigationView.setSelectedItemId(R.id.settings);
                break;
            case FRAGMENT_TODOLIST:
                bottomNavigationView.setSelectedItemId(R.id.todolist);
                break;
            default:
                Toast.makeText(MainActivity.this, "Uh-Oh", Toast.LENGTH_SHORT).show();
                break;
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                showFragment(i,item);
                return false;
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.appbar_menu, menu);

        if (menu instanceof MenuBuilder) {

            MenuBuilder menuBuilder = (MenuBuilder) menu;
            menuBuilder.setOptionalIconsVisible(true);
            menuBuilder.setGroupDividerEnabled(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item){

        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                break;
            case R.id.help:
                //help
                break;
            case R.id.share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

        public void logout () {
        LoginActivity.prefs.edit().remove("idToken").commit();
            FirebaseAuth.getInstance().signOut();
            GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                    .signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Signout Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }



        public void showFragment(int fragmentID)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            switch (i) {
                case FRAGMENT_DASHBOARD:
                    DashboardFragment dashboardFragment = new DashboardFragment();
                    fragmentTransaction.replace(R.id.fragment_display, dashboardFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.addToBackStack("dashboard");
                    fragmentTransaction.commit();
                    break;
                case FRAGMENT_SETTINGS:
                    SettingsFragment settingsFragment = new SettingsFragment();
                    fragmentTransaction.replace(R.id.fragment_display, settingsFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.addToBackStack("settings");
                    fragmentTransaction.commit();
                    break;
                case FRAGMENT_TODOLIST:
                    ToDoListFragment toDoListFragment = new ToDoListFragment();
                    fragmentTransaction.replace(R.id.fragment_display, toDoListFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.addToBackStack("to-do list");
                    fragmentTransaction.commit();
                    break;
                default:
                    Toast.makeText(MainActivity.this, "Uh-Oh", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        public void showFragment(int fragment,MenuItem item)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (item.getItemId() == R.id.dashboard) {
                i = FRAGMENT_DASHBOARD;
            } else if (item.getItemId() == R.id.todolist) {
                i = FRAGMENT_TODOLIST;
            } else if (item.getItemId() == R.id.settings) {
                i = FRAGMENT_SETTINGS;
            }

            switch (i) {
                case FRAGMENT_DASHBOARD:
                    item.setChecked(true);
                    DashboardFragment dashboardFragment = new DashboardFragment();
                    fragmentTransaction.replace(R.id.fragment_display, dashboardFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.addToBackStack("dashboard");
                    fragmentTransaction.commit();
                    break;
                case FRAGMENT_SETTINGS:
                    item.setChecked(true);
                    SettingsFragment settingsFragment = new SettingsFragment();
                    fragmentTransaction.replace(R.id.fragment_display, settingsFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.addToBackStack("settings");
                    fragmentTransaction.commit();
                    break;
                case FRAGMENT_TODOLIST:
                    item.setChecked(true);
                    ToDoListFragment toDoListFragment = new ToDoListFragment();
                    fragmentTransaction.replace(R.id.fragment_display, toDoListFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.addToBackStack("to-do list");
                    fragmentTransaction.commit();
                    break;
                default:
                    Toast.makeText(MainActivity.this, "Uh-Oh", Toast.LENGTH_SHORT).show();
                    break;
            }
        }


}