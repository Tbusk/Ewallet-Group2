import java.util.Scanner;

public class Expense {
	Scanner scnr = new Scanner(System.in);
	String source;
	double amount;
	int yearlyfrequency; //1 for 1 time or once a year, 12 for monthly or or 24 for biweekly
	
	public String addSource() {
		System.out.println("Enter the Source of the expense.");
		source = scnr.nextLine();
		
		return source;
	}
	
	public Double addAmount() {
		System.out.println("Enter the amount of the expense.");
		amount = scnr.nextDouble();
		
		return amount;
	}
	
	public int addFrequency() {
		System.out.println("Enter the yearly frequency of the expense (1 for 1 time or once a year,"
				+ " 12 for monthly or or 24 for biweekly).");
		yearlyfrequency = scnr.nextInt();
		
		return yearlyfrequency;
	}


}
