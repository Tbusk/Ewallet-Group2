import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EWalletApp {
	//this is the app class, has the GUI and create one object of your expense calculator class. The expense calculator class is the implementation of the Expenser interface
	private ArrayList<User> AllData;

	/**
	 * Method responsible for creating a user account.  It adds username and password to a UserCredentials.csv file.
	 * @param username user's desired username
	 * @param password user's desired password
	 */
	public void CreateUser(String username, String password) {

		if (!checkForRepeatUsernames(username) && isComplexPassword(password)) { // If there are no repeat usernames and password is valid, a new account will be created and stored.
			User user = new User(username, password);
			AllData.add(user);

			try { // Writes username and password to file in csv format
				FileOutputStream fileOutputStream = new FileOutputStream("src//UserCredentials.csv", true);
				PrintWriter printWriter = new PrintWriter(fileOutputStream);
				printWriter.append(username);
				printWriter.append(",");
				printWriter.append(password);
				printWriter.append(",\n");
				printWriter.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method that checks to see if a username exists in UserCredentials.csv
	 * @param username user's desired username as String
	 * @return if username is found, returns true.  Otherwise, it will return false.
	 */
	public boolean checkForRepeatUsernames(String username) {
		try {
			FileInputStream fileInputStream = new FileInputStream("src\\UserCredentials.csv");
			Scanner scnr = new Scanner(fileInputStream);

			Path path = Paths.get("src/UserCredentials.csv");
			long lines = Files.lines(path).count(); // Counts lines in UserCredentials.csv
			String textAtLine;
			String readUsername;
			if(lines < 1) { // Checks if file is empty
				System.out.println("There is no data in file.");
			} else {
				for (int i = 0; i < lines; i++) { // Goes line by line throughout the file
					textAtLine = scnr.nextLine();
					readUsername = "";
					for (int j = 0; j < textAtLine.length(); j++) { // Collects characters until a comma is reached
						if (textAtLine.charAt(j) != ',') {
							readUsername += textAtLine.charAt(j);
						} else {
							break;
						}
					}
					if (username.equals(readUsername)) { // Checks if username at line is equal to the user's desired username

						System.out.println("Username already exists.");
						return true;

					}
				}
			}

		} catch (FileNotFoundException e) {

			System.out.println("The file UserCredentials.csv was not found.");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isComplexPassword(String password) {

		try {

			/* Regex Breakdown
			 * Passwords must be 8 - 20 characters consisting of at least one digit, symbol, uppercase, lowercase, and it contains no whitespaces.
			 * ^ - start of String
			 * $ - end of String
			 * (?=.*[0-9]) - looks through entire string for at least one occurence of 0-9.
			 * (?=.*[a-z]) - looks through entire string for at least one occurence of a-z.
			 * (?=.*[A-Z]) - looks through entire string for at least one occurence of A-Z.
			 * (?=.*[!@#$%^&+=] - looks through entire string for at least one occurence of !,@,#,$,%,^,&,+, or =.
			 * {8-20} - minimum of 8 characters, max of 20.
			 */
			String passwordRegEx = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,20}$";
			Pattern pattern = Pattern.compile(passwordRegEx);
			Matcher matcher = pattern.matcher(password);
			if(!matcher.find()) {
				System.out.println("Password is not valid.");
			} else {
				System.out.println("Password is valid.");
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	public static void main(String[] args) {
		appFrame app = new appFrame();

	}

}

/**
 * appFrame is a class that makes up ewallet's GUI frame.  It contains a JMenu with options to navigate
 * between the different features of the app by utilizing a CardLayout while clearing and setting a panel when navigating between the pages.
 */
class appFrame extends JFrame {

	static User user; // temporary - until login is set up.
	ExpenserMain expenserMain;
	JMenuBar navMenuBar;
	JMenu navMenu;
	JMenuItem homeNav, addItemNav, importNav, estimateNav, incomeReportNav, expenseReportNav, detailedReportNav, loginNav, createAccNav; // different pages

	appFrame(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(900,700));
		this.setTitle("EWallet Application");

		user = new User("Kevin", "Abc!1234"); // temporary solution until login is set up
		expenserMain = new ExpenserMain();
		expenserMain.userAtHand = user;
		
		loginPanel lPanel = new loginPanel();
		homePanel hPanel = new homePanel();
		addItemPanel addItmPanel = new addItemPanel();
		importPanel impPanel = new importPanel();
		estimatePanel estPanel = new estimatePanel();
		incomeRepPanel incRepPanel = new incomeRepPanel();
		expenseRepPanel expRepPanel = new expenseRepPanel();
		detailedRepPanel detRepPanel = new detailedRepPanel();
		getContentPane().add(hPanel); // setting default page
		navMenuBar = new JMenuBar();
		navMenu = new JMenu("<html><p style='margin-left:20'>Menu"); // Menu
		homeNav = new JMenuItem("<html><p style='margin-left:15'>Home"); // Home Page
		homeNav.addMouseListener(new MouseAdapter() {
			// once the page is clicked and released, it will be displayed, while discarding previous jpanel instead of storing it
			@Override
			public void mousePressed(MouseEvent e) {

				super.mouseClicked(e);
				getContentPane().removeAll();
				getContentPane().add(hPanel);
				revalidate();
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				revalidate();
				repaint();
			}
		});
		addItemNav = new JMenuItem("<html><p style='margin-left:15'>Add Item"); // Add Items Page
		addItemNav.addMouseListener(new MouseAdapter() {
			// once the page is clicked and released, it will be displayed while discarding previous JPanel instead of storing it
			@Override
			public void mousePressed(MouseEvent e) {

				super.mouseClicked(e);
				getContentPane().removeAll();
				getContentPane().add(addItmPanel);
				revalidate();
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				revalidate();
				repaint();
			}
		});

		importNav = new JMenuItem("<html><p style='margin-left:15'>Import Tool"); // Import Page
		importNav.addMouseListener(new MouseAdapter() {
			// once the page is clicked and released, it will be displayed, while discarding previous jpanel instead of storing it
			@Override
			public void mousePressed(MouseEvent e) {
				super.mouseClicked(e);
				getContentPane().removeAll();
				revalidate();
				repaint();
				getContentPane().add(impPanel);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				revalidate();
				repaint();
			}
		});
		estimateNav = new JMenuItem("<html><p style='margin-left:15'>Estimate Tool"); // Estimate Page
		estimateNav.addMouseListener(new MouseAdapter() {
			// once the page is clicked and released, it will be displayed, while discarding previous jpanel instead of storing it
			@Override
			public void mousePressed(MouseEvent e) {

				super.mouseClicked(e);
				getContentPane().removeAll();
				getContentPane().add(estPanel);
				revalidate();
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				revalidate();
				repaint();
			}
		});
		incomeReportNav = new JMenuItem("<html><p style='margin-left:15'>Income Report"); // Income Report Page
		incomeReportNav.addMouseListener(new MouseAdapter() {
			// once the page is clicked and released, it will be displayed, while discarding previous jpanel instead of storing it
			@Override
			public void mousePressed(MouseEvent e) {

				super.mouseClicked(e);
				getContentPane().removeAll();
				getContentPane().add(incRepPanel);
				revalidate();
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				revalidate();
				repaint();
			}
		});
		expenseReportNav = new JMenuItem("<html><p style='margin-left:15'>Expense Report"); // Expense Report Page
		expenseReportNav.addMouseListener(new MouseAdapter() {
			// once the page is clicked and released, it will be displayed, while discarding previous jpanel instead of storing it
			@Override
			public void mousePressed(MouseEvent e) {

				super.mouseClicked(e);
				getContentPane().removeAll();
				getContentPane().add(expRepPanel);
				revalidate();
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				revalidate();
				repaint();
			}
		});
		detailedReportNav = new JMenuItem("<html><p style='margin-left:15'>Detailed Report"); // Detailed Report Page
		detailedReportNav.addMouseListener(new MouseAdapter() {
			// once the page is clicked and released, it will be displayed, while discarding previous jpanel instead of storing it
			@Override
			public void mousePressed(MouseEvent e) {

				super.mouseClicked(e);
				getContentPane().removeAll();
				getContentPane().add(detRepPanel);
				revalidate();
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				revalidate();
				repaint();
			}
		});
		
		loginNav = new JMenuItem("<html><p style='margin-left:15'>Login"); // Add Items Page
		loginNav.addMouseListener(new MouseAdapter() {
			// once the page is clicked and released, it will be displayed while discarding previous JPanel instead of storing it
			@Override
			public void mousePressed(MouseEvent e) {

				super.mouseClicked(e);
				getContentPane().removeAll();
				getContentPane().add(lPanel);
				revalidate();
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				revalidate();
				repaint();
			}
		});
		
		createAccNav = new JMenuItem("<html><p style='margin-left:15'>Create Account"); // Add Items Page
		createAccNav.addMouseListener(new MouseAdapter() {
			// once the page is clicked and released, it will be displayed while discarding previous JPanel instead of storing it
			@Override
			public void mousePressed(MouseEvent e) {

				super.mouseClicked(e);
				getContentPane().removeAll();
				getContentPane().add(lPanel);
				revalidate();
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				revalidate();
				repaint();
			}
		});

		// Updating font size of menu and menu items
		navMenu.setFont(new Font(null, Font.PLAIN, 24));
		homeNav.setFont(new Font(null, Font.PLAIN, 20));
		addItemNav.setFont(new Font(null, Font.PLAIN, 20));
		importNav.setFont(new Font(null, Font.PLAIN, 20));
		estimateNav.setFont(new Font(null, Font.PLAIN, 20));
		incomeReportNav.setFont(new Font(null, Font.PLAIN, 20));
		expenseReportNav.setFont(new Font(null, Font.PLAIN, 20));
		detailedReportNav.setFont(new Font(null, Font.PLAIN, 20));
		loginNav.setFont(new Font(null, Font.PLAIN, 20));
		createAccNav.setFont(new Font(null, Font.PLAIN, 20));

		// Adding items to the navigation menu
		navMenu.add(homeNav);
		navMenu.add(addItemNav);
		navMenu.add(importNav);
		navMenu.add(estimateNav);
		navMenu.add(incomeReportNav);
		navMenu.add(expenseReportNav);
		navMenu.add(detailedReportNav);
		navMenu.add(loginNav);
		navMenu.add(createAccNav);
		navMenuBar.add(navMenu);
		

		this.setJMenuBar(navMenuBar);
		this.setLayout(new CardLayout());
		this.pack();
		this.setVisible(true);
	}
}

/**
 * homePanel is a class that makes up ewallet's GUI home page.  It contains basic total information like total income,
 * total expenses, and savings.
 */
class homePanel extends JPanel {

	JLabel summaryTxt, totalIncomeLbl ,totalExpensesLbl, totalSavingsLbl;
	static JLabel totalExpensesAmtLbl, totalSavingsAmtLbl, totalIncomeAmtLbl;
	GridBagConstraints gbConst;
	homePanel() {

		summaryTxt = new JLabel("User Summary");
		totalIncomeLbl = new JLabel("Total Income: ");
		totalIncomeAmtLbl = new JLabel("$0.00");
		totalExpensesLbl = new JLabel("Total Expenses: ");
		totalExpensesAmtLbl = new JLabel("$0.00");
		totalSavingsLbl = new JLabel("Total Savings: ");
		totalSavingsAmtLbl = new JLabel("$0.00");
		gbConst = new GridBagConstraints();
		this.setLayout(new GridBagLayout());

		gbConst.gridx = 0;
		gbConst.gridy = 0;
		gbConst.gridwidth = 2;
		gbConst.insets = new Insets(20,20,60,20);
		summaryTxt.setFont(new Font(null, Font.PLAIN, 44));
		this.add(summaryTxt, gbConst);

		gbConst.gridx = 0;
		gbConst.gridy = 1;
		gbConst.gridwidth = 1;
		gbConst.insets = new Insets(20,40,20,5);
		totalIncomeLbl.setFont(new Font(null, Font.PLAIN, 32));
		this.add(totalIncomeLbl, gbConst);

		gbConst.gridx = 1;
		gbConst.gridy = 1;
		gbConst.insets = new Insets(20,10,20,40);
		totalIncomeAmtLbl.setFont(new Font(null, Font.PLAIN, 32));
		this.add(totalIncomeAmtLbl, gbConst);

		gbConst.gridx = 0;
		gbConst.gridy = 2;
		gbConst.insets = new Insets(20,40,20,5);
		totalExpensesLbl.setFont(new Font(null, Font.PLAIN, 32));
		this.add(totalExpensesLbl, gbConst);

		gbConst.gridx = 1;
		gbConst.gridy = 2;
		gbConst.insets = new Insets(20,10,20,40);
		totalExpensesAmtLbl.setFont(new Font(null, Font.PLAIN, 32));
		this.add(totalExpensesAmtLbl, gbConst);

		gbConst.gridx = 0;
		gbConst.gridy = 3;
		gbConst.insets = new Insets(20,40,40,5);
		totalSavingsLbl.setFont(new Font(null, Font.PLAIN, 32));
		this.add(totalSavingsLbl, gbConst);

		gbConst.gridx = 1;
		gbConst.gridy = 3;
		gbConst.insets = new Insets(20,10,40,40);
		totalSavingsAmtLbl.setFont(new Font(null, Font.PLAIN, 32));
		this.add(totalSavingsAmtLbl, gbConst);
	}
}

/**
 * addItemPanel is a class that makes up ewallet's Add Item page. It contains the ability for user to add either an income
 * or an expense.  Adding items will update the home page as well as the reports pages.
 */
class addItemPanel extends JTabbedPane {

	ExpenserMain expenserMain;
	int yearlyFrequency;
	double amount;
	String month, source;
	GridBagConstraints gbConst;
	JPanel incomePane, expensePane;
	JLabel addIncomeItemLbl, addExpenseItemLbl;
	JLabel nameIncomeLbl, amountIncomeLbl, monthIncomeLbl;
	JLabel nameExpenseLbl, amountExpenseLbl, frequencyExpLbl;
	JTextField nameIncField, amountIncField, frequencyExpField;
	JTextField nameExpField, amountExpField;
	JButton addIncomeButton, addExpenseButton;
	JComboBox monthComboBox;
	String[] months;
	addItemPanel() {

		expenserMain = new ExpenserMain();
		expenserMain.userAtHand = appFrame.user;

		months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		incomePane = new JPanel();
		expensePane = new JPanel();

		monthComboBox = new JComboBox<>(months);
		monthComboBox.setFont(new Font(null,Font.PLAIN, 24));
		monthComboBox.setSelectedIndex(0); // setting January as default selection

		addIncomeButton = new JButton("Add");
		addExpenseButton = new JButton("Add");

		gbConst = new GridBagConstraints(); // defining layout managers
		incomePane.setLayout(new GridBagLayout());
		expensePane.setLayout(new GridBagLayout());

		addIncomeItemLbl = new JLabel("Add Item");
		nameIncomeLbl = new JLabel("Name");
		amountIncomeLbl = new JLabel("Amount");
		monthIncomeLbl = new JLabel("Month");

		addExpenseItemLbl = new JLabel("Add Item");
		nameExpenseLbl = new JLabel("Name");
		amountExpenseLbl = new JLabel("Amount");
		frequencyExpLbl = new JLabel("Freq.");

		nameIncField = new JTextField();
		nameIncField.setPreferredSize(new Dimension(280, 50));
		amountIncField = new JTextField();
		amountIncField.setPreferredSize(new Dimension(280, 50));
		frequencyExpField = new JTextField();
		frequencyExpField.setPreferredSize(new Dimension(280, 50));

		nameExpField = new JTextField();
		nameExpField.setPreferredSize(new Dimension(280, 50));
		amountExpField = new JTextField();
		amountExpField.setPreferredSize(new Dimension(280, 50));
		monthComboBox.setPreferredSize(new Dimension(280, 50));

		gbConst.gridx = 0;
		gbConst.gridy = 0;
		gbConst.gridwidth = 2;
		gbConst.insets = new Insets(0,20,60,30);
		addIncomeItemLbl.setFont(new Font(null, Font.PLAIN, 44));
		incomePane.add(addIncomeItemLbl, gbConst);

		gbConst.gridx = 0;
		gbConst.gridy = 1;
		gbConst.gridwidth = 1;
		gbConst.insets = new Insets(10,20,30,30);
		nameIncomeLbl.setFont(new Font(null, Font.PLAIN, 32));
		incomePane.add(nameIncomeLbl, gbConst);

		gbConst.gridx = 1;
		gbConst.gridy = 1;
		gbConst.insets = new Insets(10,10,30,30);
		nameIncField.setFont(new Font(null, Font.PLAIN, 28));
		incomePane.add(nameIncField, gbConst);

		gbConst.gridx = 0;
		gbConst.gridy = 2;
		gbConst.gridwidth = 1;
		gbConst.insets = new Insets(10,20,30,30);
		amountIncomeLbl.setFont(new Font(null, Font.PLAIN, 32));
		incomePane.add(amountIncomeLbl, gbConst);

		gbConst.gridx = 1;
		gbConst.gridy = 2;
		gbConst.insets = new Insets(10,10,30,30);
		amountIncField.setFont(new Font(null, Font.PLAIN, 28));
		incomePane.add(amountIncField, gbConst);

		gbConst.gridx = 0;
		gbConst.gridy = 3;
		gbConst.gridwidth = 1;
		gbConst.insets = new Insets(10,20,30,30);
		monthIncomeLbl.setFont(new Font(null, Font.PLAIN, 32));
		incomePane.add(monthIncomeLbl, gbConst);

		gbConst.gridx = 1;
		gbConst.gridy = 3;
		gbConst.insets = new Insets(10,10,30,30);
		monthComboBox.setFont(new Font(null, Font.PLAIN, 28));
		incomePane.add(monthComboBox, gbConst);

		gbConst.gridx = 0;
		gbConst.gridy = 4;
		gbConst.gridwidth = 2;
		gbConst.insets = new Insets(20,30,30,30);
		addIncomeButton.setFont(new Font(null, Font.PLAIN, 28));
		addIncomeButton.setPreferredSize(new Dimension(150,60));
		incomePane.add(addIncomeButton, gbConst);

		/*
		 * When the add income button is pressed, if there is text in the name and amount field,
		 * the data will be retrieved and put into a wage variable as well as updating both the home table, the item report table
		 * and the detailed report table.
		 */
		addIncomeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == addIncomeButton) {
					if(nameIncField.getText().length() > 0 && amountIncField.getText().length() > 0) {
						try {
							amount = Double.parseDouble(amountIncField.getText());
						} catch (NumberFormatException n) {
							System.out.println(n.getMessage());
							amount = 0.00f;
						}
						source = nameIncField.getText();
						month = String.valueOf(monthComboBox.getItemAt(monthComboBox.getSelectedIndex()));
						Wage w = new Wage(source, amount, month);
						expenserMain.userAtHand.addMonthlyIncome(w); // adding it to the user's wage arraylist

						// Adds item to combobox for type filter if name is unique
						if(incomeRepPanel.typeSelector.getItemCount() > 0) {
							boolean contains = false;
							for (int i = 0; i < incomeRepPanel.typeSelector.getItemCount(); i++) {
								if (incomeRepPanel.typeSelector.getItemAt(i).equals(w.getSource())) {
									contains = true;
								}
							}
							if (!contains) {
								incomeRepPanel.typeSelector.addItem(w.getSource());
							}
						} else {
							incomeRepPanel.typeSelector.addItem(w.getSource());
						}

						// Updating tables and values
						expenserMain.updateIncomeTable();
						expenserMain.updateIncomeValues();
						expenserMain.updateDetailedTable();

						// Clearing fields
						nameIncField.setText("");
						monthComboBox.setSelectedItem(0);
						amountIncField.setText("");

					}

				}
			}
		});

		this.add("Add Income", incomePane);

		gbConst.gridx = 0;
		gbConst.gridy = 0;
		gbConst.insets = new Insets(0,20,60,30);
		addExpenseItemLbl.setFont(new Font(null, Font.PLAIN, 44));
		expensePane.add(addExpenseItemLbl, gbConst);

		gbConst.gridx = 0;
		gbConst.gridy = 1;
		gbConst.gridwidth = 1;
		gbConst.insets = new Insets(10,20,30,30);
		nameExpenseLbl.setFont(new Font(null, Font.PLAIN, 32));
		expensePane.add(nameExpenseLbl, gbConst);

		gbConst.gridx = 1;
		gbConst.gridy = 1;
		gbConst.insets = new Insets(10,10,30,30);
		nameExpField.setFont(new Font(null, Font.PLAIN, 28));
		expensePane.add(nameExpField, gbConst);

		gbConst.gridx = 0;
		gbConst.gridy = 2;
		gbConst.gridwidth = 1;
		gbConst.insets = new Insets(10,20,30,30);
		amountExpenseLbl.setFont(new Font(null, Font.PLAIN, 32));
		expensePane.add(amountExpenseLbl, gbConst);

		gbConst.gridx = 1;
		gbConst.gridy = 2;
		gbConst.insets = new Insets(10,10,30,30);
		amountExpField.setFont(new Font(null, Font.PLAIN, 28));
		expensePane.add(amountExpField, gbConst);

		gbConst.gridx = 0;
		gbConst.gridy = 3;
		gbConst.gridwidth = 1;
		gbConst.insets = new Insets(10,20,30,30);
		frequencyExpLbl.setFont(new Font(null, Font.PLAIN, 32));
		expensePane.add(frequencyExpLbl, gbConst);

		gbConst.gridx = 1;
		gbConst.gridy = 3;
		gbConst.insets = new Insets(10,10,30,30);
		frequencyExpField.setFont(new Font(null, Font.PLAIN, 28));
		expensePane.add(frequencyExpField, gbConst);

		gbConst.gridx = 0;
		gbConst.gridy = 4;
		gbConst.gridwidth = 2;
		gbConst.insets = new Insets(20,30,30,30);
		addExpenseButton.setFont(new Font(null, Font.PLAIN, 28));
		addExpenseButton.setPreferredSize(new Dimension(150,60));
		expensePane.add(addExpenseButton, gbConst);


		/*
		 * When the add expense button is pressed, if there is text in the name, amount, and frequency field,
		 * the data will be retrieved and put into an expense variable as well as updating both the home table, the expense report table
		 * and the detailed report table.
		 */
		addExpenseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == addExpenseButton) {
					if(nameExpField.getText().length() > 0 && amountExpField.getText().length() > 0 & frequencyExpField.getText().length() > 0) {
						try {
							amount = Double.parseDouble(amountExpField.getText());
							yearlyFrequency = Integer.parseInt(frequencyExpField.getText());

						} catch (NumberFormatException n) {
							System.out.println(n.getMessage());
							amount = 0.00f;
						}
						source = nameExpField.getText();
						Expense Ex = new Expense(source, amount, yearlyFrequency); // new expense object
						expenserMain.addExpense(Ex); // adding it to the user's spending arraylist
						expenserMain.updateExpenseValues();

						// Adds item to combobox for type filter if name is unique
						if(expenseRepPanel.typeSelector.getItemCount() > 0) {
							boolean contains = false;
							for (int i = 0; i < expenseRepPanel.typeSelector.getItemCount(); i++) {
								if (expenseRepPanel.typeSelector.getItemAt(i).equals(Ex.getSource())) {
									contains = true;
								}
							}
							if (!contains) {
								expenseRepPanel.typeSelector.addItem(Ex.getSource());
							}
						} else {
							expenseRepPanel.typeSelector.addItem(Ex.getSource());
						}


						// update expense table and expenses on home
						expenserMain.updateExpenseTable();
						expenserMain.updateDetailedTable();

						// Clearing fields
						nameExpField.setText("");
						frequencyExpField.setText("");
						amountExpField.setText("");
					}
				}
			}
		});

		this.add("Add Expense", expensePane);
		this.setFont(new Font(null, Font.PLAIN, 24));
	}
}

