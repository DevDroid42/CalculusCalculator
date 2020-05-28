package main;

import java.awt.Color;
import java.util.*;

public class Calculator {
	public List<FunctionData> functions = new ArrayList<FunctionData>();
	private ExpressionEval eval = new ExpressionEval();
	//global variable as would exist on ti-84
	public static double x;

	public Calculator() {
		for (int i = 0; i < 10; i++) {
			functions.add(new FunctionData());
		}
	}

	public void setFunction(String function, int index, FunctionData.FunctionType type) {
		functions.set(index, new FunctionData(function, type));
	}

	public void displayFunctions() {
		for (int i = 0; i < functions.size(); i++) {
			System.out.println(functions.get(i).color.toString().substring(14) + "\tType:"
					+ functions.get(i).type.toString() + "\t Y" + i + "=" + functions.get(i).function);
		}
	}

	private double derivitive(String function, double x) {
		double f1 = Double.parseDouble(eval.Evaluate(function, x));
		double f2 = Double.parseDouble(eval.Evaluate(function, x + 0.0000001));
		return (f1 - f2) / (x - (x + 0.0000001));
	}

	private double integral(String function, double x) {
		System.out.println("Integral not implemented yet");
		return 0;
	}

	/**
	 * 
	 * @return returns the lowest index of an empty function. returns the last
	 *         function if all are filled
	 */
	public int getNextEmptyFunc() {
		int i;
		for (i = 0; i < functions.size(); i++) {
			if (functions.get(i).function.equals(""))
				return i;
		}
		return i;
	}

	public void clearFunc() {
		for (int i = 0; i < functions.size(); i++) {
			functions.set(i, new FunctionData("", FunctionData.FunctionType.normal));
		}
	}
	
	public void clearFunc(int index) {
		functions.set(index, new FunctionData());
	}

	/**
	 * handles function nesting and calc operations
	 */
	public double InterpretFunc(int index, double x) {
		String y = functions.get(index).function;
		y = formatNested(y);
		y = formatVariables(y);
		if (Main.debug)
			System.out.println("Interpreting function: " + y);

		switch (functions.get(index).type) {
		case normal:
			return Double.parseDouble(eval.Evaluate(y, x));
		case derivative:
			return derivitive(y, x);
		case integral:
			return integral(y, x);
		default:
			System.out.println("Error invalid fucntion type");
			return 0;
		}
	}

	public double zero(int index, double guess) {
		double y = InterpretFunc(index,guess);
		boolean currentSign = y > 0;
		x = guess;
		for (int i = 0; i < 10000; i++) {
			// if the sign changes we have crossed 0 when adding. Hone in at i moving
			// backwards with more precision.
			if (InterpretFunc(index, x + i) > 0 != currentSign) {
				if (Main.debug)
					System.out.println("Detected sign change at " + (x + i));
				return hone(index, x + i, -0.1);
				// if we've crossed 0 while subtracting hone in moving forward
			} else if (InterpretFunc(index, x - i) > 0 != currentSign) {
				if (Main.debug)
					System.out.println("Detected sign change at " + (x - i));
				return hone(index, x - i, 0.1);
			}
		}
		System.out.println("no sign change detected");
		return 0;
	}

	private double hone(int index, double x, double iterator) {
		if(Main.debug)
			System.out.println("honing in at" + x + " moving: " + iterator);
		boolean currentSign = InterpretFunc(index, x) > 0;
		for (int i = 0; i < 10000; i++) {
			double currentNum = InterpretFunc(index, x + i * iterator);
			if (Math.abs(currentNum) < 0.00000001) {
				return x + i * iterator;
			} else if (InterpretFunc(index, x + i * iterator) > 0 != currentSign) {
				return hone(index, x + i * iterator, iterator * -0.5);
			}
		}
		System.out.println("error no secondary sign change detected");
		return 0;
	}

	private String formatNested(String y) {
		int yIndex = 0;
		while (y.contains("Y")) {
			yIndex = y.indexOf("Y");
			y = eval.sub(y, "(" + functions.get(Integer.parseInt(y.substring(yIndex + 1, yIndex + 2))).function + ")",
					yIndex, yIndex + 2);
			if (!y.contains("Y"))
				break;
		}
		return y;
	}

	private String formatVariables(String y) {
		for (int i = 0; i < y.length(); i++) {
			if (y.charAt(i) == 'x') {
				y = eval.sub(y, "(x)", i, i + 1);
				i++;
			}
		}
		return y;
	}

}

class FunctionData {
	public FunctionType type = FunctionType.normal;
	public String function = "";
	public Color color;

	public enum FunctionType {
		normal, derivative, integral
	};

	public FunctionData(String function, FunctionType type) {
		this.function = function;
		this.type = type;
		this.color = Color.white;
	}

	public FunctionData() {
		this.function = "";
		this.type = FunctionType.normal;
		this.color = Color.white;
	}

}
