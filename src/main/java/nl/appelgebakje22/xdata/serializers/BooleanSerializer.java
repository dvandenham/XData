package nl.appelgebakje22.xdata.serializers;

import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.BooleanAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkAdapter;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import org.jetbrains.annotations.Nullable;

public class BooleanSerializer extends Serializer<Boolean> {

	@Override
	public @Nullable BaseAdapter serialize(final Reference ref, final AdapterFactory adapters) {
		return adapters.ofBoolean(this.getData());
	}

	@Override
	public void deserialize(final Reference ref, final AdapterFactory adapters, final BaseAdapter adapter) {
		final BooleanAdapter booleanAdapter = this.testAdapter(adapter, BooleanAdapter.class);
		this.setData(booleanAdapter.getBoolean());
	}

	@Override
	public void toNetwork(final Reference ref, final NetworkAdapter network) {
		network.write(this.getData());
	}

	@Override
	public void fromNetwork(final Reference ref, final NetworkAdapter network) {
		this.setData(network.readBoolean());
	}

	public static BooleanSerializer of(final boolean data) {
		return XData.make(new BooleanSerializer(), serializer -> serializer.setData(data));
	}
}