/**
 * importPanel is a class that makes up ewallet's import page.  It contains the ability for the user to import data from a csv file.
 * This will be completed in Sprint 2.
 */
class importPanel extends JPanel {

	ExpenserMain expenserMain;
	GridBagConstraints gbConst;
	JLabel importLbl, selectFileLbl, selectTypeLbl, descriptionLbl;
	JButton selectFileButton, importButton;
	JFileChooser fileChooser;
	String[] typesOfImports;
	JComboBox<String> options;
	File userFile;
	importPanel() {

		expenserMain = new ExpenserMain();
		expenserMain.userAtHand = appFrame.user;

		fileChooser = new JFileChooser();

		this.setLayout(new GridBagLayout());
		gbConst = new GridBagConstraints();
		typesOfImports = new String[] {"Income","Expense"};
		options = new JComboBox<>(typesOfImports); // combo box for selecting which type to input to
		options.setSelectedIndex(0); // sets Income as initial selection

		importLbl = new JLabel("Import From File");
		gbConst.gridx = 0;
		gbConst.gridy = 0;
		gbConst.gridwidth = 2;
		gbConst.insets = new Insets(10,30,20,30);
		importLbl.setFont(new Font(null, Font.PLAIN, 44));
		this.add(importLbl, gbConst);

		selectFileButton = new JButton("File");
		selectFileButton.addActionListener(new ActionListener() { // When the select file button is pressed
			@Override
			public void actionPerformed(ActionEvent e) { // File chooser component
				if (e.getSource() == selectFileButton) {
					int userDecision = fileChooser.showOpenDialog(null); // opens file chooser window
					if(userDecision == JFileChooser.APPROVE_OPTION) { // if file is selected
						userFile = fileChooser.getSelectedFile();
						if(!userFile.getAbsolutePath().endsWith(".csv")){ // if user selects a non-csv file, the user will be alerted.
							JOptionPane.showMessageDialog(null,"Warning.  Only csv files are supported.  Select something else.", "Warning!", JOptionPane.ERROR_MESSAGE);
							System.out.println("User selected a non csv file!");
						}
						System.out.println("The user selected: " + userFile.getAbsolutePath());
					} else if (userDecision == JFileChooser.CANCEL_OPTION) { // if user backs out
						System.out.println("The user canceled the operation.");
					}
				}
			}
		});

		selectFileLbl = new JLabel("Select File");
		gbConst.gridx = 0;
		gbConst.gridy = 1;
		gbConst.gridwidth = 1;
		gbConst.insets = new Insets(30,30,20,0);
		selectFileLbl.setFont(new Font(null, Font.PLAIN, 24));
		this.add(selectFileLbl, gbConst);

		gbConst.gridx = 1;
		gbConst.gridy = 1;
		gbConst.insets = new Insets(30,0,20,30);
		selectFileButton.setFont(new Font(null, Font.PLAIN, 24));
		this.add(selectFileButton, gbConst);

		selectTypeLbl = new JLabel("Select Type");
		gbConst.gridx = 0;
		gbConst.gridy = 2;
		gbConst.insets = new Insets(30,30,20,0);
		selectTypeLbl.setFont(new Font(null, Font.PLAIN, 24));
		this.add(selectTypeLbl, gbConst);

		gbConst.gridx = 1;
		gbConst.gridy = 2;
		gbConst.insets = new Insets(30,0,20,30);
		options.setFont(new Font(null, Font.PLAIN, 24));
		this.add(options, gbConst);

		descriptionLbl = new JLabel("<html><body style='text-align: center'>Note: Only csv files are supported. <br><br> The format of the csv file matters.  <br>The first line of the file needs to contain \"source,amount,month\" or <br>\"source,amount,frequency\", depending on the type<br><br>Once you select a file, click the import button.</body></html>");
		gbConst.gridwidth = 2;
		gbConst.gridheight = 2;
		gbConst.gridx = 0;
		gbConst.gridy = 3;
		gbConst.insets = new Insets(30,30,30,30);
		descriptionLbl.setFont(new Font(null, Font.PLAIN, 20));
		this.add(descriptionLbl, gbConst);

		importButton = new JButton("Import");
		importButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { // When the import button is pressed
				if(options.getItemAt(options.getSelectedIndex()).equalsIgnoreCase("income")) {
					System.out.println("Income Selected");
					if(userFile == null) { // if there isn't a file selected
						JOptionPane.showMessageDialog(null,"No file selected!", "Warning User!", JOptionPane.ERROR_MESSAGE);
					} else { // when there is a csv file selected
						expenserMain.loadIncomeFile(userFile.getAbsolutePath());
						expenserMain.updateIncomeTable();
						expenserMain.updateDetailedTable();
						expenserMain.updateIncomeValues();
					}
				} else if (options.getItemAt(options.getSelectedIndex()).equalsIgnoreCase("expense")) {
					System.out.println("Expense Selected");
					if(userFile == null) { // if there isn't a file selected
						JOptionPane.showMessageDialog(null,"No file selected!", "Warning User!", JOptionPane.ERROR_MESSAGE);
					} else { // when there is a csv file selected
						expenserMain.loadExpenseFile(userFile.getAbsolutePath());
						expenserMain.updateExpenseTable();
						expenserMain.updateDetailedTable();
						expenserMain.updateExpenseValues();
					}
				}

			}
		});
		gbConst.gridheight = 1;
		gbConst.gridx = 0;
		gbConst.gridy = 5;
		gbConst.insets = new Insets(30,0,30,30);
		importButton.setFont(new Font(null, Font.PLAIN, 24));
		this.add(importButton, gbConst);

	}
}

