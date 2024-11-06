package nl.appelgebakje22.xdata.adapter;

public interface BaseObjectAdapter<TYPE> extends BaseAdapter {

	void set(TYPE value);

	TYPE get();

	TYPE getOrDefault(TYPE fallback);
}
