package com.example.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

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

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                .signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(view.getContext(),LoginActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Signout Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}