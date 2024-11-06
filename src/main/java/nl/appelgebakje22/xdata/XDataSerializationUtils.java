package nl.appelgebakje22.xdata;

import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
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
		final Serializer<?> serializer = handler.readFromReference(operation, adapters, ref);
		return serializer.serialize(adapters);
	}

	public static void readRefFromAdapter(Operation operation, AdapterFactory adapters, Reference ref, BaseAdapter adapter) {
		ReferenceHandler handler = XDataRegister.getHandler(ref.getKey().getRawType());
		if (handler == null) {
			//TODO logging
			System.out.printf("Cannot deserialize field %s because no matching %s was registered%n", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return;
		}
		final Serializer<?> serializer = XDataRegister.getSerializerByHandler(handler);
		serializer.deserialize(adapters, adapter);
		handler.writeToReference(operation, adapters, ref, serializer);
	}
}
