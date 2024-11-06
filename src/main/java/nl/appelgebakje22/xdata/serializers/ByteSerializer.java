package nl.appelgebakje22.xdata.serializers;

import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.NumberAdapter;
import nl.appelgebakje22.xdata.api.SimpleSerializer;
import org.jetbrains.annotations.Nullable;

public class ByteSerializer extends SimpleSerializer<Byte> {

	@Override
	public @Nullable BaseAdapter serialize(AdapterFactory adapters) {
		return adapters.ofByte(getData());
	}

	@Override
	public void deserialize(AdapterFactory adapters, BaseAdapter adapter) {
		NumberAdapter<?> numberAdapter = this.testAdapter(adapter, NumberAdapter.class);
		setData(numberAdapter.getByte());
	}

	public static ByteSerializer of(byte data) {
		return XData.make(new ByteSerializer(), serializer -> serializer.setData(data));
	}
}
