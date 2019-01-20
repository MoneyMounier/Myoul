package com.myoul.app;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.client.myoul.LoginClient;
import com.client.myoul.MyoulClient;
import com.server.myoul.Message;

import java.util.UUID;

public class LoginActivity extends AppCompatActivity {

    public static final String LEAD = "com.myoul.app";
    protected MyoulClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(((Client)getApplication()).client == null)
            ((Client)getApplication()).client = new MyoulClient(getString(R.string.ip), Integer.parseInt(getString(R.string.port)));

        client = ((Client)this.getApplication()).client;
    }

    //login script
    public void login(View view) {
        EditText editText = (EditText) findViewById(R.id.username);
        String user = editText.getText().toString();
        editText = (EditText) findViewById(R.id.password);
        String pass = editText.getText().toString();

        Message sent = LoginClient.Login(client, user, pass);

        /////////////////////code for checking for and recieving a message
        if(sent != null) {
            final Handler handler = new Handler();
            final int delay = 500; //ms
            final UUID id = sent.id;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String temp = LoginClient.checkReply(client, id);

                    if (temp == null)
                        handler.postDelayed(this, delay);
                    else
                        System.out.println(temp);//handle message here
                }
            }, delay);
        }
        //////////////////////

       /*if (sent){
            ((TextView)findViewById(R.id.error)).setText(R.string.cerror);
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(LEAD, "");
            startActivity(intent);
        } else if (id.length() == 36){
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra(LEAD, "");
            startActivity(intent);
        }else{
            //write error to screen
            ((TextView)findViewById(R.id.error)).setText(R.string.lerror);
        }*/
    }

    public void create(View view){
        //testing
        //MyoulClient.query("email nick_mounier@yahoo.com", "mounien.ddns.net", 3666);
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }
}
