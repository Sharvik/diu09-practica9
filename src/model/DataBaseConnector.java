package model;

import java.sql.*;
import java.util.logging.*;

public class DataBaseConnector {
    
    private final static int SUCCESS = 0;
    private final static int FAILURE = -1;

    public DataBaseConnector(String user, String pass) {
        connectToDB(user, pass);
    }
    
    
    
    private int connectToDB(String user, String pass) {        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://mozart.dis.ulpgc.es:3306/PracticaDIU?useSSL=true",
                    user,
                    pass);
            
            DatabaseMetaData md = con.getMetaData();
            String[] types = {"TABLE"};
            ResultSet rs = md.getTables(null, null, "%", types);
            
            while(rs.next()) {
                String tablename = rs.getString("TABLE_NAME");
                System.out.println("Tabla : " + tablename);
                
                ResultSet rs2 = md.getColumns(null, null, tablename, null);
                while(rs.next()) {
                    String fieldname = rs.getString("COLUMN_NAME");
                    System.out.println("    Campo : " + fieldname);
                }
            }
            
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            return FAILURE;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataBaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            return FAILURE;
        }
        
        
        
        return SUCCESS;
    }
}
