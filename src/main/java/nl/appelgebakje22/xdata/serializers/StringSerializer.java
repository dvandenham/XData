package nl.appelgebakje22.xdata.serializers;

import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.StringAdapter;
import nl.appelgebakje22.xdata.api.SimpleSerializer;
import org.jetbrains.annotations.Nullable;

public class StringSerializer extends SimpleSerializer<String> {

	@Override
	public @Nullable BaseAdapter serialize(AdapterFactory adapters) {
		return adapters.ofString(getData());
	}

	@Override
	public void deserialize(AdapterFactory adapters, BaseAdapter adapter) {
		StringAdapter stringAdapter = this.testAdapter(adapter, StringAdapter.class);
		setData(stringAdapter.getString());
	}

	public static StringSerializer of(String data) {
		return XData.make(new StringSerializer(), serializer -> serializer.setData(data));
	}
}
