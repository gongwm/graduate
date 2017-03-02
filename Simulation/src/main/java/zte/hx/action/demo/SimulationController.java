package zte.hx.action.demo;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import groovy.json.JsonSlurper;
import zte.hx.simulation.demo.block.Scope;
import zte.hx.simulation.demo.block.Simulator;
import zte.hx.tool.ui.highcharts.LineData;
import zte.hx.tool.ui.highcharts.LineDataBuilder;
import zte.hx.util.LangUtil;

public class SimulationController {
	@ResponseBody
	@PostMapping(path = "/simulate")
	@SuppressWarnings("unchecked")
	public LineData simulate(@RequestBody String jsonModel) {
		JsonSlurper js = new JsonSlurper();
		Object obj = js.parseText(jsonModel);

		Simulator simulator = new Simulator();
		simulator.initSystem((Map<String, Object>) obj);
		simulator.simulate();

		Map<String, Scope> scopes = (Map<String, Scope>) simulator.findOutputs();

		LineDataBuilder ldb = LineDataBuilder.createBuilder("out", "t/s", "y");
		double[] time = (double[]) simulator.getTime();

		scopes.forEach((name, scope) -> {
			ldb.addSeries(name, time, LangUtil.toPrimitiveDoubleArray(scope.getData()));
		});
		return ldb.build();
	}
}
