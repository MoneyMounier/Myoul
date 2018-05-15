package com.example.myouldb;

import java.util.UUID;
import java.sql.*;

public class Login {

    private static String serverUser = "root";
    private static String serverPass = "Wasr13!!";

    public static String authorize(String user, String pass) {

        if (verifyUser(user)) {
            if (login(user, pass)) {
                UUID id = UUID.randomUUID();

                //verify and set uuid

                return id.toString();
            }
        }
        return("Login failed");
    }

    private static boolean verifyUser(String user){
        try (
                // Step 1: Allocate a database 'Connection' object
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/Login?useSSL=true", serverUser, serverPass);
                // MySQL: "jdbc:mysql://hostname:port/databaseName", "username", "password"

                // Step 2: Allocate a 'Statement' object in the Connection
                Statement stmt = conn.createStatement()
        ) {
            // Step 3: Execute a SQL SELECT query, the query result
            //  is returned in a 'ResultSet' object.
            String strSelect = new StringBuilder("select username from login where username = '").append(user).append("';").toString();

            ResultSet rset = stmt.executeQuery(strSelect);

            // Step 4: Process the ResultSet by scrolling the cursor forward via next().
            //  For each row, retrieve the contents of the cells with getXxx(columnName).
            if(rset.next()) {   // Move the cursor to the next row, return false if no more row
                String dbUser = rset.getString("username");
                if(dbUser.equals(user))
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

    private static boolean login(String user, String pass){
        try (
                // Step 1: Allocate a database 'Connection' object
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/Login?useSSL=true", serverUser, serverPass);
                // MySQL: "jdbc:mysql://hostname:port/databaseName", "username", "password"

                // Step 2: Allocate a 'Statement' object in the Connection
                Statement stmt = conn.createStatement()
        ) {
            // Step 3: Execute a SQL SELECT query, the query result
            //  is returned in a 'ResultSet' object.
            String strSelect = new StringBuilder("select username from login where username = '").append(user).append("' and password = '").append(pass).append("';").toString();

            ResultSet rset = stmt.executeQuery(strSelect);

            // Step 4: Process the ResultSet by scrolling the cursor forward via next().
            //  For each row, retrieve the contents of the cells with getXxx(columnName).
            if(rset.next()) {   // Move the cursor to the next row, return false if no more row
                String dbUser = rset.getString("username");
                if(dbUser.equals(user))
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

    /*
    A JDBC (Java Database Connectivity) program comprises the following steps:

    Allocate a Connection object, for connecting to the database server.
    Allocate a Statement object, under the Connection object created, for holding a SQL command.
    Write a SQL query and execute the query, via the Statement and Connection created.
    Process the query result.
    Close the Statement and Connection object to free up the resources.
    */
}
