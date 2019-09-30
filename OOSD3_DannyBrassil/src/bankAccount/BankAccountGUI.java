package bankAccount;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.border.TitledBorder;


import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;

public class BankAccountGUI extends JFrame{
	
	// Menu structure
	private JMenuBar myBar;
	private JMenu fileMenu, recordMenu;
	private JMenuItem fileLoad, fileSaveAs, removeAccount;
	
	
	//Arraylist of account objects
	private ArrayList <Account> arrData;

	//Table components
	private JTable myTable;
	private MyTableModel tm;
	private JScrollPane myPane;
	
		
	//Form panel components
	private JLabel nameLabel = new JLabel("Name");
    private JTextField nameField = new JTextField(10);
	private JLabel accountNoLabel = new JLabel("Account Number");
	private JTextField accountNoField= new JTextField(10);
	private JLabel balanceLabel = new JLabel("Balance");
	private JTextField balanceField= new JTextField(10);
	private JButton addButton = new JButton("Add Account");
	private JPanel formPnl = new JPanel(); 
	private JPanel tblPnl = new JPanel(); 
	
	//Thread panel
	private JPanel threadPnl = new JPanel();
	private JTextArea threadOutput = new JTextArea();
	private JButton threadButton = new JButton("Thread simulation");
	
	//File variables
	private File file = new File("accounts.dat");	
	private String currentDirectory;
	
