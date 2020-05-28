package main;

public class ExpressionEval {
	private char[] decimals = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', 'E', '-' };

	public String Evaluate(String expression, Double in) {
		expression = removeSpaces(expression);
		expression = formatSubtraction(expression);
		expression = implicitMult(expression);
		while (expression.contains("x")) {
			expression = sub(expression, in.toString(), expression.indexOf("x"), expression.indexOf("x") + 1);
			if (Main.debug)
				System.out.println(expression);
		}
		return EvaluateR(expression);
	}

	public String Evaluate(String expression) {
		expression = removeSpaces(expression);
		expression = formatSubtraction(expression);
		expression = implicitMult(expression);
		/*while (expression.contains("x")) {
			expression = sub(expression, "" + Calculator.x, expression.indexOf("x"), expression.indexOf("x") + 1);
			if (Main.debug)
				System.out.println(expression);
		}*/
		return EvaluateR(expression);

	}

	private String EvaluateR(String expression) {
		if (Main.debug)
			System.out.println(expression);

		if (expression.contains("(")) {
			int start = expression.indexOf("(") + 1;
			int end = parenthesisIndex(expression);
			return EvaluateR(sub(expression, EvaluateR(expression.substring(start, end)), start - 1, end + 1));
		} else if (expression.contains("^")) {
			ExpressionData data = isolateExpression(expression, expression.indexOf('^'));
			return EvaluateR(sub(expression, power(data.expression), data.lower, data.upper));

		} else if (expression.contains("*") || expression.contains("/")) {
			if (decideMult(expression)) {
				ExpressionData data = isolateExpression(expression, expression.indexOf('*'));
				return EvaluateR(sub(expression, multiply(data.expression), data.lower, data.upper));
			} else {
				ExpressionData data = isolateExpression(expression, expression.indexOf('/'));
				return EvaluateR(sub(expression, divide(data.expression), data.lower, data.upper));
			}
		} else if (expression.contains("+") || expression.contains("~")) {
			if (decideAdd(expression)) {
				ExpressionData data = isolateExpression(expression, expression.indexOf('+'));
				return EvaluateR(sub(expression, add(data.expression), data.lower, data.upper));
			} else {
				ExpressionData data = isolateExpression(expression, expression.indexOf('~'));
				return EvaluateR(sub(expression, subtract(data.expression), data.lower, data.upper));
			}
		} else {
			return expression;
		}
	}

	public String implicitMult(String expression) {
		if (Main.debug)
			System.out.print("formating implicit multiplication: in:" + expression + "\tout:");
		expression = implcitInsert(expression, '(', ')', true, false);
		expression = implcitInsert(expression, ')', '(', false, true);
		if (Main.debug)
			System.out.println(expression);
		return expression;
	}

	/**
	 * adds multiplication symbols adjacent to given character if they are next to a
	 * number
	 * 
	 * @param expression
	 * @param character
	 * @param multChar   additional character to check against for multiplication
	 * @param checkBack  whether should check back for implicit multiplication
	 * @param checkFront whether should check front for implicit multiplication.
	 * @return
	 */
	private String implcitInsert(String expression, char character, char multChar, boolean checkBack,
			boolean checkFront) {
		for (int i = 0; i < expression.length(); i++) {
			if (expression.charAt(i) == character) {
				// if at the last character on the string don't check in front
				if (i != expression.length() - 1 && checkFront)
					if (isNumber(expression.charAt(i + 1)) || expression.charAt(i + 1) == multChar) {
						expression = sub(expression, "*", i + 1, i + 1);
					}

				// if at the first character of string don't check behind
				if (i != 0 && checkBack)
					if (isNumber(expression.charAt(i - 1)) || expression.charAt(i - 1) == multChar) {
						expression = sub(expression, "*", i , i);
					}
			}
		}
		return expression;
	}

	private String formatSubtraction(String in) {
		if (Main.debug)
			System.out.print("Formatting subtraction in:" + in + "\t");
		while (in.contains("-")) {
			in = sub(in, "~", in.indexOf("-"), in.indexOf("-") + 1);
		}
		if (Main.debug)
			System.out.println("Out: " + in);
		return in;
	}

	private String removeSpaces(String in) {
		while (in.contains(" ")) {
			in = sub(in, "", in.indexOf(" "), in.indexOf(" ") + 1);
		}
		return in;
	}

