import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
		// Not used
	}

	@Override
	public void PrintExpensereport() {
		// Not used
	}

	@Override
	public void PrintIncomereport() {
		// Not used
	}

	@Override
	public void PrintIncomereportbyTpe() {
		// Not used
	}

	@Override
	public void PrintExpensebyType() {
		// Not used
	}

	/**
	 * Method responsible for exporting reports using the export report button on the various report pages on the GUI
	 * @param reportTitle title of report as String
	 */
	@Override
	public void exportReport(String reportTitle) {
		try {
			// Creates new csv file with specified title
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh.mm.ss-MM-dd-yyyy");
			String filePath = "";
			String fileName = reportTitle + dateTimeFormatter.format(LocalDateTime.now()); // title + time & date
			String fileType = "csv";
			File reportFile = new File(filePath + fileName + "." + fileType);
			FileOutputStream fileStream = new FileOutputStream(reportFile,true);
			PrintWriter printWriter = new PrintWriter(fileStream);

			reportFile.createNewFile(); // creates file

			if (reportTitle.startsWith("Expense Report")) { // If title starts with Expense Report, the following will be written to file
				printWriter.append("Total Income");
				printWriter.append(",");
				printWriter.append("" + userAtHand.getBalance());
				printWriter.append("\n");
				printWriter.append("Total Expense");
				printWriter.append(",");
				printWriter.append("" + userAtHand.getExpenses());
				printWriter.append("\n");
				printWriter.append("Filtered Expense");
				printWriter.append(",");
				printWriter.append("" + expenseRepPanel.totalFilteredExpenseAmtLbl.getText());
				printWriter.append("\n");
				printWriter.append("Total Savings");
				printWriter.append(",");
				printWriter.append("" + userAtHand.getMonthlySavings());
				printWriter.append("\n");
				printWriter.append("Source,Amount,Frequency\n");

				for (Expense exp : expenseRepPanel.filteredSpending) {
					printWriter.append("" + exp.getSource() + ",");
					printWriter.append("" + exp.getAmount() + ",");
					printWriter.append("" + exp.getFrequency() + "\n");
				}
			} else if (reportTitle.startsWith("Income Report")) { // If title starts with Income Report, the following will be written to file
				printWriter.append("Total Income");
				printWriter.append(",");
				printWriter.append("" + userAtHand.getBalance());
				printWriter.append("\n");
				printWriter.append("Total Expense");
				printWriter.append(",");
				printWriter.append("" + userAtHand.getExpenses());
				printWriter.append("\n");
				printWriter.append("Filtered Income");
				printWriter.append(",");
				printWriter.append("" + incomeRepPanel.totalFilteredIncomeAmtLbl.getText());
				printWriter.append("\n");
				printWriter.append("Total Savings");
				printWriter.append(",");
				printWriter.append("" + userAtHand.getMonthlySavings());
				printWriter.append("\n");
				printWriter.append("Source,Amount,Frequency\n");

				for (Wage wage : incomeRepPanel.filteredIncomeList) {
					printWriter.append("" + wage.getSource() + ",");
					printWriter.append("" + wage.getAmount() + ",");
					printWriter.append("" + wage.getMonth() + "\n");
				}
			} else if (reportTitle.startsWith("Detailed Report")) { // If title starts with Detailed Report, the following will be written to file
				printWriter.append("Total Income");
				printWriter.append(",");
				printWriter.append("" + userAtHand.getBalance());
				printWriter.append("\n");
				printWriter.append("Total Expense");
				printWriter.append(",");
				printWriter.append("" + userAtHand.getExpenses());
				printWriter.append("\n");
				printWriter.append("Total Savings");
				printWriter.append(",");
				printWriter.append("" + userAtHand.getMonthlySavings());
				printWriter.append("\n");
				printWriter.append("Type,Source,Amount,Frequency / Month\n");

				for (Wage wage : userAtHand.getIncome()) {
					printWriter.append("Income,");
					printWriter.append("" + wage.getSource() + ",");
					printWriter.append("" + wage.getAmount() + ",");
					printWriter.append("" + wage.getMonth() + "\n");
				}

				for (Expense exp : userAtHand.getSpending()) {
					printWriter.append("Expense,");
					printWriter.append("" + exp.getSource() + ",");
					printWriter.append("" + exp.getAmount() + ",");
					printWriter.append("" + exp.getFrequency() + "\n");
				}
			}
			printWriter.close(); // file closes and saves.



		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public Currency convertForeignCurrency(Currency C, double amount) {
		return null;
	}

	/**
	 * This is a method that loads information from a csv file.  The file must start with "source,amount,frequency" in the first line as a minimum.
	 * The data will be added to expense objects and loaded into the program on the tables and total locations.
	 * @param filePath the path of the file that data will be taken from
	 * @return true if the program had no issues loading the data.  false if the program encountered a problem.
	 */
	@Override
	public boolean loadExpenseFile(String filePath) {
		// Initialization
		String lineText = "";
		BufferedReader bufferedLineReader = null;
		BufferedReader bufferedTextReader = null;
		File userFile = new File(filePath);
		String source = "", frequency = "", amount = "";

		try {
			int linesInFile = 0;
			bufferedLineReader = new BufferedReader(new FileReader(userFile));
			bufferedTextReader = new BufferedReader(new FileReader(userFile));
			while (bufferedLineReader.readLine() != null) { // count lines in file
				linesInFile++;
			}
			Expense expense;
			for (int lineIndex = 0; lineIndex < linesInFile; lineIndex++) { // loops through entirety of file
				lineText = bufferedTextReader.readLine(); // reads each line and puts it into lineText
				if(lineIndex == 0 && lineText.trim().equals("source,amount,frequency")) { // first line of file must match the String
					System.out.println("File start setup correctly.");
				}
				else if (lineIndex > 0){ // Each line outside of the first will go through this operation
					// resetting data after each line
					source = "";
					frequency = "";
					amount = "";

					// Gets from start of line to the first occurence of the comma and stores it in source
					for(int sourceIndex = 0; sourceIndex < lineText.indexOf(',', 0); sourceIndex++) {
						source += lineText.charAt(sourceIndex);
					}
					// Gets text starting after the first comma up until the next comma and stores it in amount
					for(int amountIndex = lineText.indexOf(',') + 1; amountIndex < lineText.lastIndexOf(','); amountIndex++) {
						amount += lineText.charAt(amountIndex);
					}
					// Gets text starting after the last comma until the end of the line and stores it in frequency
					for(int freqIndex = lineText.lastIndexOf(',') + 1; freqIndex < lineText.length(); freqIndex++) {
						frequency += lineText.charAt(freqIndex);
					}

					// little loop that shows what was read (and will be added) to the program.
					System.out.println("Text read: " + source + "," + amount + "," + frequency);

					// Creates a new wage object from the data
					expense = new Expense(source,Double.parseDouble(amount),Integer.valueOf(frequency));

					// Updates user expenses
					userAtHand.setExpenses(userAtHand.getExpenses() + expense.getAmount());
					// Adds expense object to user's spending ArrayList
					addExpense(expense);

					// Loop that adds items to the type filter dropdown in income reports page, and will not add repeats
					if(expenseRepPanel.typeSelector.getItemCount() > 0) {
						boolean contains = false;
						for (int i = 0; i < expenseRepPanel.typeSelector.getItemCount(); i++) {
							if (expenseRepPanel.typeSelector.getItemAt(i).equals(expense.getSource())) {
								contains = true;
							}
						}
						if (!contains) {
							expenseRepPanel.typeSelector.addItem(expense.getSource());
						}
					} else {
						expenseRepPanel.typeSelector.addItem(expense.getSource());
					}

				}
			}
		} catch (FileNotFoundException e) { // if file is not found, return false.
			return false;
		} catch (IOException e) { // if there is an IO problem (maybe operation interruption)
			return false;
		}
		return true;
	}

	/**
	 * This is a method that loads information from a csv file.  The file must start with "source,amount,month" in the first line as a minimum.
	 * The data will be added to wage objects and loaded into the program on the tables and total locations.
	 * @param filePath the path of the file that data will be taken from
	 * @return true if the program had no issues loading the data.  false if the program encountered a problem.
	 */
	@Override
	public boolean loadIncomeFile(String filePath) {

		// Initialization
		String lineText = "";
		BufferedReader bufferedLineReader = null;
		BufferedReader bufferedTextReader = null;
		File userFile = new File(filePath);
		String source = "", month = "", amount = "";


		try {
			int linesInFile = 0;
			bufferedLineReader = new BufferedReader(new FileReader(userFile));
			bufferedTextReader = new BufferedReader(new FileReader(userFile));
			while (bufferedLineReader.readLine() != null) { // count lines in file
				linesInFile++;
			}
			Wage wage;
			for (int lineIndex = 0; lineIndex < linesInFile; lineIndex++) { // loops through entirety of file
				lineText = bufferedTextReader.readLine(); // reads each line and puts it into lineText
				if( lineIndex == 0 && lineText.equalsIgnoreCase("source,amount,month")) { // first line of file must match the String
					System.out.println("File start setup correctly.");
				}
				else { // Each line outside of the first will go through this operation
						// resetting data after each line
						source = "";
						month = "";
						amount = "";

						// Gets from start of line to the first occurence of the comma and stores it in source
						for(int sourceIndex = 0; sourceIndex < lineText.indexOf(',', 0); sourceIndex++) {
							source += lineText.charAt(sourceIndex);
						}
						// Gets text starting after the first comma up until the next comma and stores it in amount
						for(int amountindex = lineText.indexOf(',') + 1; amountindex < lineText.lastIndexOf(','); amountindex++) {
							amount += lineText.charAt(amountindex);
						}
						// Gets text starting after the last comma until the end of the line and stores it in month
						for(int monthIndex = lineText.lastIndexOf(',') + 1; monthIndex < lineText.length(); monthIndex++) {
							month += lineText.charAt(monthIndex);
						}

						// little loop that shows what was read (and will be added) to the program.
						System.out.println("Text read: " + source + "," + amount + "," + month);

						// Creates a new wage object from the data
						wage = new Wage(source,Double.parseDouble(amount),month);

						// Updates user balance
						userAtHand.setBalance(userAtHand.getBalance() + wage.getAmount());
						// Adds wage object to user's wage ArrayList
						addMonthlyIncome(wage);

						// Loop that adds items to the type filter dropdown in income reports page, and will not add repeats
						if(incomeRepPanel.typeSelector.getItemCount() > 0) {
							boolean contains = false;
							for (int i = 0; i < incomeRepPanel.typeSelector.getItemCount(); i++) {
								if (incomeRepPanel.typeSelector.getItemAt(i).equals(wage.getSource())) {
									contains = true;
								}
							}
							if (!contains) {
								incomeRepPanel.typeSelector.addItem(wage.getSource());
							}
							} else {
							incomeRepPanel.typeSelector.addItem(wage.getSource());
						}

				}
			}
		} catch (FileNotFoundException e) { // if file is not found, return false.
			return false;
		} catch (IOException e) { // if there is an IO problem (maybe operation interruption)
			return false;
		}
		return true;
	}

	@Override
	public int whenCanIBuy(String itemname, double price) {
		return 0;
	}

	@Override
	public void updateMonthlySavings() {
		userAtHand.setMonthlySavings(userAtHand.getBalance() - userAtHand.getExpenses());
	}

	
	/**
	 * Produces a filtered expense ArrayList based on the user's frequency of the expense
	 * @param exp Base Expense ArrayList
	 * @param freq Frequency as String
	 * @return New ArrayList of filtered data
	 */
	static ArrayList<Expense> filterExpensesFreq(ArrayList<Expense> exp, String freq) {
		ArrayList<Expense> filteredExpensesFreq = new ArrayList<>();
		for(Expense ex : exp) {
			if(String.valueOf(ex.getFrequency()).equals(freq)) {
				filteredExpensesFreq.add(ex);
			}
		}
		return filteredExpensesFreq;
	}
	
	/**
	 * Produces a filtered expense ArrayList based on the user's source of the expense
	 * @param exp Base Expense ArrayList
	 * @param source Source as String
	 * @return New ArrayList of filtered data
	 */
	static ArrayList<Expense> filterExpensesSource(ArrayList<Expense> exp, String source) {
		ArrayList<Expense> filteredExpensesSource = new ArrayList<>();
		for(Expense ex : exp) {
			if(ex.getSource().equals(source)) {
				filteredExpensesSource.add(ex);
			}
		}
		return filteredExpensesSource;
	}
	
	/**
	 * Produces a filtered income ArrayList based on the user's month of income
	 * @param wage Base Wage ArrayList
	 * @param month Month as String
	 * @return New ArrayList of filtered data
	 */
	static ArrayList<Wage> filterIncomesMonth(ArrayList<Wage> wage, String month) {
		ArrayList<Wage> filteredIncomesMonth = new ArrayList<>();
		for(Wage w : wage) {
			if(w.getMonth().equals(month)) {
				filteredIncomesMonth.add(w);
			}
		}
		return filteredIncomesMonth;
	}
	
	/**
	 * Method responsible for producing a filtered income ArrayList based on the user's source of the income
	 * @param wage Base Wage ArrayList
	 * @param source Source as String
	 * @return New ArrayList of filtered data
	 */
	static ArrayList<Wage> filterIncomesSource(ArrayList<Wage> wage, String source) {
		ArrayList<Wage> filteredIncomesSource = new ArrayList<>();
		for(Wage w : wage) {
			if(w.getSource().equals(source)) {
				filteredIncomesSource.add(w);
			}
		}
		return filteredIncomesSource;
	}

	/**
	 * Updates income table values based on adding of values through import or the add tool.
	 */
	public void updateIncomeTable() {

		// Resetting row count - setting it to the income array size
		incomeRepPanel.model.setNumRows(0);

		for(int j = 0; j < userAtHand.getIncome().size(); j++ ) {
			incomeRepPanel.model.addRow(new Object[]{});
		}

		userAtHand.setBalance(0.00f);

		// Updating what is displayed on the table with new wage data
		int i = 0;
		for(Wage wage : userAtHand.getIncome()) {
			userAtHand.setBalance(userAtHand.getBalance() + wage.amount);
			incomeRepPanel.incomeTable.setValueAt(wage.getSource(), i, 0);
			incomeRepPanel.incomeTable.setValueAt(String.format("$%.2f",wage.getAmount()), i, 1);
			incomeRepPanel.incomeTable.setValueAt(wage.getMonth(), i, 2);
			++i;
		}
	}

	/**
	 * Updates detailed table values based on adding of values through import or the add tool.
	 */
	public void updateDetailedTable() {
		// clears detailed table
		detailedRepPanel.model.setNumRows(0);

		// re-adds rows based on number of wage objects
		for(int j = 0; j < userAtHand.getIncome().size() + userAtHand.getSpending().size(); j++ ) {
			detailedRepPanel.model.addRow(new Object[]{});
		}

		int i = 0;
		for(Wage wage : userAtHand.getIncome()) { // repopulates table with wage data
			detailedRepPanel.detailedTable.setValueAt("Income", i, 0);
			detailedRepPanel.detailedTable.setValueAt(wage.getSource(), i, 1);
			detailedRepPanel.detailedTable.setValueAt(String.format("$%.2f",wage.getAmount()), i, 2);
			detailedRepPanel.detailedTable.setValueAt(wage.getMonth(), i, 3);
			++i;
		}

		for(Expense expense : userAtHand.getSpending()) { // repopulates table with expense data
			detailedRepPanel.detailedTable.setValueAt("Expense", i, 0);
			detailedRepPanel.detailedTable.setValueAt(expense.getSource(), i, 1);
			detailedRepPanel.detailedTable.setValueAt(String.format("$%.2f",expense.getAmount()), i, 2);
			detailedRepPanel.detailedTable.setValueAt(expense.getFrequency(), i, 3);
			++i;
		}

	}

	/**
	 * Updates expense table values based on adding of values through import or the add tool.
	 */
	public void updateExpenseTable() {

		// Resetting row count - setting it to the income array size
		expenseRepPanel.model.setNumRows(0);

		for(int j = 0; j < userAtHand.getSpending().size(); j++ ) {
			expenseRepPanel.model.addRow(new Object[]{});
		}

		// Updating what is displayed on the table with new wage data
		userAtHand.setExpenses(0.00f);
		int i = 0;
		for(Expense expense : userAtHand.getSpending()) {
			userAtHand.setExpenses(userAtHand.getExpenses() + expense.amount);
			expenseRepPanel.spendingTable.setValueAt(expense.getSource(), i, 0);
			expenseRepPanel.spendingTable.setValueAt(String.format("$%.2f",expense.getAmount()), i, 1);
			expenseRepPanel.spendingTable.setValueAt(expense.getFrequency(), i, 2);
			++i;
		}
	}

	/**
	 * Updates income values through import or the add tool
	 * Values on the home page, income page, and expense page will be updated with this.
	 */
	public void updateIncomeValues() {
		userAtHand.setExpenses(getExpense(userAtHand.getSpending()));
		userAtHand.setBalance(getTotalIncome(userAtHand.getIncome()));
		updateMonthlySavings();
		incomeRepPanel.totalIncomeAmtLbl.setText(String.format("$%.2f",getTotalIncome(userAtHand.getIncome())));
		homePanel.totalIncomeAmtLbl.setText("$" + String.format("%.2f",userAtHand.getBalance()));
		homePanel.totalSavingsAmtLbl.setText("$" + String.format("%.2f", userAtHand.getMonthlySavings()));
	}

	/**
	 * Updates expense values through import or the add tool
	 * Values on the home page, income page, and expense page will be updated with this.
	 */
	public void updateExpenseValues() {
		userAtHand.setExpenses(getExpense(userAtHand.getSpending()));
		userAtHand.setBalance(getTotalIncome(userAtHand.getIncome()));
		updateMonthlySavings();
		expenseRepPanel.totalExpenseAmtLbl.setText(String.format("$%.2f",getExpense(userAtHand.getSpending())));
		homePanel.totalExpensesAmtLbl.setText("$" + String.format("%.2f",getExpense(userAtHand.getSpending())));
		homePanel.totalSavingsAmtLbl.setText("$" + String.format("%.2f", userAtHand.monthlysavings));
	}

	/**
	 * Gets the total income for a user based on an inputted ArrayList of type wage.
	 * @param wage user's income ArrayList
	 * @return user's total income
	 */
	public double getTotalIncome(ArrayList<Wage> wage) {
		double sum = 0.00f;
		for(Wage userWage : wage) {
			sum += userWage.getAmount();
		}
		return sum;
	}

	/**
	 * Obtains sources for the income combobox selector
	 * @param updatedWage Wage ArrayList
	 * @return ArrayList of Strings of sources
	 */
	public ArrayList<String> getIncomeSources(ArrayList<Wage> updatedWage) {
		ArrayList<String> sources = new ArrayList<>();

		for(Wage wage : updatedWage) {
			sources.add(wage.getSource());
		}
		return sources;
	}

	/**
	 * Gets expense sources for the expense combobox selector
	 * @param expenses ArrayList of Expenses
	 * @return ArrayList of Sources
	 */
	public ArrayList<String> getExpenseSources(ArrayList<Expense> expenses) {
		ArrayList<String> sources = new ArrayList<>();
		for(Expense exp : expenses) {
			sources.add(exp.getSource());
		}
		return sources;
	}

	/**
	 * Gets total expenses
	 * @param Ex ArrayList of Spending (expense)
	 * @return Sum of Expenses
	 */
	public double getExpense(ArrayList<Expense> Ex) {
		double sum = 0.00f;
		for(Expense ex : Ex) {
			sum += ex.getAmount();
		}
		return sum;
	}

}
