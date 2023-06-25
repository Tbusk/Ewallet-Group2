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
	

}
