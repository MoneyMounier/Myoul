package com.server.myoul;

import java.util.UUID;
import java.sql.*;

public class LoginServer {

    private static String serverUser = "root";
    private static String serverPass = "Wasr13!!";

    public static String authorize(String user, String pass) {

        if (login(user, pass)) {
            UUID id = UUID.randomUUID();

            while(!verifyUUID(id.toString()))
                System.out.println("nonUniqueID!!!");

            while(!setUUID(id.toString(), user))
                System.out.println("Failed to set unique ID");

            return id.toString();
        }
        return("Login failed");
    }


    private static boolean login(String user, String pass){
        try (
                // Step 1: Allocate a database 'Connection' object
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://127.0.0.1:3306/Myoul", serverUser, serverPass);
                // MySQL: "jdbc:mysql://hostname:port/databaseName", "username", "password"

                // Step 2: Allocate a 'Statement' object in the Connection
                Statement stmt = conn.createStatement()
        ) {
            // Step 3: Execute a SQL SELECT query, the query result
            //  is returned in a 'ResultSet' object.
            String strSelect = String.format("select username from login where username = '%s' and password = '%s';", user, pass);

            ResultSet rset = stmt.executeQuery(strSelect);

            // Step 4: Process the ResultSet by scrolling the cursor forward via next().
            //  For each row, retrieve the contents of the cells with getXxx(columnName).
            if(rset.next()) {   // Move the cursor to the next row, return false if no more row
                return true;
            }
            else
                return false;

        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        // Step 5: Close the resources - Done automatically by try-with-resources

        return false;
    }


    private static boolean verifyUUID(String uuid){
        try (
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://127.0.0.1:3306/Myoul", serverUser, serverPass);
                Statement stmt = conn.createStatement()
        ) {
            String strSelect = String.format("select uuid from login where uuid = '%s';", uuid);
            ResultSet rset = stmt.executeQuery(strSelect);

            if(rset.next())
                return false;
            else
                return true;
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private static boolean setUUID(String uuid, String user){
        try (
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://127.0.0.1:3306/Myoul", serverUser, serverPass);
                Statement stmt = conn.createStatement()
        ) {
            String strSelect = String.format("update login set uuid = '%s', time = %d where username = '%s';", uuid, System.currentTimeMillis(), user);
            stmt.executeQuery(strSelect);
            return true;//should check that it was correctly set
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
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
