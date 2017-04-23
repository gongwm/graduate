package hust.hx.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadTest {

	public static void main(String[] args) throws Exception {
		ExecutorService es = Executors.newFixedThreadPool(4);
		int age = 0;
		ThreadLocal<Integer> n = new ThreadLocal<Integer>() {
			public Integer initialValue() {
				return 0;
			}
		};

		while (age < 10) {
			for (int i = 0; i < 4; ++i) {
				es.execute(new Runnable() {
					@Override
					public void run() {
						Integer c = n.get();
						Integer cc = c + 1;
						n.set(cc);
						System.out.println(Thread.currentThread().getName() + "   " + n.get());
					}
				});
			}
			Thread.sleep(1000);
			System.out.println("");
			++age;
		}
		es.awaitTermination(0, TimeUnit.SECONDS);
		es.shutdown();
	}

}
