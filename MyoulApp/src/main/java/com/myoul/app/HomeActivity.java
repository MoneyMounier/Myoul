package com.myoul.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.LEAD);

        // Capture the layout's TextView and set the string as its text
        //TextView textView = findViewById(R.id.textView);
        //textView.setText(message);
    }
}
