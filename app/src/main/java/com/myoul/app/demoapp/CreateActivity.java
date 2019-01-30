package com.myoul.app.demoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.myoul.app.R;

public class CreateActivity extends AppCompatActivity {

    public static final String LEAD = "com.myoul.app";
    Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        global = (Global)this.getApplication();
    }

    public void createAccount(View view) {
        String user = ((EditText)findViewById(R.id.username)).getText().toString();
        String pass = ((EditText)findViewById(R.id.password)).getText().toString();
        String confirm = ((EditText)findViewById(R.id.confirm)).getText().toString();
        //unused //String email = ((EditText)findViewById(R.id.email)).getText().toString();

        if(!pass.equals(confirm))
            return;

        Profile profile = new Profile();
        profile.user = user;
        profile.pass = pass;
        profile.type = Profile.ProfileType.Normal;

        //global.User = profile;
        global.addUser(profile);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
