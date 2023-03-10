package com.med.superapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Hub extends AppCompatActivity {
ImageView sms,call,camera,music,mail,search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        camera=findViewById(R.id.camera);
        call=findViewById(R.id.call);
        mail=findViewById(R.id.mail);
        sms=findViewById(R.id.sms);
        music=findViewById(R.id.music);
        search=findViewById(R.id.search);

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Hub.this,Sms.class );
                startActivity(intent);
            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Hub.this,Camera.class );
                startActivity(intent);
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Hub.this,Call.class );
                startActivity(intent);
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Hub.this,Mail.class );
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW );
                intent.setData(Uri.parse("http://www.google.ma"));
                startActivity(intent);
            }
        });
      music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Hub.this,Music.class );
                startActivity(intent);
            }
        });

    }
}