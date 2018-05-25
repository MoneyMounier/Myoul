package com.myoul.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.client.myoul.LoginClient;
import com.client.myoul.MyoulClient;

public class LoginActivity extends AppCompatActivity {

    public static final String LEAD = "com.myoul.app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    //login script
    public void login(View view) {
        EditText editText = (EditText) findViewById(R.id.username);
        String user = editText.getText().toString();
        editText = (EditText) findViewById(R.id.password);
        String pass = editText.getText().toString();

        String id = LoginClient.Login(user, pass, getString(R.string.homeip), Integer.parseInt(getString(R.string.port)));

        if (id == null){
            ((TextView)findViewById(R.id.error)).setText(R.string.cerror);
        } else if (id.length() == 36){
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(LEAD, id);
            startActivity(intent);
        }else{
            //write error to screen
            ((TextView)findViewById(R.id.error)).setText(R.string.lerror);
        }
    }

    public void create(View view){
        MyoulClient.query("email nick_mounier@yahoo.com", "mounien.ddns.net", 3666);
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }
}
