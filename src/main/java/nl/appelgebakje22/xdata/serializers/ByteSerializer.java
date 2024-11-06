package nl.appelgebakje22.xdata.serializers;

import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkAdapter;
import nl.appelgebakje22.xdata.adapter.NumberAdapter;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import org.jetbrains.annotations.Nullable;

public class ByteSerializer extends Serializer<Byte> {

	@Override
	public @Nullable BaseAdapter serialize(final Reference ref, final AdapterFactory adapters) {
		return adapters.ofByte(this.getData());
	}

	@Override
	public void deserialize(final Reference ref, final AdapterFactory adapters, final BaseAdapter adapter) {
		final NumberAdapter<?> numberAdapter = this.testAdapter(adapter, NumberAdapter.class);
		this.setData(numberAdapter.getByte());
	}

	@Override
	public void toNetwork(final Reference ref, final NetworkAdapter network) {
		network.write(this.getData());
	}

	@Override
	public void fromNetwork(final Reference ref, final NetworkAdapter network) {
		this.setData(network.readByte());
	}

	public static ByteSerializer of(final byte data) {
		return XData.make(new ByteSerializer(), serializer -> serializer.setData(data));
	}
}
