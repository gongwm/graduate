package hust.hx.simulation.demo;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import hust.hx.util.TestUtil;

/**
 * 很慢。
 * 
 * @author hx
 *
 */
public class ScriptEngineTest {
	public static void main(String[] args) {
		ScriptEngine se = new ScriptEngineManager()
				.getEngineByName("JavaScript");
		String str = "1+2.0";
		try {
			TestUtil.timeIt(() -> {
				for (int i = 0; i < 10000; ++i) {
					try {
						se.eval(str);
					} catch (ScriptException e) {
						e.printStackTrace();
					}
				}
			});
			Double d = (Double) se.eval(str);
			System.out.println(d);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}
}
