package com.company;

/**
 * Created by Максим on 07.12.2015.
 */

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class JDBCTest {

    private static final Logger logger = Logger.getLogger(JDBCTest.class.getName());

    public static void main(String[] args) {
        Properties prop = loadPropertiesFile();

        try (Connection con = getConnection(prop)) {
            if (con != null) {
                logger.info("Statement");
                logger.info(UseStatements.getStatementResult(con, "select * from phonesbook"));

                //PreparedStatement
                logger.info("PreparedStatement");
                logger.info(UseStatements.getPreparedStatementResult(con));

                //Callabele statment
                logger.info("Callabele statment");
                logger.info(UseStatements.getCallabledStatementResult(con));

                //Batch update
                logger.info("Batch update:");
                logger.info(UseStatements.BatchUpdate(con));

            } else
                logger.error("Unable to create connection");
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }

    }


    public static Properties loadPropertiesFile() {

        Properties prop = new Properties();
        try (InputStream in = new FileInputStream("./src/main/resources/database.properties")) {

            prop.load(in);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return prop;
    }

    public static Connection getConnection(Properties prop) throws ClassNotFoundException, SQLException {
        String driverClass = prop.getProperty("MYSQLJDBC.driver");
        String url = prop.getProperty("MYSQLJDBC.url");
        String username = prop.getProperty("MYSQLJDBC.username");
        String password = prop.getProperty("MYSQLJDBC.password");
        Class.forName(driverClass);
        Connection con = DriverManager.getConnection(url, username, password);
        return con;
    }

}