	/**
	 * determines if there are multiple multiplication and divisions in the
	 * expression. If not it chooses the only option. If there are it chooses the
	 * first one
	 * 
	 * @param expression
	 * @return returns true for * and false for /
	 */
	private boolean decideMult(String expression) {
		if (expression.contains("*") && !expression.contains("/")) {
			return true;
		} else if (!expression.contains("*") && expression.contains("/")) {
			return false;
		} else if (expression.indexOf('*') < expression.indexOf('/')) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * determines if there are multiple multiplication and divisions in the
	 * expression. If not it chooses the only option. If there are it chooses the
	 * first one
	 * 
	 * @param expression
	 * @return returns true for + and false for -
	 */
	private boolean decideAdd(String expression) {
		if (expression.contains("+") && !expression.contains("~")) {
			return true;
		} else if (!expression.contains("+") && expression.contains("~")) {
			return false;
		} else if (expression.indexOf('+') < expression.indexOf('~')) {
			return true;
		} else {
			return false;
		}
	}

	private ExpressionData isolateExpression(String expression, int index) {
		int lower = 0, upper = 0;
		for (int i = index - 1; i >= 0; i--) {
			if (!isNumber(expression.charAt(i))) {
				lower = i;
				break;
			}
			if (i == 0) {
				lower = i - 1;
			}
		}

		for (int i = index + 1; i < expression.length(); i++) {
			if (!isNumber(expression.charAt(i))) {
				upper = i - 1;
				break;
			}
			if (i == expression.length() - 1) {
				upper = i;
			}
		}
		// System.out.println("lower: " + lower + "\t upper: " + upper);
		return new ExpressionData(lower + 1, upper + 1, expression.substring(lower + 1, upper + 1));
	}

	/**
	 * evaluates an expression such as 2^3. Must only have two numbers and the
	 * carrot operator.
	 * 
	 * @param Expression
	 * @return
	 */
	private String power(String Expression) {
		double first = Double.parseDouble(Expression.substring(0, Expression.indexOf('^')));
		double second = Double.parseDouble(Expression.substring(Expression.indexOf('^') + 1));
		Double result = (Math.pow(first, second));
		return result.toString();
	}

	private String multiply(String Expression) {
		double first = Double.parseDouble(Expression.substring(0, Expression.indexOf('*')));
		double second = Double.parseDouble(Expression.substring(Expression.indexOf('*') + 1));
		Double result = first * second;
		return result.toString();
	}

	private String divide(String Expression) {
		double first = Double.parseDouble(Expression.substring(0, Expression.indexOf('/')));
		double second = Double.parseDouble(Expression.substring(Expression.indexOf('/') + 1));
		Double result = first / second;
		return result.toString();
	}

	private String add(String Expression) {
		double first = Double.parseDouble(Expression.substring(0, Expression.indexOf('+')));
		double second = Double.parseDouble(Expression.substring(Expression.indexOf('+') + 1));
		Double result = first + second;
		return result.toString();
	}

	private String subtract(String Expression) {
		double first = Double.parseDouble(Expression.substring(0, Expression.indexOf('~')));
		double second = Double.parseDouble(Expression.substring(Expression.indexOf('~') + 1));
		Double result = first - second;
		return result.toString();
	}

	/**
	 * returns a string with another string inserted in place of a portion of the
	 * input string. To insert without replacing set the start and end indexes equal
	 * 
	 * @param in
	 * @param sub
	 * @param start
	 * @param end
	 * @return
	 */
	public String sub(String in, String sub, int start, int end) {
		return in.substring(0, start) + sub + in.substring(end);
	}

	/**
	 * returns the index of the closing parenthesis
	 * 
	 * @return
	 */
	private int parenthesisIndex(String input) {
		int n = 0;
		for (int i = input.indexOf('(') + 1; i < input.length(); i++) {
			if (input.charAt(i) == '(')
				n++;
			if (input.charAt(i) == ')')
				n--;
			if (n < 0)
				return i;
		}
		System.out.println("Syntax error parenthesis");
		return 0;
	}

	public boolean isNumber(char character) {
		for (int i = 0; i < decimals.length; i++) {
			if (character == decimals[i])
				return true;
		}
		return false;
	}

}

//there is probably a better way to do this but this will work
class ExpressionData {
	public ExpressionData(int lower, int upper, String expression) {
		this.lower = lower;
		this.upper = upper;
		this.expression = expression;
	}

	// the original bounds of the expression to be used when subbing the evaluated
	// expression back in
	public int lower, upper;
	public String expression;
}