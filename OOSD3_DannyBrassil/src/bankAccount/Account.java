package bankAccount;

import java.io.Serializable;
import java.text.NumberFormat;

public class Account implements Serializable {

   private int acctNumber;
   private double balance;
   private String name;
 
   public Account() {
	   
   }
 
   //-----------------------------------------------------------------
   //  Sets up the account by defining its owner, account number,
   //  and initial balance.
   //-----------------------------------------------------------------
   public Account(String owner, int accountNo, double bal)
   {
      name = owner;
      acctNumber = accountNo;
      balance = bal;

   }
 
   //-----------------------------------------------------------------
   //  Deposits the specified amount into the account. Returns the
   //  new balance.
   //-----------------------------------------------------------------
   public double deposit(double amount)
   {
	  System.out.println("Depositing: " + amount);
      balance = balance + amount;
      System.out.println("New Balance: " + balance);
      return balance;
   }

   //-----------------------------------------------------------------
   //  Withdraws the specified amount from the account.
   //  Returns the new balance.
   //-----------------------------------------------------------------
   public double withdraw(double amount)
   {
	  System.out.println("Withdrawing: " + amount);
      balance = balance - amount;
      System.out.println("New Balance: " + balance);
      return balance;
   }
 
 
   //-----------------------------------------------------------------
   //  Returns the current balance of the account.
   //-----------------------------------------------------------------
   public double getBalance()
   {
      return balance;
   }
   public String getName()
   {
      return name;
   }
   public int getAccNumber()
   {
      return acctNumber;
   }
 
   
   public void setAcctNumber(int acctNumber) {
	this.acctNumber = acctNumber;
   }

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setName(String name) {
		this.name = name;
	}
   //-----------------------------------------------------------------
   //  Returns a one-line description of the account as a string.
   //-----------------------------------------------------------------
   public String toString()
   {
      NumberFormat fmt = NumberFormat.getCurrencyInstance();
      return (acctNumber + "\t" + name + "\t" + fmt.format(balance));
   }
}