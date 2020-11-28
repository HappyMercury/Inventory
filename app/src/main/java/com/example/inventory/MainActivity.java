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
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
    DashboardFragment dashboardFragment;
    SettingsFragment settingsFragment;
    ToDoListFragment toDoListFragment;

    String token = "";

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        token = getIntent().getStringExtra("token");

        String emailLoginEmail = getIntent().getStringExtra("emailLoginEmail");
        String emailLoginProfession = getIntent().getStringExtra("emailLoginProfession");
        String emailLoginName = getIntent().getStringExtra("emailLoginName");


        Intent intent = getIntent();
        i = intent.getIntExtra("FragmentToStart", FRAGMENT_DASHBOARD);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

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
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{});
                email.putExtra(Intent.EXTRA_SUBJECT, "From Inventory App");
                //adding message which is to be done after getting data from fragment
                String message="";
                switch(i)
                {
                    case FRAGMENT_DASHBOARD:
                        message = dashboardFragment.getMessage();
                        break;
                    case FRAGMENT_SETTINGS:
                        Fragment settingsFragment = new SettingsFragment();
                        message = "Settings";
                        break;
                    case FRAGMENT_TODOLIST:
                        message = toDoListFragment.getToDoMessage();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Uh-Oh", Toast.LENGTH_SHORT).show();
                        break;
                }
                email.putExtra(Intent.EXTRA_TEXT, message);

//need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

        public void logout () {
        preferences = getSharedPreferences("com.example.inventory",MODE_PRIVATE);//LoginActivity.prefs.edit().remove("idToken").commit();
            SharedPreferences.Editor preferencesEditor = preferences.edit();
            preferencesEditor.clear();
            preferencesEditor.commit();
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
                    dashboardFragment = new DashboardFragment();
                    Bundle b = new Bundle();
                    b.putString("token",token);
                    dashboardFragment.setArguments(b);
                    fragmentTransaction.replace(R.id.fragment_display, dashboardFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.addToBackStack("dashboard");
                    fragmentTransaction.commit();
                    break;
                case FRAGMENT_SETTINGS:
                    settingsFragment = new SettingsFragment();
                    fragmentTransaction.replace(R.id.fragment_display, settingsFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.addToBackStack("settings");
                    fragmentTransaction.commit();
                    break;
                case FRAGMENT_TODOLIST:
                    toDoListFragment = new ToDoListFragment();
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
                    dashboardFragment = new DashboardFragment();
                    Bundle b = new Bundle();
                    b.putString("token",token);
                    dashboardFragment.setArguments(b);
                    fragmentTransaction.replace(R.id.fragment_display, dashboardFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.addToBackStack("dashboard");
                    fragmentTransaction.commit();
                    break;
                case FRAGMENT_SETTINGS:
                    item.setChecked(true);
                    settingsFragment = new SettingsFragment();
                    fragmentTransaction.replace(R.id.fragment_display, settingsFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.addToBackStack("settings");
                    fragmentTransaction.commit();
                    break;
                case FRAGMENT_TODOLIST:
                    item.setChecked(true);
                    toDoListFragment = new ToDoListFragment();
                    fragmentTransaction.replace(R.id.fragment_display, toDoListFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.addToBackStack("to-do list");
                    fragmentTransaction.commit();
                    break;
                default:
                    Toast.makeText(MainActivity.this, "Uh-Oh", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }



}