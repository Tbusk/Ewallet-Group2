
public class Wage {
	String source;
	double amount;
	String Month;
	
	/**
	 * Accepts the parameter values as inputs and updating the object's values
	 * @param source user's source of wage
	 * @param amount user's amount of wage
	 * @param Month user's month in which they receive the wage
	 */
	Wage(String source, double amount, String Month) {
		this.source = source;
		this.amount = amount;
		this.Month = Month;
	}
	
	/**
	 * Method responsible for getting the wage's source
	 * @param source user's source of the wage
	 */
	public void getSource(String source) {
		this.source = source;
	}
	
	/**
	 * Method responsible for getting the wage's amount
	 * @param amount user's amount of the wage
	 */
	public void getAmount(double amount) {
		this.amount = amount;
	}
	
	/**
	 * Method responsible for getting the month in which the wage was received
	 * @param Month the month in which the user received the wage
	 */
	public void getMonth(String Month) {
		this.Month = Month;
	}
}
