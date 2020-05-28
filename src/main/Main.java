package main;

import java.util.Scanner;

public class Main {
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
	
	public static String helpText = "Help menu. <> indicates an argument"
			+ "\nhelp -Displays this help menu"
			+ "\nsf -set Function. Can be used in two ways. This way allows you to set all properties at once"
			+ "\n\tsf <function> <function index(0-9)> <functionType (0=normal 1=derivitive 2=integral)>"
			+ "\n\tThis way lets you set the next free function:"
			+ "\n\tsf <function>"
			+ "\nsft -set Function Type. used to set the function type"
			+ "\n\tsft <function index> <functionType (0=normal 1=derivitive 2=integral)>"
			+ "\nview -displays a menu of all the functions"
			+ "\ngraph -will open a graphical graphing window and graph all functions in memory. if used"
			+ "\nwithout arguments it will use the last bounds set"
			+ "\n\tgraph <xLower> <xUpper> <yLower> <yUpper>"
			+ "\ndebug -enable or disable debug information"
			+ "\n\tdebug <true/false>"
			+ "\nclear -clears the screen"
			+ "\neval -allows you to evaluate a function at a given x value"
			+ "\n\teval <function index> <x val>"
			+ "\nexit -will close the program"
			+ "\nif a command is not recognized input will be treated as a mathematical expression";
}
