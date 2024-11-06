package nl.appelgebakje22.xdata.api;

import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.ref.Reference;

public interface ReferenceHandler {

	boolean canHandle(Class<?> clazz);

	Serializer<?> readFromReference(Operation operation, AdapterFactory adapters, Reference ref);

	void writeToReference(Operation operation, AdapterFactory adapters, Reference ref, Serializer<?> serializer);

	default <T extends Serializer<?>> T testSerializer(Serializer<?> serializer, Class<T> expectedType) {
		if (expectedType.isAssignableFrom(serializer.getClass())) {
			return expectedType.cast(serializer);
		}
		throw new IllegalArgumentException("Expected %s of type %s, got: %s".formatted(Serializer.class.getName(), expectedType.getName(), serializer.getClass().getName()));
	}
}
