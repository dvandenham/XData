package nl.appelgebakje22.xdata.serializers;

import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.api.SimpleSerializer;
import nl.appelgebakje22.xdata.dummyclasses.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

public class AdapterSerializer extends SimpleSerializer<BaseAdapter> {

	@Override
	public @Nullable BaseAdapter serialize(AdapterFactory adapters) {
		return getData();
	}

	@Override
	public void deserialize(AdapterFactory adapters, BaseAdapter adapter) {
		setData(adapter);
	}

	@Override
	public void toNetwork(RegistryFriendlyByteBuf buf) {
		//TODO implement
	}

	@Override
	public void fromNetwork(RegistryFriendlyByteBuf buf) {
		//TODO implement
	}

	public static AdapterSerializer of(BaseAdapter data) {
		return XData.make(new AdapterSerializer(), serializer -> serializer.setData(data));
	}
}
