import java.util.ArrayList;

/**
 * A class for creating user objects, which contains currency rates, income, expenses, balance, and monthly savings
 */
public class User {
	private ArrayList <Currency>currencyRates;
	private ArrayList <Wage>Income = new ArrayList<>();  // user income sources that user can record or view or search by type or month
	private ArrayList <Expense>Spending = new ArrayList<>(); //user's expenses
	String username;
	String pwd;
	//current total income - total 
	double balance;
	// possible monthly savings, calculated using monthly income (most recent) assuming the data we have is for one year, and monthly and biweekly expenses, here you can assume yearly expenses that are recorded have already been paid. 
	double monthlysavings;
	double expenses;
	//should add constructor(s)

	/**
	 * When creating a User object, the user needs to provide a username and password.
	 * @param username user's desired username as String
	 * @param password user's desired password as String
	 */
	User(String username, String password){
		this.username = username;
		this.pwd = password;
	}

	/**
	 * Method responsible for getting user's username
	 * @return user's username as String
	 */
	protected String getUsername() {
		return this.username;
	}

	/**
	 * Method responsible for getting user's password
	 * @return user's password as String
	 */
	protected String getPwd() {
		return this.pwd;
	}

	/**
	 * Method responsible for updating the user's username
	 * @param username user's desired username
	 */
	protected void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Method responsible for updating the user's password
	 * @param password user's desired password
	 */
	protected void setPwd(String password) {
		this.pwd = password;
	}
	
	/**
	 * Method responsible for adding expense to the arraylist
	 * @param Ex user's calculated expense
	 */
	protected void addExpense(Expense Ex) {
		Spending.add(Ex);
	}

	/**
	 * Method responsible for adding a monthly income to the Income ArrayList
	 * @param W Wage object
	 */
	protected void addMonthlyIncome(Wage W) {
		Income.add(W);
	}

	/**
	 * Method responsible for adding an expense object to Spending ArrayList
	 * @param Ex Expense object
	 */

	/**
	 * Method responsible for making the ArrayList Income accessible to classes that have access to a user object.
	 * @return ArrayList of Wage objects.
	 */
	protected ArrayList<Wage> getIncome() {
		return this.Income;
	}

	/**
	 * Method responsible for making the ArrayList Spending accessible to classes that have access to a user object.
	 * @return ArrayList of Expense objects
	 */
	protected ArrayList<Expense> getSpending() {
		return this.Spending;
	}

	/**
	 * Method responsible for updating balance (income) of user
	 * @param balance updated income of user as double
	 */
	protected void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * Method responsible for getting balance of user
	 * @return current income of user as a double
	 */
	protected double getBalance(){
		return this.balance;
	}

	/**
	 * Method responsible for updating expenses of user
	 * @param expenses expenses of user as double.
	 */
	protected void setExpenses(double expenses) {
		this.expenses = expenses;
	}

	/**
	 * Method responsible for getting expenses of user
	 * @return expenses of user as double
	 */
	protected double getExpenses() {
		return this.expenses;
	}

	/**
	 * Method responsible for getting savings of user
	 * @return monthly savings of user as double
	 */
	protected double getMonthlySavings() {
		return this.monthlysavings;
	}

	/**
	 * Method responsible for updating monthly savings of user
	 * @param monthlySavings new value of monthly savings of user
	 */
	protected void setMonthlySavings(double monthlySavings) {
		this.monthlysavings = monthlySavings;
	}
}

