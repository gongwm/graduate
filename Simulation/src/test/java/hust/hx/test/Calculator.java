package hust.hx.test;

public class Calculator {
	private double a;
	private double b;

	Calculator(double a, double b) {
		this.a = a;
		this.b = b;
	}

	double sum() {
		return a + b;
	}

	double divide() {
		return a / b;
	}
}