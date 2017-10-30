/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsds.mvnjersey2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author prach
 */
public class ConnectionManager {

    public Connection getConnection() throws SQLException {
        Connection con = null;
        Statement stmt = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://skidbinstance.c0nyye1pshf0.us-east-1.rds.amazonaws.com:3306/skidb", "Administrator", "rdsdbski");

            stmt = (Statement) con.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS SKIER "
                    + "(ResortId VARCHAR(255), "
                    + " Day VARCHAR(255), "
                    + " SkierId VARCHAR(255), "
                    + " LiftId VARCHAR(255), "
                    + " Time VARCHAR(255), "
                    + " VERTICALCOUNT VARCHAR(255))";
                    

            stmt.executeUpdate(sql);

        } catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }

    public void closeConnection(Connection connection) throws SQLException {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