/**
 * estimatePanel is a class that makes up ewallet's estimate page. It contains the ability for the user to estimate how long it
 * will take for a user to save up for an item based on stored income and expense information.
 */
class estimatePanel extends JPanel {
	GridBagConstraints gbConst;
	JLabel estimateTitleLbl, nameLbl, priceLbl, estimateLbl, estimateAmtLbl;
	JTextField nameField, priceField;
	JButton estimateButton;
	estimatePanel() {
		this.setLayout(new GridBagLayout());
		gbConst = new GridBagConstraints();

		estimateTitleLbl = new JLabel("Estimate Tool");
		gbConst.gridx = 0;
		gbConst.gridy = 0;
		gbConst.gridwidth = 2;
		gbConst.insets = new Insets(10,20,30,30);
		estimateTitleLbl.setFont(new Font(null, Font.PLAIN, 44));
		this.add(estimateTitleLbl, gbConst);

		estimateLbl = new JLabel("Estimate:");
		gbConst.gridx = 0;
		gbConst.gridy = 1;
		gbConst.gridwidth = 1;
		gbConst.insets = new Insets(10,0,30,0);
		estimateLbl.setFont(new Font(null, Font.PLAIN, 32));
		this.add(estimateLbl, gbConst);

		estimateAmtLbl = new JLabel("0 days");
		gbConst.gridx = 1;
		gbConst.gridy = 1;
		gbConst.insets = new Insets(10,0,30,0);
		estimateAmtLbl.setFont(new Font(null, Font.PLAIN, 32));
		this.add(estimateAmtLbl, gbConst);

		nameLbl = new JLabel("Item Name");
		gbConst.gridx = 0;
		gbConst.gridy = 2;
		gbConst.insets = new Insets(10,20,30,30);
		nameLbl.setFont(new Font(null, Font.PLAIN, 32));
		this.add(nameLbl, gbConst);

		nameField = new JTextField();
		nameField.setPreferredSize(new Dimension(280, 50));
		gbConst.gridx = 1;
		gbConst.gridy = 2;
		gbConst.insets = new Insets(10,10,30,30);
		nameField.setFont(new Font(null, Font.PLAIN, 28));
		this.add(nameField, gbConst);

		priceLbl = new JLabel("Item Price");
		gbConst.gridx = 0;
		gbConst.gridy = 3;
		gbConst.insets = new Insets(10,20,30,30);
		priceLbl.setFont(new Font(null, Font.PLAIN, 32));
		this.add(priceLbl, gbConst);

		priceField = new JTextField();
		priceField.setPreferredSize(new Dimension(280, 50));
		gbConst.gridx = 1;
		gbConst.gridy = 3;
		gbConst.insets = new Insets(10,10,30,30);
		priceField.setFont(new Font(null, Font.PLAIN, 28));
		this.add(priceField, gbConst);

		estimateButton = new JButton("Get Estimate");
		estimateButton.addActionListener(new ActionListener() {
			// Will retrieve estimate information.  Scheduled for Sprint 2.
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == estimateButton) {
					System.out.println("Get Estimate Button Clicked.");
					nameField.setText("");
					priceField.setText("");
				}
			}
		});
		estimateButton.setPreferredSize(new Dimension(220, 60));
		gbConst.gridx = 0;
		gbConst.gridy = 4;
		gbConst.gridwidth = 2;
		gbConst.insets = new Insets(30,30,30,30);
		estimateButton.setFont(new Font(null, Font.PLAIN, 28));
		this.add(estimateButton, gbConst);

	}
}

