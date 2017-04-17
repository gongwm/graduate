package hust.hx.action.demo;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import groovy.json.JsonSlurper;
import hust.hx.simulation.demo.block.Scope;
import hust.hx.simulation.demo.block.Simulator;
import hust.hx.simulation.util.PrintUtil;
import hust.hx.tool.ui.highcharts.LineData;
import hust.hx.tool.ui.highcharts.LineDataBuilder;
import hust.hx.util.LangUtil;

@Controller
public class SimulationController {
	boolean flag = true;

	@ResponseBody
	@PostMapping(path = "/simulate")
	@SuppressWarnings("unchecked")
	public LineData simulate(@RequestParam("model") String jsonModel) {
		JsonSlurper js = new JsonSlurper();
		Object obj = js.parseText(jsonModel);

		Simulator simulator = new Simulator();
		simulator.initSystem((Map<String, Object>) obj);
		simulator.simulate();

		Map<String, Scope> scopes = (Map<String, Scope>) simulator.findOutputs();

		LineDataBuilder ldb = LineDataBuilder.createBuilder("out", "t/s", "y");
		double[] time = (double[]) simulator.getTime();

		scopes.forEach((name, scope) -> {
			printOnce(time, scope);
			ldb.addSeries(name, time, LangUtil.toPrimitiveDoubleArray(scope.getData()));
		});

		flag = true;
		return ldb.build();
	}

	private void printOnce(double[] time, Scope scope) {
		if (flag) {
			flag = false;
			PrintUtil.print(p -> {
				LangUtil.zipDoubleArray(time, LangUtil.toPrimitiveDoubleArray(scope.getData())).forEach(pair -> {
					p.println(String.format("%f %f", pair.first, pair.second));
				});
			});
		}
	}
}
