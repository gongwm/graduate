package hust.hx.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LearningTest {

	Calculator c1;
	Calculator c2;

	@Before
	public void setUp() {
		c1 = new Calculator(1.0, 2.0);
		c2 = new Calculator(1.0, 0.0);
	}

	@Test
	public void testSum() {
		assertEquals(3.0, c1.sum(), 0.0);
		assertEquals(1.0, c2.sum(), 0.0);
	}

	@Test
	public void testDivideByZeroReturnsInfinity() {
		assertTrue(Double.isInfinite(c2.divide()));
	}

}
