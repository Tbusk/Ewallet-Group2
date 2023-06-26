public class ExpenserMain implements Expenser {
	public User userAtHand = null;

	// Add Expense feature
	@Override
	public void addExpense (Expense Ex) {
		userAtHand.addExpense(Ex);
	}

	// Add Monthly Income feature
	@Override
	public void addMonthlyIncome (Wage W) {
		userAtHand.addMonthlyIncome(W);
	}

	@Override
	public void PrintFullreport() {

	}

	@Override
	public void PrintExpensereport() {

	}

	@Override
	public void PrintIncomereport() {

	}

	@Override
	public void PrintIncomereportbyTpe() {

	}

	@Override
	public void PrintExpensebyType() {

	}

	@Override
	public void exportReport(String reportTitle) {

	}

	@Override
	public Currency convertForeignCurrency(Currency C, double amount) {
		return null;
	}

	@Override
	public boolean loadExpenseFile(String filePath) {
		return false;
	}

	@Override
	public boolean loadIncomeFile(String filePath) {
		return false;
	}

	@Override
	public int whenCanIBuy(String itemname, double price) {
		return 0;
	}

	@Override
	public void updateMonthlySavings() {
		userAtHand.setMonthlySavings(userAtHand.getBalance() - userAtHand.getExpenses());
	}

}
