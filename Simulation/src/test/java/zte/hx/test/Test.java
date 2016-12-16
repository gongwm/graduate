package zte.hx.test;

import zte.hx.test.HelloWorld;
import zte.hx.test.HelloWorldGroovy;
import zte.hx.util.TestUtil;

public class Test {
	public static void main(String[] args) {
		HelloWorld h = new HelloWorld();
		TestUtil.print(h.sayHello());
		
		HelloWorldGroovy hw=new HelloWorldGroovy();
		TestUtil.print(hw.sayHello());
	}
}
