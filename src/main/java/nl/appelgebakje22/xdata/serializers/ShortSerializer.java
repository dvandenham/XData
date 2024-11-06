package nl.appelgebakje22.xdata.serializers;

import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkAdapter;
import nl.appelgebakje22.xdata.adapter.NumberAdapter;
import nl.appelgebakje22.xdata.api.Serializer;
import org.jetbrains.annotations.Nullable;

public class ShortSerializer extends Serializer<Short> {

	@Override
	public @Nullable BaseAdapter serialize(AdapterFactory adapters) {
		return adapters.ofShort(getData());
	}

	@Override
	public void deserialize(AdapterFactory adapters, BaseAdapter adapter) {
		NumberAdapter<?> numberAdapter = this.testAdapter(adapter, NumberAdapter.class);
		setData(numberAdapter.getShort());
	}

	@Override
	public void toNetwork(NetworkAdapter network) {
		network.write(getData());
	}

	@Override
	public void fromNetwork(NetworkAdapter network) {
		setData(network.readShort());
	}

	public static ShortSerializer of(short data) {
		return XData.make(new ShortSerializer(), serializer -> serializer.setData(data));
	}
}
