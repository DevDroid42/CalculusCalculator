package main;

import java.util.Scanner;

public class Main {
	public static String helpText = "Commands and args are separated by spaces. <> indicates an argument"
			+ "\nhelp, sf <function> <function index(0-9)> <functionType (0=normal 1=derivitive 2=integral)>, "
			+ "\nview, graph, debug <true/false>, clear, evaluate <function index> <x val>, exit"
			+ "\n\n if a command is not recognized input will be treated as a mathematical expression ";
	public static boolean debug = false;
	public static boolean running = true;

	public static void main(String args[]) {
		Main main = new Main();
		main.run();
	}

	CommandManager command = new CommandManager();
	Scanner sc = new Scanner(System.in);

	public void run() {
		while (running)
			command.ParseInput(sc.nextLine());
	}
}
