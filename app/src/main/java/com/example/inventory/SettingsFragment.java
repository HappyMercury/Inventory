package com.example.inventory;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.textfield.TextInputEditText;

public class SettingsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
    }


    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Uri profileImageURL = LoginActivity.signInAccount.getPhotoUrl();
        //Log.i("url",profileImageURL.toString());
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        TextInputEditText name = rootView.findViewById(R.id.nameTextInputEditText);
        TextInputEditText email = rootView.findViewById(R.id.emailTextInputEditText);
        name.setText(LoginActivity.googleName);
        email.setText(LoginActivity.googleEmail);
        ImageView profileImageView = rootView.findViewById(R.id.profileImageView);
        try {
            Glide.with(this)
                    .load(LoginActivity.googlePhotoURL)
                    .circleCrop()
                    .into(profileImageView);
        }
        catch(Exception e)
        {
            Log.i("Error: ",e.toString());
        }

        return rootView;
    }
}