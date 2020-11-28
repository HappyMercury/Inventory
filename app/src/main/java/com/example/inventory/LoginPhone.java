package com.example.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

                    Intent intent = new Intent(LoginPhone.this, VerifyPhone.class);
                    intent.putExtra("mobile", "91"+mobileNo);
                    intent.putExtra("email",editTextEmail.getText().toString());
                    startActivity(intent);
                }
            });
        }
    }
}