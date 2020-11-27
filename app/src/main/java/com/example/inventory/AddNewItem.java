package com.example.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.audiofx.DynamicsProcessing;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Config;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class AddNewItem extends AppCompatActivity {

    ImageView itemImageView;
    FloatingActionButton addItemImageFloatingActionButton;
    TextInputEditText itemNameEditText;
    ImageButton increase,decrease;
    TextView itemCount;
    Button saveButton;
    RequestQueue requestQueue;

    ArrayList<String> itemNamesList = new ArrayList<>();
    ArrayList<Integer> itemQuantityList = new ArrayList<>();
    ArrayList<String> itemIDList = new ArrayList<>();

    String updateId;
    String updateName;
    int updateQuantity;
    int position;

    String action;

    String categoryName;
    Bitmap bmp;
    String BOUNDARY = "s2retfgsGSRFsERFGHfgdfgw734yhFHW567TYHSrf4yarg"; //This the boundary which is used by the server to split the post parameters.
    String MULTIPART_FORMDATA = "multipart/form-data;boundary=" + BOUNDARY;

    private static final String TAG = MainActivity.class.getSimpleName();


    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    public static final int MEDIA_TYPE_IMAGE = 1;

    private Uri fileUri; // file url to store image/video

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        TextView amazonHeading = findViewById(R.id.amazonHeading);
        TextView amazonLink = findViewById(R.id.amazonLink);
        amazonHeading.setVisibility(View.GONE);
        amazonLink.setVisibility(View.GONE);

        SharedPreferences preferences = getSharedPreferences("com.example.inventory",MODE_PRIVATE);

        //for getting type of action to be performed
        Intent startedIntent = getIntent();
        action = startedIntent.getStringExtra("action");

        requestQueue = Volley.newRequestQueue(this);

        itemImageView = findViewById(R.id.itemImage);

        addItemImageFloatingActionButton = findViewById(R.id.itemImageFloatingActionButton);

        itemNameEditText = findViewById(R.id.itemNameTextInputEditText);

        itemCount = findViewById(R.id.count);

        increase = findViewById(R.id.increment);
        decrease = findViewById(R.id.decrement);

        addItemImageFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //image clicking
            }
        });

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(itemCount.getText().toString());
                if(count>=0)
                itemCount.setText(String.valueOf(count+1));
            }
        });

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(itemCount.getText().toString());
                if(count>0)
                itemCount.setText(String.valueOf(count-1));
            }
        });

        if(action.equals("new"))
        {
            categoryName = startedIntent.getStringExtra("category name");
            position = startedIntent.getIntExtra("position",0);
        }
        else if(action.equals("update"))
        {
            position = startedIntent.getIntExtra("position",0);
            updateId = startedIntent.getStringExtra("item id");
            updateName = startedIntent.getStringExtra("item name");
            categoryName = startedIntent.getStringExtra("category name");
            updateQuantity = startedIntent.getIntExtra("item quantity",0);
            itemNameEditText.setText(updateName);
            itemCount.setText(Integer.toString(updateQuantity));
            if(updateQuantity<=2)
            {
                amazonHeading.setVisibility(View.VISIBLE);amazonLink.setText("https://www.amazon.in/s?k="+updateName);
                amazonLink.setVisibility(View.VISIBLE);
            }
        }

        saveButton = findViewById(R.id.itemSaveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //////
                if(action.equals("new"))
                {
                    JSONObject data = new JSONObject();
                    try {
                        data.put("category", categoryName);
                        System.out.println(categoryName);
                        data.put("name", itemNameEditText.getText().toString());
                        System.out.println("Item name: " + itemNameEditText.getText().toString());
//                        itemNamesList.add(itemNameEditText.getText().toString());
                        data.put("quantity", itemCount.getText().toString());
                        System.out.println("quantity: " + itemCount.getText().toString());
//                        itemQuantityList.add(Integer.valueOf(itemCount.getText().toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiEndpoints.inventoryEndpoint, data,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(AddNewItem.this, "Item Saved", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(AddNewItem.this, "Item Not Saved", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            // String idToken = LoginActivity.prefs.getString("idToken", "");
                            headers.put("authorization", "bearer " + preferences.getString("idToken",""));//LoginActivity.prefs.getString("idToken", "0"));
                            return headers;
                        }
                    };

                    requestQueue.add(jsonObjectRequest);
                    Intent categoryInformationIntent = new Intent(AddNewItem.this, CategoryInformation.class);
                    categoryInformationIntent.putExtra("category name", categoryName);
                    startActivity(categoryInformationIntent);
                }

                else
                {
                    JSONObject data = new JSONObject();
                    try {
                        data.put("_id",updateId);
                        data.put("category", categoryName);
                        System.out.println(categoryName);
                        data.put("name",itemNameEditText.getText().toString());
                        data.put("quantity", itemCount.getText().toString());
                        System.out.println("quantity: " + itemCount.getText().toString());

//                        //removing previous items
//                        itemNamesList.remove(position);
//                        itemQuantityList.remove(position);
//                        itemIDList.remove(position);
//
//                        //adding new items
//                        itemIDList.add(position,updateId);
//                        itemNamesList.add(position,updateName);
//                        itemQuantityList.add(position,Integer.valueOf(itemCount.getText().toString()));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiEndpoints.inventoryEndpoint+"/update", data,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(AddNewItem.this, "Item Saved", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(AddNewItem.this, "Item Not Saved update", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            // String idToken = LoginActivity.prefs.getString("idToken", "");
                            headers.put("authorization", "bearer " + preferences.getString("idToken",""));//LoginActivity.prefs.getString("idToken", "0"));
                            return headers;
                        }
                    };

                    requestQueue.add(jsonObjectRequest);
                    Intent categoryInformationIntent = new Intent(getApplicationContext(), CategoryInformation.class);
                    categoryInformationIntent.putExtra("category name", categoryName);
                    startActivity(categoryInformationIntent);
                }
            }
        });
    }


