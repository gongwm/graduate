package zte.hx.test;

import org.junit.Test;

import zte.hx.test.HelloWorld;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class HelloWorldTest {
	@Test
	public void testSayHello() {
		HelloWorld hw = new HelloWorld();
		assertThat("hello, hx", equalTo(hw.sayHello()));
	}
}
