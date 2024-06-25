package launcher;

import javax.swing.SwingUtilities;

import logic.Calculator;
import view.MainWindow;

public class Main {
	
	private static Calculator _calculator;
	
	private static void start_calculator() throws Exception {
		_calculator = new Calculator();
		
		SwingUtilities.invokeAndWait(() -> new MainWindow(_calculator));
	}
	
	public static void main(String[] args) {
		try {
			start_calculator();
		} catch (Exception e) {
			System.err.println("Something went wrong...");
			System.err.println();
			e.printStackTrace();
		}
	}
}
