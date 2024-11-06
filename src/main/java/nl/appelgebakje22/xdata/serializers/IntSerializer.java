package nl.appelgebakje22.xdata.serializers;

import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkAdapter;
import nl.appelgebakje22.xdata.adapter.NumberAdapter;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import org.jetbrains.annotations.Nullable;

public class IntSerializer extends Serializer<Integer> {

	@Override
	public @Nullable BaseAdapter serialize(Reference ref, AdapterFactory adapters) {
		return adapters.ofInt(getData());
	}

	@Override
	public void deserialize(Reference ref, AdapterFactory adapters, BaseAdapter adapter) {
		NumberAdapter<?> numberAdapter = this.testAdapter(adapter, NumberAdapter.class);
		setData(numberAdapter.getInt());
	}

	@Override
	public void toNetwork(Reference ref, NetworkAdapter network) {
		network.write(getData());
	}

	@Override
	public void fromNetwork(Reference ref, NetworkAdapter network) {
		setData(network.readInt());
	}

	public static IntSerializer of(int data) {
		return XData.make(new IntSerializer(), serializer -> serializer.setData(data));
	}
}
