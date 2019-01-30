package com.myoul.app.demoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.myoul.app.R;

public class SendActivity extends AppCompatActivity {

    public static final String LEAD = "com.myoul.app";
    Global global;

    private int current;
    private int[] layouts = {0, R.layout.activity_send_1, R.layout.activity_send_2, R.layout.activity_send_3, 0, R.layout.activity_send_5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        current = getIntent().getIntExtra("current", 1);
        setContentView(layouts[current]);
        global = (Global) this.getApplication();
    }

    public void nextS(View view){
        current++;
        if(current == 4){
            Intent intent = new Intent(this, SendMapActivity.class);
            startActivity(intent);
        }else
            setContentView(layouts[current]);
    }
    public void prevS(View view){
        current--;
        if(current == 4){
            Intent intent = new Intent(this, SendMapActivity.class);
            startActivity(intent);
        }else
            setContentView(layouts[current]);
    }
    public void done(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
