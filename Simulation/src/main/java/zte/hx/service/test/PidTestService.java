package zte.hx.service.test;

import zte.hx.simulation.test.PidTestDemo;
import zte.hx.tool.ui.highcharts.LineData;
import zte.hx.tool.ui.highcharts.LineDataBuilder;

public class PidTestService {
	private PidTestDemo demo;

	public void simulate() {
		demo = new PidTestDemo();
		demo.simulate();
	}

	public TestView getView() {
		LineData pout = LineDataBuilder.createBuilder("p out", "t/s", "p")
				.addSeries("", demo.getTime(), demo.getpOut()).build();
		LineData iout = LineDataBuilder.createBuilder("i out", "t/s", "i")
				.addSeries("", demo.getTime(), demo.getiOut()).build();
		LineData dout = LineDataBuilder.createBuilder("d out", "t/s", "d")
				.addSeries("", demo.getTime(), demo.getdOut()).build();
		return new TestView(pout, iout, dout);
	}

	public static class TestView {
		LineData pout;
		LineData iout;
		LineData dout;

		public TestView(LineData pout, LineData iout, LineData dout) {
			super();
			this.pout = pout;
			this.iout = iout;
			this.dout = dout;
		}

		public LineData getPout() {
			return pout;
		}

		public LineData getIout() {
			return iout;
		}

		public LineData getDout() {
			return dout;
		}
	}
}
