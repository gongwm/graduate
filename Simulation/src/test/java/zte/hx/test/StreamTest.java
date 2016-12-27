package zte.hx.test;

import java.util.Arrays;
import java.util.List;

public class StreamTest {
	public static void main(String[] args) {
		List<Integer> numbers=Arrays.asList(1,2,3);
		numbers.stream().allMatch(n-> n>0);
	}
}