/**
 * incomeRepPanel is a class that makes up ewallet's income report page.  It shows basic income report information like all
 * income information and contains the ability to filter data by month or source.
 */
class incomeRepPanel extends JPanel {
	static DefaultTableModel model;
	DefaultTableCellRenderer centerRenderer;
	static JScrollPane jScrollPane;
	static ArrayList<Wage> Income;
	static ArrayList<Wage> filteredIncomeList = new ArrayList<>();
	Object[][] tableVals; // table values
	String[] columnHeadings;
	static JTable incomeTable;
	JLabel incomeText, filterTxt;
	JLabel totalIncomeLbl, totalFilteredIncomeLbl;
	static JLabel totalIncomeAmtLbl, totalFilteredIncomeAmtLbl;
	JButton exportReport, applyFilter;
	ExpenserMain expenserMain;
	GridBagConstraints gbConst;
	JPanel upperPanel;
	JComboBox monthSelector;
	static JComboBox typeSelector;
	String[] months;
	incomeRepPanel() {

		expenserMain = new ExpenserMain();
		expenserMain.userAtHand = appFrame.user;
		incomeRepPanel.Income = expenserMain.userAtHand.getIncome(); // retrieving income data

		this.setLayout(new BorderLayout());

		incomeText = new JLabel("Income Report");
		incomeText.setFont(new Font(null, Font.PLAIN, 40));
		incomeText.setHorizontalAlignment(JLabel.CENTER);

		gbConst = new GridBagConstraints();
		upperPanel = new JPanel();
		upperPanel.setLayout(new GridBagLayout());
		gbConst.gridx = 0;
		gbConst.gridy = 0;
		gbConst.gridwidth = 4;
		gbConst.insets = new Insets(20,0,20,0);
		upperPanel.add(incomeText, gbConst);

		totalIncomeLbl = new JLabel("Total Income");
		totalIncomeLbl.setFont(new Font(null, Font.PLAIN, 32));
		gbConst.gridwidth = 1;
		gbConst.gridx = 0;
		gbConst.gridy = 1;
		gbConst.insets = new Insets(0,20,20,0);
		upperPanel.add(totalIncomeLbl,gbConst);

		totalIncomeAmtLbl = new JLabel("0.00");
		totalIncomeAmtLbl.setFont(new Font(null, Font.PLAIN, 32));
		gbConst.gridx = 1;
		gbConst.gridy = 1;
		gbConst.insets = new Insets(0,5,20,5);
		upperPanel.add(totalIncomeAmtLbl,gbConst);

		totalFilteredIncomeLbl = new JLabel("Income (Filtered)");
		totalFilteredIncomeLbl.setFont(new Font(null, Font.PLAIN, 32));
		gbConst.gridx = 2;
		gbConst.gridy = 1;
		gbConst.insets = new Insets(0,5,20,5);
		upperPanel.add(totalFilteredIncomeLbl,gbConst);

		totalFilteredIncomeAmtLbl = new JLabel("0.00");
		totalFilteredIncomeAmtLbl.setFont(new Font(null, Font.PLAIN, 32));
		gbConst.gridx = 3;
		gbConst.gridy = 1;
		gbConst.insets = new Insets(0,5,20,20);
		upperPanel.add(totalFilteredIncomeAmtLbl,gbConst);

		filterTxt = new JLabel("Apply a filter");
		filterTxt.setFont(new Font(null, Font.PLAIN, 24));
		gbConst.gridx = 0;
		gbConst.gridy = 2;
		gbConst.insets = new Insets(0,20,20,0);
		upperPanel.add(filterTxt,gbConst);

		months = new String[]{"","January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		monthSelector = new JComboBox<>(months);
		monthSelector.setPreferredSize(new Dimension(200,50));
		monthSelector.setFont(new Font(null, Font.PLAIN, 24));
		gbConst.gridx = 1;
		gbConst.gridy = 2;
		gbConst.insets = new Insets(0,20,20,20);
		upperPanel.add(monthSelector,gbConst);

		incomeRepPanel.typeSelector = new JComboBox<>(expenserMain.getIncomeSources(Income).toArray());
		typeSelector.setFont(new Font(null, Font.PLAIN, 24));
		typeSelector.setPreferredSize(new Dimension(200,50));
		gbConst.gridx = 2;
		gbConst.gridy = 2;
		gbConst.insets = new Insets(0,20,20,20);
		upperPanel.add(typeSelector,gbConst);

		applyFilter = new JButton("Filter");
		applyFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == applyFilter) {
					filteredIncomeList =
							expenserMain.filterIncomesMonth(expenserMain.filterIncomesSource(expenserMain.userAtHand.getIncome(),(String)typeSelector.getItemAt(typeSelector.getSelectedIndex())), (String)monthSelector.getItemAt(monthSelector.getSelectedIndex()));
					incomeRepPanel.model.setNumRows(filteredIncomeList.size());
					int i = 0;
					double incomeSum = 0.00f;
					for(Wage wage : filteredIncomeList) {
						incomeRepPanel.incomeTable.setValueAt(wage.getSource(), i, 0);
						incomeRepPanel.incomeTable.setValueAt(String.format("$%.2f",wage.getAmount()), i, 1);
						incomeRepPanel.incomeTable.setValueAt(wage.getMonth(), i, 2);
						++i;
						incomeSum += wage.getAmount();
					}
					totalFilteredIncomeAmtLbl.setText(String.format("$%.2f",incomeSum));
				}
			}
		});
		applyFilter.setFont(new Font(null, Font.PLAIN, 24));
		gbConst.gridx = 3;
		gbConst.gridy = 2;
		gbConst.insets = new Insets(0,20,20,20);
		upperPanel.add(applyFilter,gbConst);

		this.add(upperPanel, BorderLayout.PAGE_START);

		centerRenderer = new DefaultTableCellRenderer();
		columnHeadings = new String[]{"Source","Amount", "Month"};
		tableVals = new Object[Income.size()][3]; // creating table with 3 columns and as many rows as there are data in Income arraylist
		model = new DefaultTableModel(tableVals, columnHeadings); // setting up table model
		incomeTable = new JTable(model) {
			public boolean isCellEditable(int row, int column) { // restricting cell editing
				return false;
			}
		};
		jScrollPane = new JScrollPane(incomeTable);

		// Centering items in table cells
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < incomeTable.getColumnCount(); i++) {
			incomeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		incomeTable.setDefaultRenderer(String.class, centerRenderer);

		incomeTable.setFont(new Font(null, Font.PLAIN, 24));
		incomeTable.setRowHeight(45);
		incomeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		incomeTable.getTableHeader().setReorderingAllowed(false);
		incomeTable.setFocusable(true);
		incomeTable.setRowSelectionAllowed(true);
		incomeTable.setCellSelectionEnabled(true);
		incomeTable.getTableHeader().setFont(new Font(null, Font.PLAIN, 32));
		incomeTable.setShowVerticalLines(false);

		this.add(jScrollPane, BorderLayout.CENTER);

		exportReport = new JButton("Export to CSV");
		exportReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { // exports report to csv
				expenserMain.exportReport("Income Report");
			}
		});
		exportReport.setSize(new Dimension(200,60));
		exportReport.setFont(new Font(null, Font.PLAIN, 24));

		// Creating centered button in bottom part of border layout
		JPanel lowerPanel = new JPanel();
		lowerPanel.add(Box.createRigidArea(new Dimension(25,50)));
		lowerPanel.add(exportReport, BorderLayout.CENTER);
		lowerPanel.add(Box.createRigidArea(new Dimension(25,50)));
		this.add(lowerPanel, BorderLayout.SOUTH);

	}
}

