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

		if (checkForRepeatUsernames(username) == false && isComplexPassword(password)) { // If there are no repeat usernames and password is valid, a new account will be created and stored.
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
			} catch (IOException e) {
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
			Long lines = Files.lines(path).count(); // Counts lines in UserCredentials.csv
			String textAtLine = "";
			String readUsername = "";
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

	}

}

class appFrame extends JFrame {

	JMenuBar navMenuBar;
	JMenu navMenu;
	JMenuItem homeNav, addItemNav, importNav, estimateNav, incomeReportNav, expenseReportNav, detailedReportNav;

	appFrame(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// this.setPreferredSize(new Dimension(800,600));
		this.setTitle("EWallet Application");


		homePanel hPanel = new homePanel();
		addItemPanel addItmPanel = new addItemPanel();
		importPanel impPanel = new importPanel();
		estimatePanel estPanel = new estimatePanel();
		incomeRepPanel incRepPanel = new incomeRepPanel();
		expenseRepPanel expRepPanel = new expenseRepPanel();
		detailedRepPanel detRepPanel = new detailedRepPanel();
		getContentPane().add(hPanel);
		navMenuBar = new JMenuBar();
		navMenu = new JMenu("<html><p style='margin-left:20'>Menu");
		homeNav = new JMenuItem("<html><p style='margin-left:15'>Home");
		homeNav.addMouseListener(new MouseAdapter() {
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
		addItemNav = new JMenuItem("<html><p style='margin-left:15'>Add Item");
		addItemNav.addMouseListener(new MouseAdapter() {
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

		importNav = new JMenuItem("<html><p style='margin-left:15'>Import Tool");
		importNav.addMouseListener(new MouseAdapter() {
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
		estimateNav = new JMenuItem("<html><p style='margin-left:15'>Estimate Tool");
		estimateNav.addMouseListener(new MouseAdapter() {
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
		incomeReportNav = new JMenuItem("<html><p style='margin-left:15'>Income Report");
		incomeReportNav.addMouseListener(new MouseAdapter() {
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
		expenseReportNav = new JMenuItem("<html><p style='margin-left:15'>Expense Report");
		expenseReportNav.addMouseListener(new MouseAdapter() {
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
		detailedReportNav = new JMenuItem("<html><p style='margin-left:15'>Detailed Report");
		detailedReportNav.addMouseListener(new MouseAdapter() {
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

		navMenu.setFont(new Font(null, Font.PLAIN, 24));
		homeNav.setFont(new Font(null, Font.PLAIN, 20));
		addItemNav.setFont(new Font(null, Font.PLAIN, 20));
		importNav.setFont(new Font(null, Font.PLAIN, 20));
		estimateNav.setFont(new Font(null, Font.PLAIN, 20));
		incomeReportNav.setFont(new Font(null, Font.PLAIN, 20));
		expenseReportNav.setFont(new Font(null, Font.PLAIN, 20));
		detailedReportNav.setFont(new Font(null, Font.PLAIN, 20));

		navMenu.add(homeNav);
		navMenu.add(addItemNav);
		navMenu.add(importNav);
		navMenu.add(estimateNav);
		navMenu.add(incomeReportNav);
		navMenu.add(expenseReportNav);
		navMenu.add(detailedReportNav);
		navMenuBar.add(navMenu);

		this.setJMenuBar(navMenuBar);
		this.setLayout(new CardLayout());
		this.pack();
		this.setVisible(true);
	}
}

class homePanel extends JPanel {

	JLabel summaryTxt;
	JLabel totalIncomeLbl, totalIncomeAmtLbl;
	JLabel totalExpensesLbl, totalExpensesAmtLbl;
	JLabel totalSavingsLbl, totalSavingsAmtLbl;
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
		gbConst.insets = new Insets(20,20,20,20);
		summaryTxt.setFont(new Font(null, Font.PLAIN, 32));
		this.add(summaryTxt, gbConst);

		gbConst.gridx = 0;
		gbConst.gridy = 1;
		gbConst.gridwidth = 1;
		gbConst.insets = new Insets(20,40,20,5);
		totalIncomeLbl.setFont(new Font(null, Font.PLAIN, 25));
		this.add(totalIncomeLbl, gbConst);

		gbConst.gridx = 1;
		gbConst.gridy = 1;
		gbConst.insets = new Insets(20,10,20,40);
		totalIncomeAmtLbl.setFont(new Font(null, Font.PLAIN, 25));
		this.add(totalIncomeAmtLbl, gbConst);

		gbConst.gridx = 0;
		gbConst.gridy = 2;
		gbConst.insets = new Insets(20,40,20,5);
		totalExpensesLbl.setFont(new Font(null, Font.PLAIN, 25));
		this.add(totalExpensesLbl, gbConst);

		gbConst.gridx = 1;
		gbConst.gridy = 2;
		gbConst.insets = new Insets(20,10,20,40);
		totalExpensesAmtLbl.setFont(new Font(null, Font.PLAIN, 25));
		this.add(totalExpensesAmtLbl, gbConst);

		gbConst.gridx = 0;
		gbConst.gridy = 3;
		gbConst.insets = new Insets(20,40,40,5);
		totalSavingsLbl.setFont(new Font(null, Font.PLAIN, 25));
		this.add(totalSavingsLbl, gbConst);

		gbConst.gridx = 1;
		gbConst.gridy = 3;
		gbConst.insets = new Insets(20,10,40,40);
		totalSavingsAmtLbl.setFont(new Font(null, Font.PLAIN, 25));
		this.add(totalSavingsAmtLbl, gbConst);
	}
}

class addItemPanel extends JPanel {
	GridBagConstraints gbConst;
	JLabel addItemLbl;
	JLabel nameLbl, amountLbl, monthLbl, frequencyLbl;
	JTextField nameField, amountField, monthField, frequencyField;
	addItemPanel() {
		gbConst = new GridBagConstraints();
		this.setLayout(new GridBagLayout());

		addItemLbl = new JLabel("Add Item");
		nameLbl = new JLabel("Name");
		amountLbl = new JLabel("Amount");
		monthLbl = new JLabel("Month");
		frequencyLbl = new JLabel("Frequency");

		nameField = new JTextField();
		nameField.setPreferredSize(new Dimension(180, 40));
		amountField = new JTextField();
		amountField.setPreferredSize(new Dimension(180, 40));
		monthField = new JTextField();
		monthField.setPreferredSize(new Dimension(180, 40));
		frequencyField = new JTextField();
		frequencyField.setPreferredSize(new Dimension(180, 40));

		gbConst.gridx = 0;
		gbConst.gridy = 0;
		gbConst.gridwidth = 2;
		gbConst.insets = new Insets(0,20,20,20);
		addItemLbl.setFont(new Font(null, Font.PLAIN, 32));
		this.add(addItemLbl, gbConst);

		gbConst.gridx = 0;
		gbConst.gridy = 1;
		gbConst.gridwidth = 1;
		gbConst.insets = new Insets(10,20,20,5);
		nameLbl.setFont(new Font(null, Font.PLAIN, 24));
		this.add(nameLbl, gbConst);

		gbConst.gridx = 1;
		gbConst.gridy = 1;
		gbConst.insets = new Insets(10,10,20,20);
		nameField.setFont(new Font(null, Font.PLAIN, 24));
		this.add(nameField, gbConst);

		gbConst.gridx = 0;
		gbConst.gridy = 2;
		gbConst.gridwidth = 1;
		gbConst.insets = new Insets(10,20,20,5);
		amountLbl.setFont(new Font(null, Font.PLAIN, 24));
		this.add(amountLbl, gbConst);

		gbConst.gridx = 1;
		gbConst.gridy = 2;
		gbConst.insets = new Insets(10,10,20,20);
		amountField.setFont(new Font(null, Font.PLAIN, 24));
		this.add(amountField, gbConst);

		gbConst.gridx = 0;
		gbConst.gridy = 3;
		gbConst.gridwidth = 1;
		gbConst.insets = new Insets(10,20,20,5);
		amountLbl.setFont(new Font(null, Font.PLAIN, 24));
		this.add(amountLbl, gbConst);

		gbConst.gridx = 1;
		gbConst.gridy = 3;
		gbConst.insets = new Insets(10,10,20,20);
		amountField.setFont(new Font(null, Font.PLAIN, 24));
		this.add(amountField, gbConst);
	}
}

class importPanel extends JPanel {
	JLabel testLbl;
	importPanel() {
		testLbl = new JLabel("Test Import Nav");
		this.add(testLbl);
	}
}

class estimatePanel extends JPanel {
	JLabel testLbl;
	estimatePanel() {
		testLbl = new JLabel("Test Estimate Nav");
		this.add(testLbl);
	}
}

class incomeRepPanel extends JPanel {
	JLabel testLbl;
	incomeRepPanel() {
		testLbl = new JLabel("Test Income Report Nav");
		this.add(testLbl);
	}
}

class expenseRepPanel extends JPanel {
	JLabel testLbl;
	expenseRepPanel() {
		testLbl = new JLabel("Test Expense Report Nav");
		this.add(testLbl);
	}
}

class detailedRepPanel extends JPanel {
	JLabel testLbl;
	detailedRepPanel() {
		testLbl = new JLabel("Test Detailed Report Nav");
		this.add(testLbl);
	}
}