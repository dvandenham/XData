package nl.appelgebakje22.xdata.serializers;

import java.util.UUID;
import net.querz.nbt.tag.StringTag;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.StringAdapter;
import nl.appelgebakje22.xdata.api.SimpleSerializer;
import org.jetbrains.annotations.Nullable;

public class UUIDSerializer extends SimpleSerializer<UUID> {

	@Override
	public @Nullable BaseAdapter serialize(AdapterFactory adapters) {
		return adapters.ofString(getData().toString());
	}

	@Override
	public void deserialize(AdapterFactory adapters, BaseAdapter adapter) {
		StringAdapter stringAdapter = this.testAdapter(adapter, StringAdapter.class);
		setData(UUID.fromString(stringAdapter.get()));
	}

	public static UUIDSerializer of(UUID data) {
		return XData.make(new UUIDSerializer(), serializer -> serializer.setData(data));
	}
}