/**
 * expenseRepPanel is a class that makes up ewallet's expense report page.  It shows basic expense report information like all
 * expense information and will contain the ability to filter data by month or type.
 */
class expenseRepPanel extends JPanel {
	static DefaultTableModel model;
	DefaultTableCellRenderer centerRenderer;
	static JScrollPane jScrollPane;
	static ArrayList<Expense> Spending;
	static ArrayList<Expense> filteredSpending = new ArrayList<>();
	Object[][] tableVals;
	String[] columnHeadings;
	static JTable spendingTable;
	JLabel expenseText;
	JLabel totalExpenseLbl, totalFilteredExpenseLbl, filterTxt ;
	static JLabel totalExpenseAmtLbl, totalFilteredExpenseAmtLbl;
	JButton exportReport, applyFilter;
	ExpenserMain expenserMain;
	GridBagConstraints gbConst;
	JPanel upperPanel;
	JComboBox monthSelector;
	static JComboBox typeSelector;
	String[] frequency;

	expenseRepPanel() {
		expenserMain = new ExpenserMain();
		expenserMain.userAtHand = appFrame.user;
		expenseRepPanel.Spending = expenserMain.userAtHand.getSpending();

		this.setLayout(new BorderLayout());


		expenseText = new JLabel("Expense Report");
		expenseText.setFont(new Font(null, Font.PLAIN, 40));
		expenseText.setHorizontalAlignment(JLabel.CENTER);

		gbConst = new GridBagConstraints();
		upperPanel = new JPanel();
		upperPanel.setLayout(new GridBagLayout());
		gbConst.gridx = 0;
		gbConst.gridy = 0;
		gbConst.gridwidth = 4;
		gbConst.insets = new Insets(20,0,20,0);
		upperPanel.add(expenseText, gbConst);

		totalExpenseLbl = new JLabel("Total Expense");
		totalExpenseLbl.setFont(new Font(null, Font.PLAIN, 32));
		gbConst.gridwidth = 1;
		gbConst.gridx = 0;
		gbConst.gridy = 1;
		gbConst.insets = new Insets(0,20,20,0);
		upperPanel.add(totalExpenseLbl,gbConst);

		totalExpenseAmtLbl = new JLabel("0.00");
		totalExpenseAmtLbl.setFont(new Font(null, Font.PLAIN, 32));
		gbConst.gridx = 1;
		gbConst.gridy = 1;
		gbConst.insets = new Insets(0,5,20,5);
		upperPanel.add(totalExpenseAmtLbl,gbConst);

		totalFilteredExpenseLbl = new JLabel("Expenses (Filtered)");
		totalFilteredExpenseLbl.setFont(new Font(null, Font.PLAIN, 32));
		gbConst.gridx = 2;
		gbConst.gridy = 1;
		gbConst.insets = new Insets(0,5,20,5);
		upperPanel.add(totalFilteredExpenseLbl,gbConst);

		totalFilteredExpenseAmtLbl = new JLabel("0.00");
		totalFilteredExpenseAmtLbl.setFont(new Font(null, Font.PLAIN, 32));
		gbConst.gridx = 3;
		gbConst.gridy = 1;
		gbConst.insets = new Insets(0,5,20,20);
		upperPanel.add(totalFilteredExpenseAmtLbl,gbConst);

		filterTxt = new JLabel("Apply a filter");
		filterTxt.setFont(new Font(null, Font.PLAIN, 24));
		gbConst.gridx = 0;
		gbConst.gridy = 2;
		gbConst.insets = new Insets(0,20,20,0);
		upperPanel.add(filterTxt,gbConst);

		frequency = new String[]{"1", "12","24"};
		monthSelector = new JComboBox<>(frequency);
		monthSelector.setFont(new Font(null, Font.PLAIN, 24));
		monthSelector.setPreferredSize(new Dimension(200,50));
		gbConst.gridx = 1;
		gbConst.gridy = 2;
		gbConst.insets = new Insets(0,20,20,20);
		upperPanel.add(monthSelector,gbConst);

		expenseRepPanel.typeSelector = new JComboBox<>(expenserMain.getExpenseSources(Spending).toArray());
		typeSelector.setFont(new Font(null, Font.PLAIN, 24));
		typeSelector.setPreferredSize(new Dimension(200,50));
		gbConst.gridx = 2;
		gbConst.gridy = 2;
		gbConst.insets = new Insets(0,20,20,20);
		upperPanel.add(typeSelector,gbConst);

		applyFilter = new JButton("Filter");
		applyFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { // Filters table results based on user selected categories like month and type.  Repopulates table and summary information
				if(e.getSource() == applyFilter) {
					filteredSpending = expenserMain.filterExpensesFreq
							(expenserMain.filterExpensesSource(expenserMain.userAtHand.getSpending(),(String)typeSelector.getItemAt(typeSelector.getSelectedIndex()))
							,(String)monthSelector.getItemAt(monthSelector.getSelectedIndex()));
					expenseRepPanel.model.setNumRows(filteredSpending.size());
					int i = 0;
					double expenseSum = 0.00f;
					for(Expense exp : filteredSpending) {
						expenseRepPanel.spendingTable.setValueAt(exp.getSource(), i, 0);
						expenseRepPanel.spendingTable.setValueAt(String.format("$%.2f",exp.getAmount()), i, 1);
						expenseRepPanel.spendingTable.setValueAt(String.valueOf(exp.getFrequency()), i, 2);
						++i;
						expenseSum += exp.getAmount();
					}
					totalFilteredExpenseAmtLbl.setText(String.format("$%.2f",expenseSum));
				}
			}
		});
		applyFilter.setFont(new Font(null, Font.PLAIN, 24));
		gbConst.gridx = 3;
		gbConst.gridy = 2;
		gbConst.insets = new Insets(0,20,20,20);
		upperPanel.add(applyFilter,gbConst);

		this.add(upperPanel, BorderLayout.PAGE_START);

		centerRenderer = new DefaultTableCellRenderer();
		columnHeadings = new String[]{"Source","Amount", "Frequency"};
		tableVals = new Object[Spending.size()][3]; // creating table with 3 columns and as many rows as there are data in Spending arraylist
		model = new DefaultTableModel(tableVals, columnHeadings); // setting up table model
		spendingTable = new JTable(model) {
			public boolean isCellEditable(int row, int column) { // restricting cell editing
				return false;
			}
		};
		jScrollPane = new JScrollPane(spendingTable);

		// Centering items in table cells
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < spendingTable.getColumnCount(); i++) {
			spendingTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		spendingTable.setDefaultRenderer(String.class, centerRenderer);

		spendingTable.setFont(new Font(null, Font.PLAIN, 24));
		spendingTable.setRowHeight(45);
		spendingTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		spendingTable.getTableHeader().setReorderingAllowed(false);
		spendingTable.setFocusable(true);
		spendingTable.setRowSelectionAllowed(true);
		spendingTable.setCellSelectionEnabled(true);
		spendingTable.getTableHeader().setFont(new Font(null, Font.PLAIN, 32));
		spendingTable.setShowVerticalLines(false);

		this.add(jScrollPane, BorderLayout.CENTER);

		exportReport = new JButton("Export to CSV");
		exportReport.addActionListener(new ActionListener() { // Exports report to csv file
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == exportReport) {
					expenserMain.exportReport("Expense Report");
				}
			}
		});
		exportReport.setSize(new Dimension(200,60));
		exportReport.setFont(new Font(null, Font.PLAIN, 24));

		// Creating centered button in bottom part of border layout
		JPanel lowerPanel = new JPanel();
		lowerPanel.add(Box.createRigidArea(new Dimension(25,50)));
		lowerPanel.add(exportReport, BorderLayout.CENTER);
		lowerPanel.add(Box.createRigidArea(new Dimension(25,50)));
		this.add(lowerPanel, BorderLayout.SOUTH);

	}
}

