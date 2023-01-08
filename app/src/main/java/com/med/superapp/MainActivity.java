package com.med.superapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

private String user="admin";
private String pass="admin";
    private EditText username,password;
    private ImageView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=findViewById(R.id.username);
        password=findViewById(R.id.Password);
        login=findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUsername =username.getText().toString();
                String inputPassord =password.getText().toString();
                if (inputUsername.isEmpty() || inputPassord.isEmpty()){
                    Toast.makeText(MainActivity.this, "Enter credentials correctly",Toast.LENGTH_SHORT).show();
                }else if(inputUsername.equals(user) && inputPassord.equals(pass)){

                        Intent intent=new Intent(MainActivity.this, Hub.class );
                        startActivity(intent);
                    }
                else  Toast.makeText(MainActivity.this, "Enter credentials correctly",Toast.LENGTH_SHORT).show();


            }
        });
    }
}