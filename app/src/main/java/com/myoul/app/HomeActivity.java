package com.myoul.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    public static final String LEAD = "com.myoul.app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Intent intent = getIntent();
        //String message = intent.getStringExtra(LoginActivity.LEAD);

        // Capture the layout's TextView and set the string as its text
        //TextView textView = findViewById(R.id.textView);
        //textView.setText(message);
    }

    /*public void deliver(View view){
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(LEAD, 'd');
        startActivity(intent);
    }

    public void send(View view){
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(LEAD, 's');
        startActivity(intent);
    }*/
}