/**
 * detailedRepPanel is a class that makes up ewallet's detailed report page.  It shows basic income and expense report information like all
 * expense and income information and will contain the ability to filter data by month or type.
 */
class detailedRepPanel extends JPanel {
	static DefaultTableModel model;
	DefaultTableCellRenderer centerRenderer;
	static JScrollPane jScrollPane;
	ArrayList<Expense> Spending;
	ArrayList<Wage> Income;
	Object[][] tableVals;
	String[] columnHeadings;
	static JTable detailedTable;
	JLabel detaileReportTxt;
	JButton exportReport;
	ExpenserMain expenserMain;
	detailedRepPanel() {

		expenserMain = new ExpenserMain();
		expenserMain.userAtHand = appFrame.user;

		this.setLayout(new BorderLayout());
		detaileReportTxt = new JLabel("Detailed Report");
		detaileReportTxt.setFont(new Font(null, Font.PLAIN, 40));
		detaileReportTxt.setHorizontalAlignment(JLabel.CENTER);
		this.add(detaileReportTxt, BorderLayout.PAGE_START);
		centerRenderer = new DefaultTableCellRenderer();
		columnHeadings = new String[]{"Type","Source","Amount", "Month / Freq"};
		Spending = expenserMain.userAtHand.getSpending();
		Income = expenserMain.userAtHand.getIncome();
		tableVals = new Object[Spending.size()+Income.size()][4];
		model = new DefaultTableModel(tableVals, columnHeadings); // setting up table model
		detailedTable = new JTable(model) {
			public boolean isCellEditable(int row, int column) { // restricting cell editing
				return false;
			}
		};
		jScrollPane = new JScrollPane(detailedTable);

		// Centering items in table cells
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < detailedTable.getColumnCount(); i++) {
			detailedTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		detailedTable.setDefaultRenderer(String.class, centerRenderer);

		detailedTable.setFont(new Font(null, Font.PLAIN, 24));
		detailedTable.setRowHeight(45);
		detailedTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		detailedTable.getTableHeader().setReorderingAllowed(false);
		detailedTable.setFocusable(true);
		detailedTable.setRowSelectionAllowed(true);
		detailedTable.setCellSelectionEnabled(true);
		detailedTable.getTableHeader().setFont(new Font(null, Font.PLAIN, 32));
		detailedTable.setShowVerticalLines(false);

		this.add(jScrollPane, BorderLayout.CENTER);

		exportReport = new JButton("Export to CSV"); // exports report to csv file
		exportReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				expenserMain.exportReport("Detailed Report");
			}
		});
		exportReport.setSize(new Dimension(200,60));
		exportReport.setFont(new Font(null, Font.PLAIN, 24));

		// Creating centered button in bottom part of border layout
		JPanel lowerPanel = new JPanel();
		lowerPanel.add(Box.createRigidArea(new Dimension(25,50)));
		lowerPanel.add(exportReport, BorderLayout.CENTER);
		lowerPanel.add(Box.createRigidArea(new Dimension(25,50)));
		this.add(lowerPanel, BorderLayout.SOUTH);
	}

}

