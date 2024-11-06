package nl.appelgebakje22.xdata.api;

import lombok.Getter;
import lombok.Setter;
import nl.appelgebakje22.xdata.XDataRegister;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkAdapter;
import org.jetbrains.annotations.Nullable;

public abstract class Serializer<T> {

	@Getter
	@Setter
	private T data;

	@Nullable
	public abstract BaseAdapter serialize(AdapterFactory adapters);

	public abstract void deserialize(AdapterFactory adapters, BaseAdapter adapter);

	public abstract void toNetwork(NetworkAdapter network);

	public abstract void fromNetwork(NetworkAdapter network);

	protected <A extends BaseAdapter> A testAdapter(BaseAdapter adapter, Class<A> expectedType) {
		if (expectedType.isAssignableFrom(adapter.getClass())) {
			return expectedType.cast(adapter);
		}
		throw new IllegalArgumentException("Expected Adapter of type %s, got: %s".formatted(expectedType.getName(), adapter.getClass().getName()));
	}

	public final int getSid() {
		return XDataRegister.getSerializerId(this);
	}
}
