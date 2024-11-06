package nl.appelgebakje22.xdata.serializers;

import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkAdapter;
import nl.appelgebakje22.xdata.adapter.StringAdapter;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import org.jetbrains.annotations.Nullable;

public class StringSerializer extends Serializer<String> {

	@Override
	public @Nullable BaseAdapter serialize(final Reference ref, final AdapterFactory adapters) {
		return adapters.ofString(this.getData());
	}

	@Override
	public void deserialize(final Reference ref, final AdapterFactory adapters, final BaseAdapter adapter) {
		final StringAdapter stringAdapter = this.testAdapter(adapter, StringAdapter.class);
		this.setData(stringAdapter.getString());
	}

	@Override
	public void toNetwork(final Reference ref, final NetworkAdapter network) {
		network.write(this.getData());
	}

	@Override
	public void fromNetwork(final Reference ref, final NetworkAdapter network) {
		this.setData(network.readString());
	}

	public static StringSerializer of(final String data) {
		return XData.make(new StringSerializer(), serializer -> serializer.setData(data));
	}
}
