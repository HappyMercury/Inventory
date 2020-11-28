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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    public static final int GOOGLE_SIGN_IN_CODE = 42069;
    public static String googleEmail,googleName;
    public static Uri googlePhotoURL;
    private SharedPreferences prefs;

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
    String idToken;

    void sendfcm(){
        String FCMTKN = MyFirebaseMessagingService.getToken(this);
        System.out.println("FCM FROM MAIN: " + FCMTKN);

        JSONObject data = new JSONObject();
        try {
            data.put("notif_token",FCMTKN);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiEndpoints.notificationEndpoint,data,
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
                //String idToken =
                headers.put("authorization", "bearer "+ idToken);//LoginActivity.prefs.getString("idToken","0"));
                return headers;
            }
        };

        Volley.newRequestQueue(LoginActivity.this).add(jsonObjectRequest);
    }

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
        prefs = getSharedPreferences("com.example.inventory", Context.MODE_PRIVATE);

        Intent emailIntent = getIntent();
        String emailLoginEmail = emailIntent.getStringExtra("emailLoginEmail");
        String emailLoginProfession = emailIntent.getStringExtra("emailLoginProfession");
        String emailLoginName = emailIntent.getStringExtra("emailLoginName");

        firebaseAuth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("922890224357-alaqa3sorbb0ck649g510u9fp7umnjoe.apps.googleusercontent.com")
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(signInAccount!=null)
        {
            googleName = firebaseUser.getDisplayName();
            googleEmail = firebaseUser.getEmail();
            googlePhotoURL = firebaseUser.getPhotoUrl();

            SharedPreferences.Editor preferencesEditor = prefs.edit();
            preferencesEditor.clear();
            preferencesEditor.commit();
//            LoginActivity.prefs.edit().remove("idToken").commit();
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            mUser.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                idToken = task.getResult().getToken();
                                System.out.println("Token: "+idToken);
                                sendfcm();
                                preferencesEditor.putString("idToken", idToken).commit();
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
            intent.putExtra("token",idToken);
            startActivity(intent);

        }

        if(( firebaseAuth.getCurrentUser() != null&&firebaseUser.isEmailVerified())){
//            googleName = firebaseUser.getDisplayName();
//            googleEmail = firebaseUser.getEmail();
//            googlePhotoURL = firebaseUser.getPhotoUrl();

            googleName = "";
            googleEmail="";
            googlePhotoURL=null;



            SharedPreferences.Editor preferencesEditor = prefs.edit();
            preferencesEditor.clear();
            preferencesEditor.commit();
//            LoginActivity.prefs.edit().remove("idToken").commit();
            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            mUser.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                idToken = task.getResult().getToken();
                                System.out.println("Token: "+idToken);
                                sendfcm();
                                preferencesEditor.putString("idToken", idToken).commit();
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
            intent.putExtra("token",idToken);
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

                Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
                Matcher emailmatcher = emailPattern.matcher(emailval);
                boolean b = emailmatcher.matches();

                if(b== false)
                {
                    Toast.makeText(LoginActivity.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseAuth.signInWithEmailAndPassword(emailval, passwordval).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {

                                            SharedPreferences.Editor preferencesEditor = prefs.edit();
                                            preferencesEditor.clear();
                                            preferencesEditor.commit();
//            LoginActivity.prefs.edit().remove("idToken").commit();
                                            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                                            mUser.getIdToken(true)
                                                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                                                            if (task.isSuccessful()) {
                                                                idToken = task.getResult().getToken();
                                                                System.out.println("Token: "+idToken);
                                                                sendfcm();
                                                                preferencesEditor.putString("idToken", idToken).commit();
                                                                // Send token to your backend via HTTPS
                                                                // ...
                                                            } else {
                                                                // Handle error -> task.getException();
                                                            }
                                                        }
                                                    });


                                            ///ending the last comment

                                            Intent intent= new Intent(LoginActivity.this,MainActivity.class);
                                            intent.putExtra("emailLoginEmail",emailLoginEmail);
                                            intent.putExtra("emailLoginName",emailLoginName);
                                            intent.putExtra("emailLoginProfession",emailLoginProfession);
                                            intent.putExtra("token",idToken);
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Please verify email address", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );
                }
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

                        SharedPreferences.Editor preferencesEditor = prefs.edit();
                        preferencesEditor.clear();
                        preferencesEditor.commit();
//            LoginActivity.prefs.edit().remove("idToken").commit();
                        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                        mUser.getIdToken(true)
                                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                                        if (task.isSuccessful()) {
                                            idToken = task.getResult().getToken();
                                            System.out.println("Token: "+idToken);
                                            sendfcm();
                                            preferencesEditor.putString("idToken", idToken).commit();
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

                        boolean isnew = task.getResult().getAdditionalUserInfo().isNewUser();
                        if(isnew == true)
                        {
                            System.out.println("NEW USER");
                            startActivity(new Intent(LoginActivity.this, UserDetailsSelect.class));
                            finish();
                        }
                        else
                        {
                            System.out.println("OLD USER");
                           Intent intent =  new Intent(getApplicationContext(),MainActivity.class).putExtra("token",idToken);
                            startActivity(intent.putExtra("fragment to start",MainActivity.FRAGMENT_DASHBOARD));
                        }


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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}