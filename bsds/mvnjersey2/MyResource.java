package com.bsds.mvnjersey2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.json.*;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    ConnectionManager connection = new ConnectionManager();
    Connection con = null;

    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Path("/myvert")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)

    public String getIt(@QueryParam("SkierID") int sid, @QueryParam("dayNum") int dayNum) throws SQLException {
        ResultSet rs = null;
        int numberOfLifts;
        int vertical;

        con = connection.getConnection();

        String info = "select count(LiftID) from SKIER where SkierID=" + sid;
        String info2 = "select SUM(VERTICALCOUNT) from SKIER where SkierID=" + sid;

        PreparedStatement preparedStatement = con.prepareStatement(info);
        rs = preparedStatement.executeQuery();
        rs.next();
        numberOfLifts = rs.getInt(1);

        PreparedStatement getVertical = con.prepareStatement(info2);
        rs = getVertical.executeQuery();
        rs.next();
        vertical = rs.getInt(1);

        connection.closeConnection(con);

        return " Skier ID =" + Integer.toString(sid) + " Number OF DAY = " + Integer.toString(dayNum) + "NUMBER OF LIFTS = " + Integer.toString(numberOfLifts)
                + " Vertical = " + Integer.toString(vertical);
    }

    @POST
    @Path("/load")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String putText(RFIDLiftData skiLiftData) throws SQLException {

        int vertical_count = 0;
        int liftID = skiLiftData.getLiftID();

        if (liftID <= 10) {
            vertical_count = 200;
        }
        if (liftID >= 11 && skiLiftData.getLiftID() <= 20) {
            vertical_count = 300;
        }
        if (liftID >= 21 && liftID <= 30) {
            vertical_count = 400;
        }
        if (liftID >= 31 && liftID <= 40) {
            vertical_count = 500;
        }

        con = connection.getConnection();
        
        String insertTableSQL = "INSERT INTO SKIER"
                + "(ResortID, Day, SkierID, LiftID,Time,VERTICALCOUNT) VALUES"
                + "(?,?,?,?,?,?)";

        PreparedStatement preparedStatement = con.prepareStatement(insertTableSQL);

        preparedStatement.setInt(1, skiLiftData.getResortID());
        preparedStatement.setInt(2, skiLiftData.getDayNum());
        preparedStatement.setInt(3, skiLiftData.getSkierID());
        preparedStatement.setInt(4, skiLiftData.getLiftID());
        preparedStatement.setInt(5, skiLiftData.getTime());
        preparedStatement.setInt(6, vertical_count);

        preparedStatement.executeUpdate();

        connection.closeConnection(con);

        return Integer.toString(skiLiftData.getSkierID());

    }

    @GET
    @Path("/test")
    @Consumes(MediaType.APPLICATION_JSON)
    public String test() {
        return "working!";
    }
}
