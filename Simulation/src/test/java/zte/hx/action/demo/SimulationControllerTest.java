package zte.hx.action.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import zte.hx.tool.ui.highcharts.LineData;
import zte.hx.util.groovy.IOUtil;

public class SimulationControllerTest {
	private String jsonModel = null;

	@Before
	public void prepare() {
		String modelPath = "/zte/hx/action/demo/simple_model.json";
		jsonModel = (String) IOUtil.readAsString(modelPath);
	}

	@Test
	public void testSimulate() {
		assertNotNull(jsonModel);
		SimulationController sc = new SimulationController();
		LineData ld = sc.simulate(jsonModel);
		assertEquals("out", ld.getTitle());
		assertEquals(1, ld.getData().length);
		double startZeroOfFirstSeries = ld.getData()[0].getData()[0][0];
		assertEquals(0.0, startZeroOfFirstSeries, 0.01);
	}
}
