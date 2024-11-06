package nl.appelgebakje22.xdata;

import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.NetworkAdapter;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import org.jetbrains.annotations.Nullable;

public class XDataSerializationUtils {

	@Nullable
	public static BaseAdapter writeRefToAdapter(Operation operation, AdapterFactory adapters, Reference ref) {
		ReferenceHandler handler = XDataRegister.getHandler(ref.getKey().getRawType());
		if (handler == null) {
			//TODO logging
			System.out.printf("Cannot serialize field %s because no matching %s was registered%n", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return null;
		}
		final Serializer<?> serializer = handler.readFromReference(operation, ref);
		return serializer.serialize(ref, adapters);
	}

	public static void readRefFromAdapter(Operation operation, AdapterFactory adapters, Reference ref, BaseAdapter adapter) {
		ReferenceHandler handler = XDataRegister.getHandler(ref.getKey().getRawType());
		if (handler == null) {
			//TODO logging
			System.out.printf("Cannot deserialize field %s because no matching %s was registered%n", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return;
		}
		final Serializer<?> serializer = XDataRegister.getSerializerByHandler(handler);
		serializer.deserialize(ref, adapters, adapter);
		handler.writeToReference(operation, ref, serializer);
	}

	public static void writeRefToNetwork(Operation operation, NetworkAdapter network, Reference ref) {
		ReferenceHandler handler = XDataRegister.getHandler(ref.getKey().getRawType());
		if (handler == null) {
			//TODO logging
			System.out.printf("Cannot serialize field %s because no matching %s was registered%n", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return;
		}
		final Serializer<?> serializer = handler.readFromReference(operation, ref);
		serializer.toNetwork(ref, network);
	}
}
