package hust.hx.simulation.demo.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import hust.hx.algorithm.gsa.ClassicGSA;
import hust.hx.algorithm.gsa.ClassicGSA.Range;

public class Paper2_2Java {

	static double fitness(List<Double> origin, List<Double> output) {
		double res = 0.0;
		for (int i = 0; i < output.size(); ++i) {
			res += Math.pow(output.get(i) - origin.get(i), 2);
		}
		return res;
	}

	public static void main(String[] args) {
		RegularSys rs = new RegularSys(0.8, 3.36, 0.2, 0.05);
		rs.simulate();
		List<Double> origin = rs.getOutput();

		ThreadLocal<RegularSys> tr = new ThreadLocal<RegularSys>() {
			@Override
			public RegularSys initialValue() {
				return new RegularSys(0.1, 0.1, 0.1, 0.1);
			}
		};
		ExecutorService es = Executors.newFixedThreadPool(8);

		ClassicGSA u = new ClassicGSA((cordinate) -> {
			double bt = cordinate[0];
			double td = cordinate[1];
			List<Double> output = null;

			Future<List<Double>> f = es.submit(new Callable<List<Double>>() {
				@Override
				public List<Double> call() throws Exception {
					RegularSys sys = tr.get();
					sys.reset(bt, td, 0.2, 0.05);
					sys.simulate();
					return new ArrayList<>(sys.getOutput());

				}
			});
			try {
				output = f.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			return fitness(origin, output);
		}, Arrays.asList(Range.of(0.001, 1), Range.of(0.001, 5)));
		u.configure(1000, 50);
		u.rockAndRoll();
		
		try {
			es.awaitTermination(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		es.shutdown();
	}

}