class loginPanel extends JPanel {
	JLabel usernameLbl, passwordLbl, loginLbl;
	GridBagConstraints gbConst;
	JTextField usernameIncField, passwordIncField;
	JButton loginBtn;
	
	loginPanel() {
		loginLbl = new JLabel("LOGIN");
		usernameLbl = new JLabel("Username: ");
		passwordLbl = new JLabel("Password: ");
		loginBtn = new JButton("Login");
		gbConst = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		
		passwordIncField = new JTextField();
		passwordIncField.setPreferredSize(new Dimension(200, 40));
		usernameIncField = new JTextField();
		usernameIncField.setPreferredSize(new Dimension(200, 40));
		
		gbConst.gridx = 0;
		gbConst.gridy = 0;
		gbConst.gridwidth = 2;
		gbConst.insets = new Insets(20,10,20,40);
		loginLbl.setFont(new Font(null, Font.PLAIN, 44));
		this.add(loginLbl, gbConst);
		
		gbConst.gridx = 1;
		gbConst.gridy = 2;
		gbConst.gridwidth = 1;
		gbConst.insets = new Insets(0,5,20,30);
		passwordIncField.setFont(new Font(null, Font.PLAIN, 28));
		this.add(passwordIncField, gbConst);

		gbConst.gridx = 1;
		gbConst.gridy = 1;
		gbConst.insets = new Insets(0,5,20,30);
		usernameIncField.setFont(new Font(null, Font.PLAIN, 28));
		this.add(usernameIncField, gbConst);

		gbConst.gridx = 0;
		gbConst.gridy = 2;
		gbConst.insets = new Insets(0,0,20,20);
		passwordLbl.setFont(new Font(null, Font.PLAIN, 44));
		this.add(passwordLbl, gbConst);

		gbConst.gridx = 0;
		gbConst.gridy = 1;
		gbConst.insets = new Insets(0,0,20,20);
		usernameLbl.setFont(new Font(null, Font.PLAIN, 44));
		this.add(usernameLbl, gbConst);
		
		gbConst.gridx = 0;
		gbConst.gridy = 4;
		gbConst.gridwidth = 2;
		gbConst.insets = new Insets(20,30,30,30);
		loginBtn.setFont(new Font(null, Font.PLAIN, 28));
		loginBtn.setPreferredSize(new Dimension(150,60));
		this.add(loginBtn, gbConst);
		
		
	}
}

