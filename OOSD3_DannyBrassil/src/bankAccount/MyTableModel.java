package bankAccount;


import java.util.ArrayList;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {
	private ArrayList<Account> data;

		
    private String[] columnNames = {"Name",
                                    "AccountNo",
                                    "Balance"};
    
	public MyTableModel(ArrayList<Account> acc) {
		data = acc;
	}

	
    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
    	Account a = data.get(row);
    	switch(col) {
    	case 0:
    		return a.getName();
       	case 1:
       		return a.getAccNumber();
       	case 2:
       		return a.getBalance();

    	}
    	return null;
    }

    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  */
    
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
    	return true;
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */

    public void setValueAt(Object value, int row, int col) {
		Account acc = data.get(row);
    	
    	if(col == 0)
    		acc.setName((String)value);
    	else if (col == 1)
       		acc.setAcctNumber((int)value);
    	else if (col == 2)
    		acc.setBalance((double)value);

    	data.set(row, acc);
    	// notify model listeners of cell change
    	fireTableCellUpdated(row, col);
    	}

}

