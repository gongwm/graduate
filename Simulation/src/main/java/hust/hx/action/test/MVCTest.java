package hust.hx.action.test;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MVCTest {
	@ResponseBody
	@RequestMapping(path = "/test.action", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<String> test() {
		return Arrays.asList("hx", "hx");
	}

	public static class Person {
		String name;
		int age;

		public Person(String name, int age) {
			this.name = name;
			this.age = age;
		}

		public String getName() {
			return name;
		}

		public int getAge() {
			return age;
		}
	}
}
