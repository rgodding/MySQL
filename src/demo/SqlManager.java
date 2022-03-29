package demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class SqlManager {
    Connection con;
    Statement stmt;
    ResultSet rs;
    String sqlString;
    String databaseName;
    String[] databaseTable;
    String databaseDescription;
    ArrayList<Integer> ids = new ArrayList<>();
    File file = new File("data/database.txt");

    public void start(){
        System.out.println("Starting program");
        try {
            //Define URL of the database
            String url = "jdbc:mysql://localhost:3306/mydb";

            //Get connection to database
            Scanner sc = new Scanner(System.in);
            System.out.print("Insert password: ");
            String password = sc.nextLine();
            con = DriverManager.getConnection(url,"root", password);

            System.out.println("connection starting?");
            //Display the URL and connection information
            System.out.println("URL: " + url);
            System.out.println("Connection: " + con);

            //Get a Statement object
            Scanner scFile = new Scanner(file);
            ArrayList<String> databases = new ArrayList<>();
            while(scFile.hasNextLine()){
                databases.add(scFile.nextLine());
            }

            for (int i = 0; i < databases.size(); i++) {
                String[] tempData = databases.get(i).split(";");
                System.out.println("Database " + (i+1) + ": " + tempData[0]);
            }
            System.out.print("choose a database: ");
            String input = sc.nextLine();
            while(!input.matches("\\d+")){
                System.out.println("Insert a positive number please");
                input = sc.nextLine();
            }
            int inputInt = Integer.parseInt(input);
            while(inputInt == 0 || inputInt > databases.size()){
                System.out.println("invalid number chosen");
                System.exit(0);
            }

            String[] database = databases.get(inputInt-1).split(";");

            databaseName = database[0];
            databaseTable = database[1].split("_");
            databaseDescription = database[2];
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sqlString = "SELECT * FROM " + databaseName + " ORDER BY id";
            program();
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("here we go, what's wrong");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void program(){
        Scanner sc = new Scanner(System.in);
        try {
            rs = stmt.executeQuery(sqlString);
            viewTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        boolean programON = true;
        while(programON){
            try {
                rs = stmt.executeQuery(sqlString);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("""
                    Menu:
                    1: View Table                   
                    2: Add to Table
                    3: Remove from Table
                    0: Exit Program
                    """);
            System.out.println("Enter choice: ");
            String input = sc.nextLine();
            switch(Integer.parseInt(input)){
                case 1:
                    viewTable();
                    break;
                case 2:
                    addToTable();
                    break;
                case 3:
                    removeTable();
                    break;
                case 0:
                    programON = false;
                    break;
                default:
                    System.out.println("invalid choice");
            }
        }

    }
    public void viewTable(){
        System.out.println("Table data");
        ArrayList<Integer> ids = new ArrayList<>();
        try {
            while (rs.next()) {
                ids.add(Integer.parseInt(rs.getString("id")));
                for (int i = 0; i < databaseTable.length; i++) {
                    System.out.print(databaseTable[i] + ": (" + rs.getString(databaseTable[i]) + "), ");
                }
                System.out.print("\n");
            }
            this.ids = ids;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addToTable(){
        Scanner sc = new Scanner(System.in);
        try {
            String data = "INSERT INTO " + databaseName + "(" + databaseTable[0];
            for (int i = 1; i < databaseTable.length; i++) {
                data += ", " + databaseTable[i];
            }
            data += ") ";
            data += "VALUES(";
            data += idGenerator();

            for (int i = 1; i < databaseTable.length; i++) {
                System.out.println(databaseTable[i] + ": ");
                String temp = sc.nextLine();
                data += ", " + "'" + temp + "'";
            }
            data += ")";
            System.out.println(data);
            stmt.executeUpdate(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void removeTable(){
        Scanner sc = new Scanner(System.in);
        try {
            String data = "DELETE FROM " + databaseName + " WHERE id = ";
            viewTable();
            System.out.print("Write which table to delete: ");
            String input = sc.nextLine();
            System.out.println("here we go");
            data += input;
            stmt.executeUpdate(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //tools
    public int idGenerator(){
        int id = 0;
        Random random = new Random();
        id = random.nextInt(9999+1);
        boolean invalid = true;
        while(invalid){
            invalid = false;
            for (int i = 0; i < ids.size(); i++) {
                if(id == ids.get(i)){
                    invalid = true;
                }
            }
        }
        return id;
    }

}
