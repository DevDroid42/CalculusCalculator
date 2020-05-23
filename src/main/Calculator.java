package main;

import java.util.*;

public class Calculator {
	private List<FunctionData> functions = new ArrayList<FunctionData>();

	private ExpressionEval eval = new ExpressionEval();

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
			System.out.println("Type:" + functions.get(i).type.toString() + "\t Y" + i + "=" + functions.get(i).function);
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
	 * handles function nesting and calc operations
	 */
	public double InterpretFunc(int index, double x) {
		String y = functions.get(index).function;
		int yIndex = 0;
		while (y.contains("Y")) {
			yIndex = y.indexOf("Y");
			y = eval.sub(y, "(" + functions.get(Integer.parseInt(y.substring(yIndex + 1, yIndex + 2))).function + ")",
					yIndex, yIndex + 2);
			if (!y.contains("Y"))
				break;
		}

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

}

class FunctionData {
	public enum FunctionType {
		normal, derivative, integral
	};

	public FunctionData(String function, FunctionType type) {
		this.function = function;
		this.type = type;
	}

	public FunctionData() {
		this.function = "";
		this.type = FunctionType.normal;
	}

	public FunctionType type = FunctionType.normal;
	public String function = "";
}
