package control;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import model.DataBaseConnector;

public class ListContentLoader {
    
    private final JList listTables;
    private final JList listFields;
    private final DataBaseConnector db;
    
    public ListContentLoader(String user, String pass, JList listTables, JList listFields) {
        this.listTables = listTables;
        this.listFields = listFields;
        db = new DataBaseConnector(user, pass);
    }

    public int addTables() {
        if(db.getTables().isEmpty())
            return DataBaseConnector.FAILURE;
        
        DefaultListModel<String> lm = new DefaultListModel<>();
        
        for (String table : db.getTables()) {
            lm.addElement(table);
        }
        
        listTables.setModel(lm);
        listTables.setEnabled(true);
        
        return DataBaseConnector.SUCCESS;
    }
    
    public int addFields() {
        if(db.getTables().isEmpty()) 
            return DataBaseConnector.FAILURE;
        
        DefaultListModel<String> lm = new DefaultListModel<>();
        
        for (Object selected : listTables.getSelectedValuesList()) {
            String tablename = String.valueOf(selected);
            db.getField(tablename).forEach((fields) -> {
                lm.addElement(tablename + "." + fields);
            });
        }
        
        listFields.setModel(lm);
        listFields.setEnabled(true);
        
        return DataBaseConnector.SUCCESS;
    }
    
    public void disconnect() {
        db.disconnect();
        resetList();
        
        DefaultListModel<String> lm = new DefaultListModel<>();
        listTables.setModel(lm);
        listTables.setEnabled(false);
    }
    
    public void resetList() {
        DefaultListModel<String> lm = new DefaultListModel<>();
        listFields.setModel(lm);
        listFields.setEnabled(false);
        
        listTables.clearSelection();
    }
    
}
