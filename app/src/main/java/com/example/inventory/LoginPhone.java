package com.example.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPhone extends AppCompatActivity {
    private EditText editTextMobile;
    Button btnContinue;
    FirebaseUser currentUser;
    private EditText editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);

        editTextMobile = findViewById(R.id.editTextMobile);
        btnContinue = findViewById(R.id.buttonContinue);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        editTextEmail = findViewById(R.id.editTextPhoneLoginEmail);

        //check whether the user is logged in
        if (currentUser != null) {
            Intent intent = new Intent(LoginPhone.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            btnContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String mobileNo = editTextMobile.getText().toString().trim();

                    if (mobileNo.isEmpty() || mobileNo.length() < 10) {
                        editTextMobile.setError("Enter a valid mobile");
                        editTextMobile.requestFocus();
                        return;
                    }
                    Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
                    Matcher emailmatcher = emailPattern.matcher(editTextEmail.getText().toString());
                    boolean b = emailmatcher.matches();
                    if(b == false)
                    {
                        Toast.makeText(LoginPhone.this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent = new Intent(LoginPhone.this, VerifyPhone.class);
                        intent.putExtra("mobile", "91" + mobileNo);
                        intent.putExtra("email", editTextEmail.getText().toString());
                        startActivity(intent);
                    }
                }
            });
        }
    }
}