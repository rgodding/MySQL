package demo;/*
Date: 13/03/2022
Example code for conecting and using MySQL database...

The purpose of this program is to test the
ability to use JDBC to access a MySQL database
server on localhost.

The MySQL server must be running on localhost
before this program is started.  In addition, a
database named test_hotel must have been created and
a user named root must have been registered on
that database with a password of admin before
this program is started.
*/


import java.sql.*;

public class jdbc_test
{
    public static void main(String args[])
    {
        System.out.println("jdbc_test program is started");
        try
        {
            Statement stmt;
            ResultSet rs;
            String sqlString;

            //Define URL of database server for database named test_hotel
            //on the localhost with the default port number 3306.
            String url = "jdbc:mysql://localhost:3306/test_hotel";

            //Get a connection to the database for a user named root with password admin
            Connection con = DriverManager.getConnection(url,"root","");

            //Display the URL and connection information
            System.out.println("URL: " + url);
            System.out.println("Connection: " + con);

            //Get a Statement object
            stmt = con.createStatement();

            //As a precaution, delete myTable if it
            // already exists as residue from a
            // previous run.  Otherwise, if the table
            // already exists and an attempt is made
            // to create it, an exception will be
            // thrown.
            try
            {
                stmt.executeUpdate("DROP TABLE humhumTable");
            }
            catch(Exception e)
            {
                System.out.print(e);
                System.out.println("No existing table to delete");
            }//end catch

            //Create a table in the database
            sqlString = "CREATE TABLE humhumTable(test_id int," +
                    "test_val char(15) not null)";
            stmt.executeUpdate(sqlString);

            //Insert some values into the table
            sqlString = "INSERT INTO humhumTable(test_id, " +
                    "test_val) VALUES(1,'One')";
            stmt.executeUpdate(sqlString);
            sqlString = "INSERT INTO humhumTable(test_id, " +
                    "test_val) VALUES(2,'Two')";
            stmt.executeUpdate(sqlString);
            sqlString = "INSERT INTO humhumTable(test_id, " +
                    "test_val) VALUES(3,'Three')";
            stmt.executeUpdate(sqlString);
            sqlString = "INSERT INTO humhumTable(test_id, " +
                    "test_val) VALUES(4,'Four')";
            stmt.executeUpdate(sqlString);
            stmt.executeUpdate("INSERT INTO humhumTable(test_id, " +
                    "test_val) VALUES(5,'Five')");

            //Get another statement object initialized as shown.
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            //Query the database, storing the result in an object of type ResultSet
            sqlString = "SELECT * from humhumTable ORDER BY test_id";
            rs = stmt.executeQuery(sqlString);

            //Use the methods of class ResultSet in a loop
            // to display all of the data in the result.
            System.out.println("Display all results:");
            while(rs.next())
            {
                int theInt= rs.getInt("test_id");
                String str = rs.getString("test_val");
                System.out.println("\ttest_id= " + theInt + "\tstr = " + str);
            }//end while loop

            //Query the database, storing the result in an object of type ResultSet
            sqlString = "SELECT * from hotel";
            rs = stmt.executeQuery(sqlString);

            //Use the methods of class ResultSet in a loop
            // to display all of the data in the result.
            System.out.println("Display all results:");
            int hotelNo = 999;
            String hotelName = "dummy", city = "dummy";
            while(rs.next())
            {
                hotelNo= rs.getInt("hotelNo");
                hotelName = rs.getString("hotelName");
                city = rs.getString("city");
                System.out.println("\thotelNo= " + hotelNo + "\thotelName = " + hotelName + "\t\tcity= " + city);
            }//end while loop

            //Query the database, storing the result in an object of type ResultSet
            sqlString = "SELECT * from hotel WHERE city='Copenhagen'";
            rs = stmt.executeQuery(sqlString);

            //Use the methods of class ResultSet in a loop
            // to display all of the data in the result.
            System.out.println("Display all results:");
            while(rs.next())
            {
                hotelNo= rs.getInt("hotelNo");
                hotelName = rs.getString("hotelName");
                city = rs.getString("city");
                System.out.println("\thotelNo= " + hotelNo + "\thotelName = " + hotelName + "\t\tcity= " + city);
            }//end while loop


            //Delete the humhumTable and close the connection to the database
            stmt.executeUpdate("DROP TABLE humhumTable");
            con.close();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }//end catch
    }//end main
}
