package com.med.superapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Sms extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS=0;
    EditText messageText;
    String phoneNo;
    String message;
    Button boutonEnvoi;
    EditText numeroTelephone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        messageText=(EditText) findViewById(R.id.editText);
        numeroTelephone=(EditText)findViewById(R.id.editText2);
        boutonEnvoi=(Button) findViewById(R.id.button);
        boutonEnvoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                envoyerSMS();
            }
        });

    }
    protected void envoyerSMS(){
        phoneNo=numeroTelephone.getText().toString();
        message=messageText.getText().toString();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS)){
            }else {ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},MY_PERMISSIONS_REQUEST_SEND_SMS);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_SEND_SMS: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    SmsManager gestionnaireSMS = SmsManager.getDefault();
                    gestionnaireSMS.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS envoyé :)", Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(getApplicationContext(), "SMS pas envoyé", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }


}