	public BankAccountGUI(){  
		// Setting up menu
		setUpMenu();

		//create array of account objects and pass to custom TableModel
		arrData = new ArrayList<Account>();
		tm = new MyTableModel(arrData);		
		myTable = new JTable(tm);
		
		myPane = new JScrollPane(myTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		myTable.setSelectionForeground(Color.white);
		myTable.setSelectionBackground(Color.red);
		myTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//create form panel
		 createFormPanel();
		 
		//Event Listeners

		 //Add Account Event
		 addButton.addActionListener(new ActionListener() {
			 Account acc;
				@Override
				public void actionPerformed(ActionEvent evt) {
					
					try {
						if (nameField.getText().isEmpty() || accountNoField.getText().isEmpty() || balanceField.getText().isEmpty() )
							JOptionPane.showMessageDialog(BankAccountGUI.this, "All textfields must have a value");

						else {
							String name = nameField.getText();
							int accNum = Integer.parseInt(accountNoField.getText());
							int bal = Integer.parseInt(balanceField.getText());
							
							acc = new Account(name, accNum, bal);
						
							arrData.add(acc);
							
							tm.fireTableDataChanged();
							nameField.setText("");
							accountNoField.setText("");
							balanceField.setText("");
						}
							
					}
					catch(NumberFormatException ex) {
						JOptionPane.showMessageDialog(BankAccountGUI.this, "account number and balance must be a number");
					}
					
				}
				
			});

		// Save the data to a file (save event handler)
		// Use provided function: writeDataFile() to save the data into the file
		 fileSaveAs.addActionListener(new ActionListener() {
			 
				@Override
				public void actionPerformed(ActionEvent evt) {
					
					if(arrData.size()<=0) {
						JOptionPane.showMessageDialog(BankAccountGUI.this, "You must add customer records before saving");
					}
					else {

						JFileChooser jfc = new JFileChooser();
					
						jfc.setCurrentDirectory(file);
						

							int retVal = jfc.showSaveDialog(BankAccountGUI.this);
							
							if(retVal == JFileChooser.APPROVE_OPTION) {
								file = jfc.getSelectedFile();
								
								if(file.exists()) {//check to see if file exists
									JOptionPane.showMessageDialog(BankAccountGUI.this, "This file already exists", "Warning",
									        JOptionPane.INFORMATION_MESSAGE);
								}
								else {//if file doesnt exist
								System.out.println(file.getAbsolutePath());
								
								try {
									writeDataFile(file);
								} catch (FileNotFoundException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								
						
								}//end else
							}
							arrData.clear();
							
							tm.fireTableDataChanged();
						}
					 
					
				}
				
			});



		//  Loading the contents of a file into the table (load event handler)
		// Use provided function: readDataFile() to save the data into the file
		 fileLoad.addActionListener(new ActionListener() {
			 
				@Override
				public void actionPerformed(ActionEvent evt) {
					JFileChooser jfc = new JFileChooser();
		 int openVal = jfc.showOpenDialog(BankAccountGUI.this);
			
			if(openVal == JFileChooser.APPROVE_OPTION) {
				file = jfc.getSelectedFile();
				try {
					readDataFile(file);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					}
				}
			});
		 
		// 4 Remove Selected Account Event
		 removeAccount.addActionListener(new ActionListener() {
			 
				@Override
				public void actionPerformed(ActionEvent evt) {
					//get selected item
					int row = myTable.getSelectedRow();
					if(row == -1) {
						JOptionPane.showMessageDialog(BankAccountGUI.this, "you must select a row to delete first");
					}
					else {
					arrData.remove(row);
					tm.fireTableRowsDeleted(arrData.size(), arrData.size());
					}
					
				}
				
			});

		 
		// 5 Thread Button simulation Event
		 threadButton.addActionListener(new ActionListener() {
			 
				@Override
				public void actionPerformed(ActionEvent evt) {
					Scanner scan = new Scanner(System.in);
					
					int x;//amount to deposit/withdraw
					int r;// num of repetitions
					
					//check to see if row is selected
					if (myTable.getSelectionModel().isSelectionEmpty()) {
						System.out.println("You must select a row in the table first");
					}
					else {
					Account ac = arrData.get(myTable.getSelectedRow());
					
					do {
					System.out.println("How many repitions do you want?");
					r = scan.nextInt();
					if(r<=0) {
						System.out.println("repetions must be greater than 0");
					}
					}while(r<=0);
					
					// check to see if funds are available
					do {
					System.out.println("How much do you wish to redraw?");
					x = scan.nextInt();
					if(x>ac.getBalance()) {
						System.out.println("You do not have enough funds to withdraw this amount");
					}
					if(x<=0) {
						System.out.println("you cannot withdraw this amount");
					}
					}while(x>ac.getBalance()|| x<=0);
					
					DepositRunnable dr = new DepositRunnable(ac, x, r); 
					WithdrawalRunnable wr = new WithdrawalRunnable(ac, x, r); 
					//start threads
					dr.start();
					wr.start();
				}
				}
				
			});
		
		
		// Adding menu bar
		
		setJMenuBar(myBar);
		
		//add scrollpane with table
		tblPnl.add(myPane);
		
		//add thread button to thread panel
		threadPnl.add(threadButton);
		threadPnl.add(threadOutput);
		
		//add panel for form
		add(formPnl, BorderLayout.NORTH);
		TitledBorder title = BorderFactory.createTitledBorder("Account Summary");
		tblPnl.setBorder(title);
		add(tblPnl, BorderLayout.CENTER);
		add(threadPnl, BorderLayout.SOUTH);
		
		
		this.setTitle("Bank Account Details");
		this.setVisible(true);
		this.pack();
	} // constructor


	private void setUpMenu() {
		fileLoad = new JMenuItem("Open");
		fileSaveAs = new JMenuItem("Save As");
		
		fileMenu = new JMenu("File");
		fileMenu.add(fileLoad);
		fileMenu.add(fileSaveAs);
		
		removeAccount = new JMenuItem("Remove Account");
		
		recordMenu = new JMenu("Account");
		recordMenu.add(removeAccount);
		
		myBar = new JMenuBar();
		myBar.add(fileMenu);
		myBar.add(recordMenu);

	}

	public void writeDataFile(File f) throws IOException, FileNotFoundException {
		FileOutputStream fo = null;
		ObjectOutputStream os = null;
		try {
			
			fo = new FileOutputStream(f);
			os = new ObjectOutputStream(fo);
			os.writeObject(arrData);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void readDataFile(File f) throws IOException, ClassNotFoundException {
		FileInputStream fi = null;
		ObjectInputStream in = null;
		ArrayList<Account>arr = null;
		try {
			fi = new FileInputStream(f);
			in = new ObjectInputStream(fi);
			
			arr = (ArrayList<Account>)in.readObject();
			
			arrData.clear();
			arrData.addAll(arr);
			
			//update table model
			tm.fireTableDataChanged();



		} catch(EOFException ex) {}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void closeDown() {
			System.exit(0);
	}
	
	public void createFormPanel() {
		    formPnl.setLayout(new GridBagLayout());
			TitledBorder title = BorderFactory.createTitledBorder("Add Account");
			formPnl.setBorder(title);
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor =  GridBagConstraints.WEST;
		    c.insets = new Insets(10,0,0,0);  
		    formPnl.add(nameLabel, c);
		    c.gridx = 1;
		    formPnl.add(nameField, c);
			c.gridx = 0;
		    c.gridy = 1;
		    formPnl.add(accountNoLabel, c);
		    c.gridx = 1;
		    formPnl.add(accountNoField,c);
			c.gridx = 0;
		    c.gridy = 2;
		    formPnl.add(balanceLabel,c);
		    c.gridx = 1;
		    formPnl.add(balanceField,c);
			c.gridx = 0;
		    c.gridy = 3;
		    c.gridwidth = 2; 
		    c.fill = GridBagConstraints.NONE;
		    c.anchor =  GridBagConstraints.CENTER; 
		    formPnl.add(addButton, c);
	   }
	
	public static void main (String args[]){
		new BankAccountGUI();
	} // main
} //class