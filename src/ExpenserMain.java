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

	@Override
	public boolean loadExpenseFile(String filePath) {
		return false;
	}

	@Override
	public boolean loadIncomeFile(String filePath) {
		String lineText = "";
		BufferedReader bufferedLineReader = null;
		BufferedReader bufferedTextReader = null;
		File userFile = new File(filePath);
		String source = "", month = "", amount = "";
		try {
			int linesInFile = 0;
			bufferedLineReader = new BufferedReader(new FileReader(userFile));
			bufferedTextReader = new BufferedReader(new FileReader(userFile));
			while (bufferedLineReader.readLine() != null) {
				linesInFile++;
			}
			Wage wage;
			for (int lineIndex = 0; lineIndex < linesInFile; lineIndex++) {
				lineText = bufferedTextReader.readLine();
				if( lineIndex == 0 && lineText.equalsIgnoreCase("source,amount,month")) {
					System.out.println("File start setup correctly.");
				} else {
						source = "";
						month = "";
						amount = "";
						for(int sourceIndex = 0; sourceIndex < lineText.indexOf(',', 0); sourceIndex++) {
							source += lineText.charAt(sourceIndex);
						}
						for(int amountindex = lineText.indexOf(',') + 1; amountindex < lineText.lastIndexOf(','); amountindex++) {
							amount += lineText.charAt(amountindex);
						}
						for(int monthIndex = lineText.lastIndexOf(',') + 1; monthIndex < lineText.length(); monthIndex++) {
							month += lineText.charAt(monthIndex);
						}
						System.out.println("Text read: " + source + "," + amount + "," + month);
						wage = new Wage(source,Double.parseDouble(amount),month);
						userAtHand.setBalance(userAtHand.getBalance() + wage.getAmount());
						addMonthlyIncome(wage);
						incomeRepPanel.typeSelector.addItem(source);

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
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
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
	 * Method responsible for producing a filtered expense ArrayList based on the user's frequency of the expense
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
	 * Method responsible for producing a filtered expense ArrayList based on the user's source of the expense
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
	 * Method responsible for producing a filtered income ArrayList based on the user's month of income
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
	 * A method that will update income table values based on adding of values through import or the add tool.
	 */
	public void updateIncomeTable() {

		// Resetting row count - setting it to the income array size
		incomeRepPanel.model.setNumRows(0);

		for(int j = 0; j < userAtHand.getIncome().size(); j++ ) {
			incomeRepPanel.model.addRow(new Object[]{});
		}

		// Updating what is displayed on the table
		int i = 0;
		for(Wage wage : userAtHand.getIncome()) {
			incomeRepPanel.incomeTable.setValueAt(wage.getSource(), i, 0);
			incomeRepPanel.incomeTable.setValueAt(String.format("$%.2f",wage.getAmount()), i, 1);
			incomeRepPanel.incomeTable.setValueAt(wage.getMonth(), i, 2);
			++i;
		}
	}

	/**
	 * A method that will update detailed table values based on adding of values through import or the add tool.
	 */
	public void updateDetailedTable() {

	}

	/**
	 * A method that will update expense table values based on adding of values through import or the add tool.
	 */
	public void updateExpenseTable() {

	}

	/**
	 * A method that will update values based on adding of values through import or the add tool.
	 * Values on the home page, income page, and expense page will be updated with this.
	 */
	public void updateIncomeValues() {
		incomeRepPanel.totalIncomeAmtLbl.setText(String.format("$%.2f",getTotalIncome(userAtHand.getIncome())));
		updateMonthlySavings();
		homePanel.totalIncomeAmtLbl.setText("$" + String.format("%.2f",userAtHand.getBalance()));
		homePanel.totalSavingsAmtLbl.setText("$" + String.format("%.2f", userAtHand.getMonthlySavings()));
	}

	public void updateExpenseValues(Expense expense) {

	}

	/**
	 * A method that gets the total income for a user based on an inputted ArrayList of type wage.
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
	 * Method responsible for obtaining sources for the combobox selector
	 * @param updatedWage Wage ArrayList
	 * @return ArrayList of Strings of sources
	 */
	public ArrayList<String> getSources(ArrayList<Wage> updatedWage) {
		ArrayList<String> sources = new ArrayList<>();

		for(Wage wage : updatedWage) {
			sources.add(wage.getSource());
		}
		return sources;
	}

}
