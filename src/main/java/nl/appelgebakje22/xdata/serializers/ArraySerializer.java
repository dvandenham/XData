package nl.appelgebakje22.xdata.serializers;

import java.lang.reflect.Array;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.XDataRegister;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.ArrayAdapter;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkAdapter;
import nl.appelgebakje22.xdata.adapter.TableAdapter;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.ArrayReference;
import nl.appelgebakje22.xdata.ref.Reference;
import org.jetbrains.annotations.Nullable;

public class ArraySerializer extends Serializer<Serializer<?>[]> {

	@Override
	public @Nullable BaseAdapter serialize(Reference ref, AdapterFactory adapters) {
		ArrayAdapter array = adapters.array();
		Reference[] arrayRefs = makeRefs(ref, getData().length);
		for (int i = 0; i < getData().length; ++i) {
			Serializer<?> serializer = this.getData()[i];
			TableAdapter entry = array.addTable();
			entry.set("sid", serializer.getSid());
			BaseAdapter dataAdapter = serializer.serialize(arrayRefs[i], adapters);
			if (dataAdapter != null) {
				entry.set("dat", dataAdapter);
			}
		}
		return array;
	}

	@Override
	public void deserialize(Reference ref, AdapterFactory adapters, BaseAdapter adapter) {
		ArrayAdapter array = this.testAdapter(adapter, ArrayAdapter.class);
		Serializer<?>[] arr = new Serializer[array.size()];
		Reference[] arrayRefs = makeRefs(ref, arr.length);
		for (int i = 0; i < arr.length; ++i) {
			TableAdapter entry = this.testAdapter(array.get(i), TableAdapter.class);
			arr[i] = XDataRegister.getSerializerById(entry.getInt("sid"));
			if (arr[i] == null) {
				throw new IllegalStateException("Could not create %s with id %s".formatted(Serializer.class.getName(), entry.getInt("sid")));
			}
			final BaseAdapter dataAdapter = entry.get("dat");
			if (dataAdapter != null) {
				arr[i].deserialize(arrayRefs[i], adapters, dataAdapter);
			}
		}
		this.setData(arr);
	}

	@Override
	public void toNetwork(Reference ref, NetworkAdapter network) {
		Reference[] arrayRefs = makeRefs(ref, this.getData().length);
		network.write(this.getData().length);
		for (int i = 0; i < this.getData().length; ++i) {
			Serializer<?> serializer = this.getData()[i];
			network.write(serializer.getSid());
			serializer.toNetwork(arrayRefs[i], network);
		}
	}

	@Override
	public void fromNetwork(Reference ref, NetworkAdapter network) {
		Serializer<?>[] arr = new Serializer[network.readInt()];
		Reference[] arrayRefs = makeRefs(ref, arr.length);
		for (int i = 0; i < arr.length; ++i) {
			int sid = network.readInt();
			arr[i] = XDataRegister.getSerializerById(sid);
			if (arr[i] == null) {
				throw new IllegalStateException("Could not create %s with id %s".formatted(Serializer.class.getName(), sid));
			}
			arr[i].fromNetwork(arrayRefs[i], network);
		}
		this.setData(arr);
	}

	private static Reference[] makeRefs(Reference arrayRef, int length) {
		Object array = arrayRef.getValueHolder().get();
		Class<?> arrayType = arrayRef.getKey().getRawField().getType().getComponentType();
		Reference[] result = new Reference[length];
		for (int i = 0; i < result.length; ++i) {
			result[i] = ArrayReference.of(arrayRef.getKey(), array, i, arrayType);
		}
		return result;
	}

	public static ArraySerializer of(Serializer<?>[] data) {
		return XData.make(new ArraySerializer(), serializer -> serializer.setData(data));
	}
}
