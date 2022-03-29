package demo;/*
Date: 13/03/2022

Simple demo using JDBC to connect to a Database and getting some data.

*/


import java.sql.*;

public class DB_ch13ex15_dentist
{
    public static void main(String args[])
    {
        System.out.println("DB excercise 13.15 normized tables from dentist appointment");
        try
        {
            Statement stmt;
            ResultSet rs;
            String sqlString;

            //Define URL of database server for database named test_hotel
            //on the localhost with the default port number 3306.
            String url = "jdbc:mysql://localhost:3306/mydb";

            //Get a connection to the database for a user named root with password admin
            Connection con = DriverManager.getConnection(url,"root","Skejs123");

            //Display the URL and connection information
            System.out.println("URL: " + url);
            System.out.println("Connection: " + con);


            //Get another statement object initialized as shown.
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            //Query the database, storing the result in an object of type ResultSet
            sqlString = "SELECT * from patient ORDER BY patientNo";
            rs = stmt.executeQuery(sqlString);

            //Use the methods of class ResultSet in a loop
            // to display all of the data in the result.
            System.out.println("Display all results:");
            while(rs.next())
            {
                String col1 = rs.getString("patientNo");
                String col2 = rs.getString("patientName");
                System.out.println("\tpatientNo = " + col1 + "\tpatientName = " + col2);
            }//end while loop

            con.close();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }//end catch
    }//end main
}
