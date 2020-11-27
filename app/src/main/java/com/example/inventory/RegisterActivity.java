package com.example.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText emailtxt;
    EditText pwd;
    EditText repwd;
    EditText Name;
    EditText UsernameEmail;
    Button signup;
    FirebaseAuth firebaseAuth;
    CheckBox showbtn;
    String ProfessionEmail;

    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailtxt = findViewById(R.id.editTextEmail);
        pwd = findViewById(R.id.editTextPwd);
        repwd = findViewById(R.id.editTxtConfirmPwd);
        signup = findViewById(R.id.SignUpBtn);
        showbtn = findViewById(R.id.showPwd);
        Name = findViewById(R.id.editTxtName);
        UsernameEmail = findViewById(R.id.editTxtName);
        firebaseAuth = FirebaseAuth.getInstance();

        preferences = getSharedPreferences("com.example.inventory",MODE_PRIVATE);
        Spinner professionSpinner = findViewById(R.id.ProfessionSelectEmail);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("WORKING");
        arrayList.add("HOME");
        arrayList.add("JOB SEEKER");
        arrayList.add("BACHELOR");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        professionSpinner.setAdapter(arrayAdapter);

        showbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is checked.
                if (isChecked) {
                    //password is visible
                    pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    repwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //password gets hided
                    pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    repwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = pwd.getText().toString();
                String repassword = repwd.getText().toString();
                String emailID = emailtxt.getText().toString();
                String UsersName = Name.getText().toString();

                switch (professionSpinner.getSelectedItemPosition())
                {
                    case 0:
                        ProfessionEmail = "working";
                        break;
                    case 1:
                        ProfessionEmail = "home";
                        break;
                    case 2:
                        ProfessionEmail = "job_seekers";
                        break;
                    case 3:
                        ProfessionEmail = "bachelors";
                        break;


                }

                Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
                Matcher emailmatcher = emailPattern.matcher(emailID);
                boolean b = emailmatcher.matches();

                if(!password.equals(repassword)){
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
                else if(b == false)
                {
                    Toast.makeText(RegisterActivity.this, "Enter a vaild email address", Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.createUserWithEmailAndPassword(emailID, password).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {


                                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){

                                                    JSONObject data = new JSONObject();
                                                    try {
                                                        data.put("profession",ProfessionEmail);
                                                        data.put("name",UsersName);
                                                    }
                                                    catch (Exception e){
                                                        e.printStackTrace();
                                                    }

                                                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiEndpoints.loginEndpoint,data,
                                                            new Response.Listener<JSONObject>()
                                                            {
                                                                @Override
                                                                public void onResponse(JSONObject responseObject) {

                                                                }
                                                            }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            //Toast.makeText(getActivity(), "Item not saved", Toast.LENGTH_SHORT).show();
                                                            error.printStackTrace();
                                                        }
                                                    }){
                                                        @Override
                                                        public Map<String, String> getHeaders() throws AuthFailureError
                                                        {
                                                            HashMap<String, String> headers = new HashMap<String, String>();
                                                            // String idToken = LoginActivity.prefs.getString("idToken", "");
                                                            headers.put("authorization", "bearer "+preferences.getString("idToken",""));//LoginActivity.prefs.getString("idToken","0"));
                                                            return headers;
                                                        }
                                                    };

                                                    Volley.newRequestQueue(RegisterActivity.this).add(jsonObjectRequest);

                                                    Toast.makeText(RegisterActivity.this, "SignUp Complete, please verify your email", Toast.LENGTH_SHORT)
                                                            .show();
                                                    //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                }
                                                else
                                                {
                                                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }


                                                finish();

                                            }
                                        });


                                    } else {
                                        Toast.makeText(RegisterActivity.this, "SignUp failed, please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );
                }
            }
        });

    }
}