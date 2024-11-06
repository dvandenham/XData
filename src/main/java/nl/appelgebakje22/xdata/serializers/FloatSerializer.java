package nl.appelgebakje22.xdata.serializers;

import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.NumberAdapter;
import nl.appelgebakje22.xdata.api.SimpleSerializer;
import org.jetbrains.annotations.Nullable;

public class FloatSerializer extends SimpleSerializer<Float> {

	@Override
	public @Nullable BaseAdapter serialize(AdapterFactory adapters) {
		return adapters.ofFloat(getData());
	}

	@Override
	public void deserialize(AdapterFactory adapters, BaseAdapter adapter) {
		NumberAdapter<?> numberAdapter = this.testAdapter(adapter, NumberAdapter.class);
		setData(numberAdapter.getFloat());
	}

	public static FloatSerializer of(float data) {
		return XData.make(new FloatSerializer(), serializer -> serializer.setData(data));
	}
}
