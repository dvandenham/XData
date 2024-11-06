package nl.appelgebakje22.xdata.serializers;

import java.util.UUID;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkAdapter;
import nl.appelgebakje22.xdata.adapter.StringAdapter;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import org.jetbrains.annotations.Nullable;

public class UUIDSerializer extends Serializer<UUID> {

	@Override
	public @Nullable BaseAdapter serialize(Reference ref, AdapterFactory adapters) {
		return adapters.ofString(getData().toString());
	}

	@Override
	public void deserialize(Reference ref, AdapterFactory adapters, BaseAdapter adapter) {
		StringAdapter stringAdapter = this.testAdapter(adapter, StringAdapter.class);
		setData(UUID.fromString(stringAdapter.getString()));
	}

	@Override
	public void toNetwork(Reference ref, NetworkAdapter network) {
		network.write(getData().toString());
	}

	@Override
	public void fromNetwork(Reference ref, NetworkAdapter network) {
		setData(UUID.fromString(network.readString()));
	}

	public static UUIDSerializer of(UUID data) {
		return XData.make(new UUIDSerializer(), serializer -> serializer.setData(data));
	}
}
