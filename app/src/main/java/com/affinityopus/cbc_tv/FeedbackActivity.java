package com.affinityopus.cbc_tv;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;


import static android.Manifest.permission.READ_PHONE_STATE;


public class FeedbackActivity extends BaseActivity {
    ProgressDialog progressDialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

AppCompatButton appCompatButton;
    TextInputEditText textInputEditText,messageInput,emailAddress;
    RequestQueue requestQueue;
    String mobileNumber,messageContent,email;

    TextInputLayout textInputLayout1,email_id,messageLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_feedback, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        textInputLayout1 = (TextInputLayout) findViewById(R.id.mobileNumber);
        textInputLayout1.getBackground().setAlpha(75);

        email_id =(TextInputLayout) findViewById(R.id.email_id);
        email_id.getBackground().setAlpha(75);

        messageLayout = (TextInputLayout) findViewById(R.id.messageLayout);
        messageLayout.getBackground().setAlpha(75);

        emailAddress =  findViewById(R.id.input_email);
      //  EditText emailid = (EditText) loginView.findViewById(R.id.login_email);


        ActionBar actionBar;
        actionBar = getSupportActionBar();
//        ColorDrawable colorDrawable
//                = new ColorDrawable(Color.parseColor(ContextCompat.getColor(context,R.color.feedbackBG)));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(),R.color.feedbackfieldBG)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //  getSupportActionBar().setTitle("Hooom");
        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title);
      mTitle.setVisibility(View.VISIBLE);
       mTitle.setText("FEEDBACK");
textInputEditText = findViewById(R.id.input_mobilenumber);
        appCompatButton = findViewById(R.id.btn_sent);
        messageInput = findViewById(R.id.input_msessage);
         progressDialog = new ProgressDialog(FeedbackActivity.this);
        final String HttpUrl = "http://radiorangh.com/app/sent_music_mail.php";

        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailAddress.getText().toString().isEmpty() && messageInput.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"enter email address",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else {
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.show();
                    GetValueFromEditText();

                    if (emailAddress.getText().toString().trim().matches(emailPattern)) {

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Hiding the progress dialog after all task complete.

                                progressDialog.dismiss();

                                Toast.makeText(FeedbackActivity.this, ""+response, Toast.LENGTH_SHORT).show();

                                // Showing response message coming from server.


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Hiding the progress dialog after all task complete.
                                progressDialog.dismiss();

                                // Showing error message if something goes wrong.
                                Toast.makeText(FeedbackActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                        ) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                Map<String, String> params = new HashMap<String, String>();

                                params.put("mobile", mobileNumber);
                                params.put("email", email);

                                params.put("message", messageContent);
                                return params;
                            }
                        };


                        RequestQueue requestQueue = Volley.newRequestQueue(FeedbackActivity.this);

                        requestQueue.add(stringRequest);
                    } else {
                        Toast.makeText(FeedbackActivity.this, "All Fields Required!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

            }


        });









        if (ActivityCompat.checkSelfPermission(this,
                READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tMgr = (TelephonyManager)   this.getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();
            textInputEditText.setText(mPhoneNumber);


            return;
        }
    }

    private void GetValueFromEditText() {

        email = emailAddress.getText().toString().trim();
        mobileNumber  = textInputEditText.getText().toString().trim();
        messageContent = messageInput.getText().toString().trim();
    }


}
