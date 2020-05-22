package main;

import java.util.Scanner;

public class Main {
	public static boolean debug = true;

	public static void main(String args[]) {
		Main main = new Main();
		main.run();
	}

	public void run() {
		Scanner sc = new Scanner(System.in);
		Calculator calc = new Calculator();
		calc.setFunction(sc.nextLine(), 0, FunctionData.FunctionType.normal);
		calc.InterpretFunc(0, 5);
		calc.displayFunctions();
		calc.setFunction("3*Y0+x", 1, FunctionData.FunctionType.normal);
		calc.setFunction("Y1", 2, FunctionData.FunctionType.normal);
		calc.displayFunctions();
		calc.InterpretFunc(1, 5);
		calc.InterpretFunc(2, 3);
	}
}
