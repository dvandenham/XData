package nl.appelgebakje22.xdata.handlers;

import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import nl.appelgebakje22.xdata.serializers.DoubleSerializer;

public class DoubleHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(Class<?> clazz) {
		return clazz == double.class || clazz == Double.class;
	}

	@Override
	public Serializer<?> readFromReference(Operation operation, Reference ref) {
		return DoubleSerializer.of((double) ref.getValueHolder().get());
	}

	@Override
	public void writeToReference(Operation operation, Reference ref, Serializer<?> serializer) {
		DoubleSerializer s = this.testSerializer(serializer, DoubleSerializer.class);
		ref.getValueHolder().set(s.getData());
	}
}
