package hust.hx.test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class StreamTest {
	private static void f(A a) {
		a.f();
	}

	public static void main(String[] args) {
		f(() -> System.out.println("haha"));

		List<Integer> list = Arrays.asList(1, 2, 3);
		Stream<Integer> s = list.stream();
		s.forEach(System.out::println);
		// s.forEach(System.out::print); //无法重新调用。

	}

}

@FunctionalInterface
interface A {
	void f();
}
