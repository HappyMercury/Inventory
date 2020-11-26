package com.example.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText emailtxt;
    EditText pwd;
    EditText repwd;
    Button signup;
    FirebaseAuth firebaseAuth;
    CheckBox showbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailtxt = findViewById(R.id.editTextEmail);
        pwd = findViewById(R.id.editTextPwd);
        repwd = findViewById(R.id.editTxtConfirmPwd);
        signup = findViewById(R.id.SignUpBtn);
        showbtn = findViewById(R.id.showPwd);
        firebaseAuth = FirebaseAuth.getInstance();


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

                if(!password.equals(repassword)){
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
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