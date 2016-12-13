package zte.hx.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import zte.hx.util.TestUtil;

public class SpringKick {
	public String sayHello() {
		return "hello, spring";
	}

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"test.xml");
		SpringKick kick = (SpringKick) context.getBean("springKick");
		TestUtil.print(kick.sayHello());
		context.close();
	}
}
