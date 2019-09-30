package bankAccount;

public class DepositRunnable extends Thread {

	Account account;
	double amount;
	int repetitions;
	
	public DepositRunnable(Account acc, double am, int rep) {
		this.account = acc;
		this.amount = am;
		this.repetitions = rep;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(int i=0; i<this.repetitions; i++) {
			this.account.deposit(amount);
			
			try {
	            Thread.sleep((int)(Math.random() * 100));
	        } catch (InterruptedException e) { }
		}

	}
	 public static void main(String[] args) {
	 
	 }

}
