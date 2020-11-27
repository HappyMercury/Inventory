package com.example.inventory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    public static final int GOOGLE_SIGN_IN_CODE = 42069;
    public static String googleEmail,googleName;
    public static Uri googlePhotoURL;
    public static SharedPreferences prefs;

    SignInButton signIn;
    EditText email;
    EditText password;
    Button Loginbtn;
    Button Signupbutton;
    Button PhoneLoginButton;
    CheckBox showPwdLogin;
    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;
    FirebaseAuth firebaseAuth;
    static String idToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        signIn = findViewById(R.id.signIn);
        Loginbtn = findViewById(R.id.buttonLogin);
        Signupbutton = findViewById(R.id.buttonSignUp);
        PhoneLoginButton = findViewById(R.id.PhoneLogin);
        email = findViewById(R.id.editTxtEmail);
        password = findViewById(R.id.editTxtPassword);
        showPwdLogin = findViewById(R.id.showPwdLogin);
        prefs = this.getSharedPreferences("com.example.nec.myapplication", Context.MODE_PRIVATE);

        MyFirebaseMessagingService.getToken(this);

        firebaseAuth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("922890224357-alaqa3sorbb0ck649g510u9fp7umnjoe.apps.googleusercontent.com")
                .requestEmail()
                .build();
        //922890224357-alaqa3sorbb0ck649g510u9fp7umnjoe.apps.googleusercontent.com
        //698187653861-g54mfttbqmvhbspc2742uum5fhjcitif.apps.googleusercontent.com

        signInClient = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(signInAccount != null ||( firebaseAuth.getCurrentUser() != null&&firebaseUser.isEmailVerified())){
            googleName = firebaseUser.getDisplayName();
            googleEmail = firebaseUser.getEmail();
            googlePhotoURL = firebaseUser.getPhotoUrl();
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            mUser.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                idToken = task.getResult().getToken();
                                System.out.println("Token: "+idToken);
                                prefs.edit().putString("idToken", idToken).apply();
                                // Send token to your backend via HTTPS
                                // ...
                            } else {
                                // Handle error -> task.getException();
                            }
                        }
                    });
            Toast.makeText(this, "User is already logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,MainActivity.class);

            intent.putExtra("FragmentToStart",MainActivity.FRAGMENT_DASHBOARD);
            startActivity(intent);
        }

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign = signInClient.getSignInIntent();
                startActivityForResult(sign, GOOGLE_SIGN_IN_CODE);

            }
        });



        showPwdLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is checked.
                if (isChecked) {
                    //password is visible
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //password gets hided
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        Signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                //finish();
            }
        });
        PhoneLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,LoginPhone.class));
            }
        });

        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailval = email.getText().toString();
                String passwordval = password.getText().toString();

                firebaseAuth.signInWithEmailAndPassword(emailval, passwordval).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if(firebaseAuth.getCurrentUser().isEmailVerified()){

                                        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                                        mUser.getIdToken(true)
                                                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                                                        if (task.isSuccessful()) {
                                                            idToken = task.getResult().getToken();
                                                            System.out.println("Token: "+idToken);
                                                            prefs.edit().putString("idToken", idToken).apply();
                                                            // Send token to your backend via HTTPS
                                                            // ...
                                                        } else {
                                                            // Handle error -> task.getException();
                                                        }
                                                    }
                                                });

                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(LoginActivity.this, "Please verify email address", Toast.LENGTH_SHORT).show();
                                    }
//                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GOOGLE_SIGN_IN_CODE){
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount signInAcc = signInTask.getResult(ApiException.class);
                AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAcc.getIdToken(),null);

                firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(getApplicationContext(), "Your account is now connected", Toast.LENGTH_SHORT).show();
                        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                        mUser.getIdToken(true)
                                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                                        if (task.isSuccessful()) {
                                            idToken = task.getResult().getToken();
                                            System.out.println("Token: "+idToken);
                                            prefs.edit().putString("idToken", idToken).apply();
                                            // Send token to your backend via HTTPS
                                            // ...
                                        } else {
                                            // Handle error -> task.getException();
                                        }
                                    }
                                });
                        googleName = mUser.getDisplayName();
                        googleEmail = mUser.getEmail();
                        googlePhotoURL = mUser.getPhotoUrl();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
}