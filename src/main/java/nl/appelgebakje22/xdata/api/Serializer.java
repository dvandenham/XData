package nl.appelgebakje22.xdata.api;

import nl.appelgebakje22.xdata.XDataRegister;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.dummyclasses.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

public interface Serializer<T> {

	@Nullable
	BaseAdapter serialize(AdapterFactory adapters);

	void deserialize(AdapterFactory adapters, BaseAdapter adapter);

	void toNetwork(RegistryFriendlyByteBuf buf);

	void fromNetwork(RegistryFriendlyByteBuf buf);

	T getData();

	default <A extends BaseAdapter> A testAdapter(BaseAdapter adapter, Class<A> expectedType) {
		if (expectedType.isAssignableFrom(adapter.getClass())) {
			return expectedType.cast(adapter);
		}
		throw new IllegalArgumentException("Expected Adapter of type %s, got: %s".formatted(expectedType.getName(), adapter.getClass().getName()));
	}

	default int getSid() {
		return XDataRegister.getSerializerId(this);
	}
}
