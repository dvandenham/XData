package nl.appelgebakje22.xdata.serializers;

import lombok.Getter;
import lombok.Setter;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.XDataRegister;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.ArrayAdapter;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.TableAdapter;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.dummyclasses.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

public class ArraySerializer implements Serializer<Serializer<?>[]> {

	@Getter
	@Setter
	private Serializer<?>[] data;

	@Override
	public @Nullable BaseAdapter serialize(AdapterFactory adapters) {
		ArrayAdapter array = adapters.array();
		for (int i = 0; i < getData().length; ++i) {
			Serializer<?> serializer = this.getData()[i];
			TableAdapter entry = array.addTable();
			entry.set("sid", serializer.getSid());
			BaseAdapter dataAdapter = serializer.serialize(adapters);
			if (dataAdapter != null) {
				entry.set("dat", dataAdapter);
			}
		}
		return array;
	}

	@Override
	public void deserialize(AdapterFactory adapters, BaseAdapter adapter) {
		ArrayAdapter array = this.testAdapter(adapter, ArrayAdapter.class);
		Serializer<?>[] arr = new Serializer[array.size()];
		for (int i = 0; i < arr.length; ++i) {
			TableAdapter entry = this.testAdapter(array.get(i), TableAdapter.class);
			arr[i] = XDataRegister.getSerializerById(entry.getInt("sid"));
			if (arr[i] == null) {
				throw new IllegalStateException("Could not create %s with id %s".formatted(Serializer.class.getName(), entry.getInt("sid")));
			}
			final BaseAdapter dataAdapter = entry.get("dat");
			if (dataAdapter != null) {
				arr[i].deserialize(adapters, dataAdapter);
			}
		}
		this.setData(arr);
	}

	@Override
	public void toNetwork(RegistryFriendlyByteBuf buf) {
		//TODO implement
	}

	@Override
	public void fromNetwork(RegistryFriendlyByteBuf buf) {
		//TODO implement
	}

	public static ArraySerializer of(Serializer<?>[] data) {
		return XData.make(new ArraySerializer(), serializer -> serializer.setData(data));
	}
}
