
/**
 * A class for calculating the user's expenses
 */
public class Expense {
	String source;
	double amount;
	int yearlyfrequency; //1 for 1 time or once a year, 12 for monthly or or 24 for biweekly
	
	/**
	 * Accepts the parameter values as inputs and updating the object's values
	 * @param source user's source of expense
	 * @param amount user's amount of expense
	 * @param yearlyfrequency user's frequency of expense
	 */
	Expense(String source, double amount, int yearlyfrequency) {
		this.source = source;
		this.amount = amount;
		this.yearlyfrequency = yearlyfrequency;
	}
	
	/**
	 * Method responsible for getting the source of the expense
	 * @param source user's source of the expense
	 */
	public String getSource() {
		return this.source;
	}
	
	/**
	 * Method responsible for getting the amount of the expense
	 * @param amount user's amount of expense
	 */
	public double getAmount() {
		return this.amount;
	}
	
	/**
	 * Method responsible for getting the frequency of the expense
	 * @param yearlyfrequency user's frequency of expense
	 */
	public int getFrequency() {
		return this.yearlyfrequency;
	}

}
