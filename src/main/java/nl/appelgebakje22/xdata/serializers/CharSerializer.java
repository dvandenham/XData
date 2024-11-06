package nl.appelgebakje22.xdata.serializers;

import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.CharAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkAdapter;
import nl.appelgebakje22.xdata.api.Serializer;
import org.jetbrains.annotations.Nullable;

public class CharSerializer extends Serializer<Character> {

	@Override
	public @Nullable BaseAdapter serialize(AdapterFactory adapters) {
		return adapters.ofChar(getData());
	}

	@Override
	public void deserialize(AdapterFactory adapters, BaseAdapter adapter) {
		CharAdapter charAdapter = this.testAdapter(adapter, CharAdapter.class);
		setData(charAdapter.getChar());
	}

	@Override
	public void toNetwork(NetworkAdapter network) {
		network.write(getData());
	}

	@Override
	public void fromNetwork(NetworkAdapter network) {
		setData(network.readChar());
	}

	public static CharSerializer of(char data) {
		return XData.make(new CharSerializer(), serializer -> serializer.setData(data));
	}
}
