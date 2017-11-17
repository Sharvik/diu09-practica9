package model;

import java.sql.*;
import java.util.logging.*;

public class DataBaseConnector {
    
    private Connection connect;
    
    private final static int SUCCESS = 0;
    private final static int FAILURE = -1;

    public DataBaseConnector(String user, String pass) {
        connectToDB(user, pass);
    }
    
    
    
    private int connectToDB(String user, String pass) {        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://genome-mysql.soe.ucsc.edu:3306/hg38?useSSL=true",
                    user,
                    pass);
            
            DatabaseMetaData md = connect.getMetaData();
            String[] types = {"TABLE"};
            //ResultSet rs = md.getTables(null, null, "%", null);
            ResultSet rs = md.getTables(null, null, "%", types);
            
            while(rs.next()) {
                String tablename = rs.getString(3/*"TABLE_NAME"*/);
                System.out.println("Tabla : " + tablename);
                
                ResultSet rs2 = md.getColumns(null, null, tablename, null);
                while(rs2.next()) {
                    String fieldname = rs2.getString("COLUMN_NAME");
                    System.out.println("    Campo : " + fieldname);
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            return FAILURE;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataBaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            return FAILURE;
        }
        return SUCCESS;
    }
    
    public void disconnect() {
        try {
            connect.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
