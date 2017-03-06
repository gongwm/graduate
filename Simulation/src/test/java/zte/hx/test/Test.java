package zte.hx.test;

import java.util.ArrayList;
import java.util.List;

public class Test {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		List<Integer>[] l = new List[4];
		Object o = l;
		Object[] oo = (Object[]) o;
		List<String> s = new ArrayList<String>();
		s.add("asdf");
		oo[0] = s;
		System.out.println(((List<String>) oo[0]).get(0));
	}
}
