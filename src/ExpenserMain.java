import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
			String filePath = "src/";
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
