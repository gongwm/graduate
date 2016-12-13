package zte.hx.test;

public class HelloWorld {
	public String sayHello() {
		return "hello, hx";
	}

	public static void main(String[] args) {
		System.out.println(new HelloWorld().sayHello());
	}
}
