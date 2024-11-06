package nl.appelgebakje22.xdata.adapter;

import nl.appelgebakje22.xdata.XData;

public class UnknownTypeAdapter implements BaseAdapter {

	private Object value;

	public void set(Object value) {
		if (value == null) {
			throw new IllegalStateException("Cannot set null as value! Use an %s instead!".formatted(NullTypeAdapter.class));
		}
		this.value = value;
	}

	public Object get() {
		return this.value;
	}

	public Object getOrDefault(Object fallback) {
		return this.value != null ? this.value : fallback;
	}

	public static UnknownTypeAdapter of(Object data) {
		return XData.make(new UnknownTypeAdapter(), adapter -> adapter.set(data));
	}
}
