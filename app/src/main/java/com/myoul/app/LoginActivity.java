package com.myoul.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
        Intent intent = new Intent(this, CompleteDeliveryActivity.class);
        startActivity(intent);
    }

    public void create(View view){
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }
}
