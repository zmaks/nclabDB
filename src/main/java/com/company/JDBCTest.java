package com.company;

/**
 * Created by Максим on 07.12.2015.
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class JDBCTest {

    public static void main(String[] args)  {
        Connection con= null;
        Statement stmt;
        ResultSet rs=null;
        Scanner in = null;

        try {

            Properties prop = loadPropertiesFile();

            String driverClass = prop.getProperty("MYSQLJDBC.driver");
            String url = prop.getProperty("MYSQLJDBC.url");
            String username = prop.getProperty("MYSQLJDBC.username");
            String password = prop.getProperty("MYSQLJDBC.password");

            Class.forName(driverClass);
            con = DriverManager.getConnection(url, username, password);


            if (con != null) {
                System.out.println("connection created successfully using properties file");
                stmt = con.createStatement();
                String query = "select * from phonesbook";
                rs = stmt.executeQuery(query);
                if(rs!=null)
                    while (rs.next()){
                        int id = rs.getInt(1);
                        String name = rs.getString(2);
                        String surname = rs.getString(3);
                        String phone = rs.getString(4);
                        System.out.printf("Selected row: \n id: %d, name: %s, surname: %s, phone: %s  %n", id, name, surname, phone);
                    }

                in = new Scanner(System.in);
                //PreparedStatement
                PreparedStatement preparedStatement = null;
                preparedStatement = con.prepareStatement("SELECT * FROM phonesbook where id = ?");

                System.out.println("Prepared statement:\n Write ID:");
                preparedStatement.setInt(1,in.nextInt());
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
                CallableStatement callableStatement = null;
                callableStatement = con.prepareCall(sql);
                System.out.println("CallableStatement:\n Write name                                                                                                                                                                                                                                            :");
                String name  = in.next();
                callableStatement.setString(1,name);
                callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR);
                callableStatement.execute();
                String phone = callableStatement.getString(2);
                System.out.println("Phone with name " + name + " is " + phone);


                con.close();
                in.close();
            }
            else
                System.out.println("Unable to create connection");
        }catch (SQLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            in.close();
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Properties loadPropertiesFile() throws IOException {

        Properties prop = new Properties();
        InputStream in = new FileInputStream("./src/main/resources/database.properties");
        prop.load(in);
        in.close();
        return prop;
    }
}