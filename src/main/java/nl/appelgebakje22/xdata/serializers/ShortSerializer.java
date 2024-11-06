package nl.appelgebakje22.xdata.serializers;

import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.BaseNumberAdapter;
import nl.appelgebakje22.xdata.api.SimpleSerializer;
import org.jetbrains.annotations.Nullable;

public class ShortSerializer extends SimpleSerializer<Short> {

	@Override
	public @Nullable BaseAdapter serialize(AdapterFactory adapters) {
		return adapters.ofShort(getData());
	}

	@Override
	public void deserialize(AdapterFactory adapters, BaseAdapter adapter) {
		BaseNumberAdapter<?> numberAdapter = this.testAdapter(adapter, BaseNumberAdapter.class);
		setData(numberAdapter.asShort());
	}

	public static ShortSerializer of(short data) {
		return XData.make(new ShortSerializer(), serializer -> serializer.setData(data));
	}
}
