package nl.appelgebakje22.xdata.serializers;

import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkAdapter;
import nl.appelgebakje22.xdata.adapter.NumberAdapter;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import org.jetbrains.annotations.Nullable;

public class DoubleSerializer extends Serializer<Double> {

	@Override
	public @Nullable BaseAdapter serialize(final Reference ref, final AdapterFactory adapters) {
		return adapters.ofDouble(this.getData());
	}

	@Override
	public void deserialize(final Reference ref, final AdapterFactory adapters, final BaseAdapter adapter) {
		final NumberAdapter<?> numberAdapter = this.testAdapter(adapter, NumberAdapter.class);
		this.setData(numberAdapter.getDouble());
	}

	@Override
	public void toNetwork(final Reference ref, final NetworkAdapter network) {
		network.write(this.getData());
	}

	@Override
	public void fromNetwork(final Reference ref, final NetworkAdapter network) {
		this.setData(network.readDouble());
	}

	public static DoubleSerializer of(final double data) {
		return XData.make(new DoubleSerializer(), serializer -> serializer.setData(data));
	}
}
