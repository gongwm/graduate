package zte.hx.action.demo;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import groovy.json.JsonSlurper;
import zte.hx.simulation.demo.block.Scope;
import zte.hx.simulation.demo.block.Simulator;
import zte.hx.tool.ui.highcharts.LineData;
import zte.hx.tool.ui.highcharts.LineDataBuilder;
import zte.hx.util.LangUtil;

@Controller
public class SimulationController {
	@ResponseBody
	@PostMapping(path = "/simulate")
	@SuppressWarnings("unchecked")
	public LineData simulate(@RequestParam("model") String jsonModel) {
		JsonSlurper js = new JsonSlurper();
		Object obj = js.parseText(jsonModel);

		Simulator simulator = new Simulator();
		simulator.initSystem((Map<String, Object>) obj);
		simulator.simulate();

		Map<String, Scope> scopes = (Map<String, Scope>) simulator
				.findOutputs();

		scopes.forEach((name, scope) -> {
			System.out.println(scope);
		});

		LineDataBuilder ldb = LineDataBuilder.createBuilder("out", "t/s", "y");
		double[] time = (double[]) simulator.getTime();

		scopes.forEach((name, scope) -> {
			ldb.addSeries(name, time,
					LangUtil.toPrimitiveDoubleArray(scope.getData()));
		});
		return ldb.build();
	}
}
