package bankAccount;

public class WithdrawalRunnable extends Thread {

	Account account;
	double amount;
	int repetitions;
	
	public WithdrawalRunnable(Account acc, double am, int rep) {
		this.account = acc;
		this.amount = am;
		this.repetitions = rep;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(int i=0; i<this.repetitions; i++) {
			this.account.withdraw(amount);
			
			try {
            Thread.sleep((int)(Math.random() * 100));
        } catch (InterruptedException e) { }
			
		}//end for
		

	}
	public static void main(String[] args) {
		 
	 }

}
