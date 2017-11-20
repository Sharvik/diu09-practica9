package model;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import javax.swing.JOptionPane;

public class DataBaseConnector {

    private Connection connect;
    private final List<String> tables;

    public final static int SUCCESS = 0;
    public final static int FAILURE = -1;

    public DataBaseConnector(String user, String pass) {
        tables = new ArrayList<>();

        if (connectToDB(user, pass) == FAILURE) {
            JOptionPane.showMessageDialog(
                    null,
                    "Incorrect username/password",
                    "Authentication failure",
                    JOptionPane.OK_OPTION);
        }
    }

    private int connectToDB(String user, String pass) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://mozart.dis.ulpgc.es:3306/PracticaDIU?useSSL=true",
                    //"jdbc:mysql://genome-mysql.soe.ucsc.edu:3306/hg38?useSSL=true",
                    user,
                    pass);

            initializeTables();

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DataBaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            return FAILURE;
        }
        return SUCCESS;
    }

    public void disconnect() {
        try {
            if (connect != null)connect.close();            
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initializeTables() throws SQLException {

        DatabaseMetaData md;
        md = connect.getMetaData();
        String[] types = {"TABLE"};
        ResultSet rs = md.getTables(null, null, "%", types);

        while (rs.next()) {
            String tablename = rs.getString("TABLE_NAME");
            tables.add(tablename);
            /*System.out.println("Tabla : " + tablename);
                
                ResultSet rs2 = md.getColumns(null, null, tablename, null);
                while(rs2.next()) {
                    String fieldname = rs2.getString("COLUMN_NAME");
                    System.out.println("    Campo : " + fieldname);
                }*/
        }
    }

    public ArrayList getField(String tablename) {
        ArrayList<String> arrayList = new ArrayList<>();
        DatabaseMetaData md;
        try {
            md = connect.getMetaData();

            /*String[] types = {"TABLE"};
            ResultSet rs = md.getTables(null, null, "%", types);*/
            ResultSet rs = md.getColumns(null, null, tablename, null);

            while (rs.next()) {
                arrayList.add(rs.getString("COLUMN_NAME"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DataBaseConnector.class.getName()).log(Level.SEVERE, null, ex);
        }

        return arrayList;
    }

    public List<String> getTables() {
        return tables;
    }
}
