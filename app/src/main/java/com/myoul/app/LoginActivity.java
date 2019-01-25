package com.myoul.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    public static final String LEAD = "com.myoul.app";
    private Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        global = (Global)this.getApplication();
        global.create();
    }

    //login script
    public void login(View view) {

        String user = ((EditText)findViewById(R.id.username)).getText().toString();
        String pass = ((EditText)findViewById(R.id.password)).getText().toString();

        Profile profile = global.getUser(user, pass);

        if(profile != null) {
            global.User = profile;
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }else{
            ((TextView)findViewById(R.id.error)).setText("Invalid Username or Password");
        }
    }

    public void create(View view){
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }
}
