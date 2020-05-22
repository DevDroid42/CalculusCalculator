package main;

import java.util.Scanner;

public class Main {
	public static String helpText = "Commands and args are separated by spaces. <> indicates an argument"
			+ "\nhelp, setFunction <function index(0-9)> <function> <functionType (0=normal 1=derivitive 2=integral)>, "
			+ "\n view, graph, debug <true/false>, clear"
			+ "\n\n if a command is not recognized input will be treated as a mathematical expression "
			+ "There is currently no checking for syntax errors as I ran out of time.";
	public static boolean debug = false;

	public static void main(String args[]) {
		Main main = new Main();
		main.run();
	}

	CommandManager command = new CommandManager();
	Scanner sc = new Scanner(System.in);
	public void run() {
		command.ParseInput(sc.nextLine());
	}
}
