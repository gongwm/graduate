package hust.hx.simulation.demo.block;

import java.util.Arrays;
import java.util.List;

import hust.hx.util.LangUtil;

public class RegularSys {
	Integrator int1;
	Homopoly1 homo1_1;
	Inertia inertia1;
	List<Block> components;
	List<Line> lines;
	Scope scope;
	Scope scopeTy;

	Config config = Config.of(0.01, 30);

	RegularSys(double bt, double td, double ty, double ti) {
		init(bt, td, ty, ti);
	}

	private void init(double bt, double td, double ty, double ti) {
		Block step = new StepSource();
		Adder adder1 = new Adder();
		Homopoly homo1 = new Homopoly(1.0, 1.0, 1.0);
		Adder adder2 = new Adder();

		int1 = new Integrator(1 / ti);
		homo1_1 = new Homopoly1(bt * td, td);

		Block amp1 = new Amplifier(0.0);
		Adder adder3 = new Adder();

		inertia1 = new Inertia(1, ty);
		scopeTy = new Scope();

		Homopoly homo2 = new Homopoly(1, -1, 0.5);
		Inertia inertia2 = new Inertia(1, 5);

		scope = new Scope();

		Line l1 = Line.of(step, adder1);
		Line l2 = Line.of(adder1, homo1);
		Line l3 = Line.of(homo1, adder2);
		Line l4 = Line.of(adder2, int1);
		Line l5 = Line.of(int1, inertia1);
		Line l6 = Line.of(inertia1, homo2);
		Line lty = Line.of(inertia1, scopeTy);

		Line l7 = Line.of(homo2, inertia2);
		Line l8 = Line.of(inertia2, scope);

		Line l9 = Line.of(int1, homo1_1);
		Line l10 = Line.of(homo1_1, adder3);
		Line l11 = Line.of(int1, amp1);
		Line l12 = Line.of(amp1, adder3);
		Line l13 = Line.of(adder3, adder2);
		Line l14 = Line.of(inertia2, adder1);

		inertia1.setConfig(config);
		inertia2.setConfig(config);
		int1.setConfig(config);
		homo1.setConfig(config);
		homo2.setConfig(config);
		homo1_1.setConfig(config);

		adder1.addLine(l1, Adder.ADD);
		adder1.addLine(l14, Adder.SUB);

		adder2.addLine(l3, Adder.ADD);
		adder2.addLine(l13, Adder.SUB);

		adder3.addLine(l10, Adder.ADD);
		adder3.addLine(l12, Adder.ADD);

		components = Arrays.asList(step, adder1, homo1, adder2, int1, homo1_1, amp1, adder3, inertia1, homo2, inertia2,
				scope);
		lines = Arrays.asList(l1, l2, l3, l4, l5, l6, lty, l7, l8, l9, l10, l11, l12, l13, l14);
	}

	void reset(double bt, double td, double ty, double ti) {
		int1.config(1 / ti);
		homo1_1.config(bt * td, td);
		inertia1.config(1, ty);
		components.stream().forEach(b -> b.setInitValue(0.0));
		lines.stream().forEach(l -> l.init());
		config.reset();
	}

	void simulate() {
		config.iterate(() -> {
			lines.stream().forEach(l -> l.push());
			components.stream().forEach(c -> c.moveOn());
		});
	}

	List<Double> getTime() {
		return LangUtil.toList(config.getTime());
	}

	List<Double> getOutput() {
		return scope.getData();
	}

	List<Double> getTyOutput() {
		return scopeTy.getData();
	}
}
