package com.company;

/**
 * Created by Максим on 07.12.2015.
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class JDBCTest {

    public static void main(String[] args)  {
        Properties prop = loadPropertiesFile();
        String driverClass = prop.getProperty("MYSQLJDBC.driver");
        String url = prop.getProperty("MYSQLJDBC.url");
        String username = prop.getProperty("MYSQLJDBC.username");
        String password = prop.getProperty("MYSQLJDBC.password");

        try{
            Class.forName(driverClass);
            try (Connection con = DriverManager.getConnection(url, username, password)) {
            if (con != null) {
                System.out.println("connection created successfully using properties file");
                Statement stmt = con.createStatement();
                String query = "select * from phonesbook";
                ResultSet rs = stmt.executeQuery(query);
                if (rs != null)
                    while (rs.next()) {
                        int id = rs.getInt(1);
                        String name = rs.getString(2);
                        String surname = rs.getString(3);
                        String phone = rs.getString(4);
                        System.out.printf("Selected row: \n id: %d, name: %s, surname: %s, phone: %s  %n", id, name, surname, phone);
                    }

                Scanner in = new Scanner(System.in);
                //PreparedStatement
                PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM phonesbook where id = ?");

                System.out.println("Prepared statement:\n Write ID:");
                preparedStatement.setInt(1, in.nextInt());
                ResultSet result = preparedStatement.executeQuery();


                System.out.println("PreparedStatement");
                while (result.next()) {
                    int id = result.getInt(1);
                    String name = result.getString(2);
                    String surname = result.getString(3);
                    String phone = result.getString(4);
                    System.out.printf("Selected row: \n id: %d, name: %s, surname: %s, phone: %s  %n", id, name, surname, phone);
                }

                //Callabele statment

                String sql = "{call getPhone (?, ?)}"; //gets phone by name
                CallableStatement callableStatement =  con.prepareCall(sql);
                System.out.println("CallableStatement:\n Write name                                                                                                                                                                                                                                            :");
                String name = in.next();
                callableStatement.setString(1, name);
                callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR);
                callableStatement.execute();
                String phone = callableStatement.getString(2);
                System.out.println("Phone with name " + name + " is " + phone);

                //batch update
                System.out.println("Batch update:");
                Statement stmt2 = con.createStatement();
                stmt2.addBatch("UPDATE phonesbook SET NAME = UPPER(NAME) WHERE id = 1");
                stmt2.addBatch("UPDATE phonesbook SET NAME = UPPER(NAME) WHERE id = 3");
                stmt2.executeBatch();
                ResultSet res = stmt2.executeQuery("select * from phonesbook");
                if (res != null)
                    while (res.next())
                        System.out.printf("Selected row: \n id: %d, name: %s, surname: %s, phone: %s  %n", res.getInt(1), res.getString(2), res.getString(3), res.getString(4));


            } else
                System.out.println("Unable to create connection");
        }
        }catch (SQLException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static Properties loadPropertiesFile() {

        Properties prop = new Properties();
        try(InputStream in = new FileInputStream("./src/main/resources/database.properties")){

            prop.load(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}