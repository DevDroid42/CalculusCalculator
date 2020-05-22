package main;

public class Main {
	public static void main(String args[]) {
		ExpressionEval x = new ExpressionEval();
		String y = "&20359.";
		//System.out.println(x.parenthesisIndex("()"));
		//System.out.println(x.isolateExpression("5*9", 1));
		System.out.println(x.Evaluate("(3+(5*9))^7.4+9"));
	}
}
