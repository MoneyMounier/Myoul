package com.server.myoul;

import java.security.PublicKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class LoginServer {

    public static String authorize(Message message, PublicKey key) {

        String pass = (String)message.args;
        String user = message.user;

        System.out.println(user + " " + pass);
        return "great success!!!";

        /*
        if (login(user, pass)) {
            return "Logged in";
        }
        return("Login failed");
        */
    }


    private static boolean login(String user, String pass){

        try {
            ResultSet rset = MyoulServer.query(String.format("select username from login where username = '%s' and password = '%s';", user, pass));

            if(rset.first()) {
                return true;
            }
            else
                return false;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    private static boolean verifyMAC(String mac){
        try {
            ResultSet rset = MyoulServer.query(String.format("select username from login where mac = '%s';", mac));

            return rset.first();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static boolean setMAC(String mac, String user){
        int rset= MyoulServer.update(String.format("update login set mac = '%s', time = %d where username = '%s';", mac, System.currentTimeMillis(), user));
        if(rset != 0)
            return true;
        else
            return false;
    }
    /*
    A JDBC (Java Database Connectivity) program comprises the following steps:

    Allocate a Connection object, for connecting to the database server.
    Allocate a Statement object, under the Connection object created, for holding a SQL command.
    Write a SQL query and execute the query, via the Statement and Connection created.
    Process the query result.
    Close the Statement and Connection object to free up the resources.
    */
}
