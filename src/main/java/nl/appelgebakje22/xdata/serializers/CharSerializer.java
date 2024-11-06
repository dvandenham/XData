package nl.appelgebakje22.xdata.serializers;

import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.CharAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkAdapter;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import org.jetbrains.annotations.Nullable;

public class CharSerializer extends Serializer<Character> {

	@Override
	public @Nullable BaseAdapter serialize(final Reference ref, final AdapterFactory adapters) {
		return adapters.ofChar(this.getData());
	}

	@Override
	public void deserialize(final Reference ref, final AdapterFactory adapters, final BaseAdapter adapter) {
		final CharAdapter charAdapter = this.testAdapter(adapter, CharAdapter.class);
		this.setData(charAdapter.getChar());
	}

	@Override
	public void toNetwork(final Reference ref, final NetworkAdapter network) {
		network.write(this.getData());
	}

	@Override
	public void fromNetwork(final Reference ref, final NetworkAdapter network) {
		this.setData(network.readChar());
	}

	public static CharSerializer of(final char data) {
		return XData.make(new CharSerializer(), serializer -> serializer.setData(data));
	}
}
