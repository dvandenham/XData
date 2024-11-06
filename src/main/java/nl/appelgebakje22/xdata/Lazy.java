package nl.appelgebakje22.xdata;

import java.util.function.Supplier;

public class Lazy<T> {

	private final Object lock = new Object();
	private final Supplier<T> provider;
	private T value;
	private boolean loaded;

	private Lazy(Supplier<T> provider) {
		this.provider = provider;
	}

	public T get() {
		synchronized (this.lock) {
			if (!loaded && this.provider != null) {
				this.loaded = true;
				this.value = this.provider.get();
			}
			return this.value;
		}
	}

	public static <T> Lazy<T> of(Supplier<T> provider) {
		return new Lazy<>(provider);
	}

	public static <T> Lazy<T> of(T value) {
		return new Lazy<>(() -> value);
	}
}
