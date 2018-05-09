package com.myoul.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.File;

public class Login extends AppCompatActivity {

    public static final String LEAD = "com.myoul.app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    //login script
    public void sendMessage(View view){
        EditText editText = (EditText) findViewById(R.id.username);
        String user = editText.getText().toString();

        if(verifyUser(user)){
            editText = (EditText) findViewById(R.id.password);
            String pass = editText.getText().toString();
            if(login(user, pass)){
                Intent intent = new Intent(this, Home.class);
                intent.putExtra(LEAD, user.concat(" ").concat(pass));
                startActivity(intent);
            }
        }


        //write error invalid password or username to ogin screen
    }

    private boolean verifyUser(String user){
        //will verify user on database
        return true;
    }

    private boolean login(String user, String pass){
        //will actually login to database
        return true;
    }
}