//    /**
//     * Launching camera app to capture image
//     */
//    private void captureImage() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//
//        // start the image capture Intent
//        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
//    }
//
//    public Uri getOutputMediaFileUri(int type) {
//        return Uri.fromFile(getOutputMediaFile(type));
//    }
//
//    private static File getOutputMediaFile(int type) {
//
//        // External sdcard location
//        File mediaStorageDir = new File(
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "");
//
//        // Create the storage directory if it does not exist
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                Log.d(TAG, "Oops! Failed create "
//                        + "Harsh" + " directory");
//                return null;
//            }
//        }
//
//        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
//                Locale.getDefault()).format(new Date());
//        File mediaFile;
//        if (type == MEDIA_TYPE_IMAGE) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "IMG_" + timeStamp + ".jpg");
//        }else {
//            return null;
//        }
//
//        return mediaFile;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // if the result is capturing Image
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//
//                // successfully captured the image
//                // launching upload activity
//                uploadImage();
//
//            } else if (resultCode == RESULT_CANCELED) {
//
//                // user cancelled Image capture
//                Toast.makeText(getApplicationContext(),
//                        "User cancelled image capture", Toast.LENGTH_SHORT)
//                        .show();
//
//            } else {
//                // failed to capture image
//                Toast.makeText(getApplicationContext(),
//                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
//                        .show();
//            }
//        }
//    }
//
//    void uploadImage()
//    {
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiEndpoints.fileUploadEndpoint, null
//                , new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        })
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError
//            {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                // String idToken = LoginActivity.prefs.getString("idToken", "");
//                headers.put("authorization", "bearer "+LoginActivity.prefs.getString("idToken","0"));
//                return headers;
//            }
//            public String getBodyContentType() {
//                return MULTIPART_FORMDATA;
//            }
//
//            public byte[] getBody(){
//                    byte b[] = {0};
//                try {
//                    b = createPostBody(getParams()).getBytes();
//                } catch (AuthFailureError authFailureError) {
//                    authFailureError.printStackTrace();
//                }
//                return b;
//            }
//
//        };
//    }
//
//    private String createPostBody(Map<String, String> params) {
//        StringBuilder sbPost = new StringBuilder();
//        if (params != null) {
//            for (String key : params.keySet()) {
//                if (params.get(key) != null) {
//                    sbPost.append("\r\n" + "--" + BOUNDARY + "\r\n");
//                    sbPost.append("Content-Disposition: form-data; name=\"" + key + "\"" + "\r\n\r\n");
//                    sbPost.append(params.get(key).toString());
//                }
//            }
//        }
//        return sbPost.toString();
//    }

}