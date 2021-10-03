import java.sql.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
/*
CSCE 315
9-27-2021 Lab
 */
public class jdbcpostgreSQL {

  //Commands to run this script
  //This will compile all java files in this directory
  //javac *.java 
  //This command tells the file where to find the postgres jar which it needs to execute postgres commands, then executes the code
  //Windows: java -cp ".;postgresql-42.2.8.jar" jdbcpostgreSQL
  //Mac/Linux: java -cp ".:postgresql-42.2.8.jar" jdbcpostgreSQL

  //MAKE SURE YOU ARE ON VPN or TAMU WIFI TO ACCESS DATABASE
  public static void main(String args[]) {
 
    //Building the connection with your credentials
    //TODO: update dbName, userName, and userPassword here
     Connection conn = null;
     String teamNumber = "7";
     String sectionNumber = "902";
     String dbName = "csce315" + sectionNumber + "_" + teamNumber + "db";
     String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
     String userName = "csce315" + sectionNumber + "_" + teamNumber + "user";
     String userPassword = "Amoonguss";

    //Connecting to the database 
    try {
        conn = DriverManager.getConnection(dbConnectionString,userName, userPassword);
     } catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);
     }

     System.out.println("Opened database successfully");
     
     try {
       
       Scanner sc = new Scanner(new File("cleaned_titles.csv"), "UTF-8");

       sc.useDelimiter("\n");

       ArrayList<String[]> title_data = new ArrayList<String[]>();

       while(sc.hasNext()) {
         String line = sc.next();
         //System.out.println(line);
         String[] split_line = line.split("\t");
         title_data.add(split_line);
         //System.out.println(line);
       }
       sc.close();

       //create a statement object

       PreparedStatement statement = conn.prepareStatement("INSERT INTO titles (id, name, runtime, year, average_rating, type, votes) VALUES (?,?,?,?,?,?,?);");
       
       title_data.remove(0);

       for(String[] row : title_data) {
         statement.setInt(1, Integer.parseInt(row[0].trim()));
         statement.setString(2, row[2]);
         if(row[3] == "") {
          statement.setInt(3, -1);
         }
         else {
           statement.setInt(3, Integer.parseInt(row[3].trim()));
         }
         statement.setInt(4, Integer.parseInt(row[4].trim()));
         statement.setFloat(5, Float.parseFloat(row[5].trim()));
         statement.setString(6, row[1]);
         statement.setInt(7, Integer.parseInt(row[6].trim()));
         statement.executeUpdate();
       }

       statement.close();

       System.out.println("Done uploading titles!");

       //Upload genre data

       PreparedStatement statement2 = conn.prepareStatement("INSERT INTO genres (title_id, genre_name) VALUES (?,?);");

       Scanner sc2 = new Scanner(new File("cleaned_genres.csv"), "UTF-8");

       sc2.useDelimiter("\n");

       ArrayList<String[]> genre_data = new ArrayList<String[]>();

       while(sc2.hasNext()) {
         String line = sc2.next();
         String[] split_line = line.split("\t");
         genre_data.add(split_line);
       }
        sc2.close();

        genre_data.remove(0);

       for(String[] row : genre_data) {
         statement2.setInt(1, Integer.parseInt(row[0].trim()));
         statement2.setString(2, row[1].trim());
         statement2.executeUpdate();
       }

        statement2.close();

        System.out.println("Done uploading genres data!");

       //send statement to DBMS
       //This executeQuery command is useful for data retrieval
       //ResultSet result = stmt.executeQuery(sqlStatement);
       //OR
       //This executeUpdate command is useful for updating data
       //long[] result = stmt.executeLargeBatch();

       //OUTPUT
       //You will need to output the results differently depeninding on which function you use
       System.out.println("--------------------Query Results--------------------");
       //while (result.next()) {
       //System.out.println(result.getString("column_name"));
       //}
       //OR
       //System.out.println(result);
   } catch (Exception e){
       e.printStackTrace();
       System.err.println(e.getClass().getName()+": "+e.getMessage());
       System.exit(0);
   }
    
    //closing the connection
    try {
      conn.close();
      System.out.println("Connection Closed.");
    } catch(Exception e) {
      System.out.println("Connection NOT Closed.");
    }//end try catch
  }//end main
}//end Class