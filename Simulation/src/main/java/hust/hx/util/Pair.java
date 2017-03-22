package hust.hx.util;

public class Pair<T, K> {
	public final T first;
	public final K second;

	public Pair(T first, K second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public String toString() {
		return String.format("(%s,%s)", first, second);
	}

	public static <T, K> Pair<T, K> of(T first, K second) {
		return new Pair<T, K>(first, second);
	}
}