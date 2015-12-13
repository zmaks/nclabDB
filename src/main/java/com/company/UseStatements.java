package com.company;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.Scanner;

/**
 * Created by Максим on 13.12.2015.
 */
public class UseStatements {
    private static final Logger logger = Logger.getLogger(UseStatements.class.getName());

    public static String getStatementResult(Connection con, String query) {
        ResultSet rs = null;
        String resStr = "Selected row: \n";
        try (Statement stmt = con.createStatement();) {
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                resStr+= rs.getInt(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4)+"\n";
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return resStr;
    }

    public static String getPreparedStatementResult(Connection con) {
        ResultSet rs = null;
        String resStr = "Selected row: \n";
        try(PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM phonesbook where id = ?")) {
            Scanner in = new Scanner(System.in);
            System.out.print("Prepared statement:\n SELECT * FROM phonesbook where id = ?\n\n Write ID:");
            int id = in.nextInt();
            preparedStatement.setInt(1, id);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                resStr+= rs.getInt(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4)+"\n";
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return resStr;
    }

    public static String getCallabledStatementResult(Connection con) {
        String sql = "{call getPhone (?, ?)}"; //gets phone by name
        String res="";

        try(CallableStatement callableStatement = con.prepareCall(sql)) {
            System.out.println("CallableStatement:\n Write name ");
            Scanner in = new Scanner(System.in);
            String name = in.next();
            callableStatement.setString(1, name);
            callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR);
            callableStatement.execute();
            String phone = callableStatement.getString(2);
            res = "Phone with name " + name + " is " + phone;
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return res;
    }

    public static String BatchUpdate(Connection con){
        String res = "";
        try(PreparedStatement preparedStatement = con.prepareStatement("UPDATE phonesbook SET NAME = UPPER(NAME) WHERE id = ?")) {
            if(con.getMetaData().supportsBatchUpdates()) {
                Scanner in = new Scanner(System.in);
                System.out.println("Batch update:");
                System.out.println("Write ID:");
                preparedStatement.setInt(1, in.nextInt());
                preparedStatement.addBatch();

                System.out.println("Write ID:");
                preparedStatement.setInt(1, in.nextInt());

                preparedStatement.addBatch();

                int[] affectedRecords = preparedStatement.executeBatch();

                for (int i = 0; i < affectedRecords.length; i++) {
                    res += "\n In " + (i + 1) + " query " + affectedRecords[i] + " rows updated \n";
                }
            }
            else
                logger.error("Your database dosen't support batch updates");
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return res;
    }
}
