package nbt;

import nl.appelgebakje22.xdata.adapter.BaseObjectAdapter;

class NbtObjectAdapter<TYPE> extends NbtAdapter implements BaseObjectAdapter<TYPE> {

	private TYPE value;

	@Override
	public void set(TYPE value) {
		this.value = value;
	}

	@Override
	public TYPE get() {
		return this.value;
	}

	@Override
	public TYPE getOrDefault(TYPE fallback) {
		return this.value != null ? this.value : fallback;
	}
}
