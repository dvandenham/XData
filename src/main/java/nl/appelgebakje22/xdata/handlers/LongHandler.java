package nl.appelgebakje22.xdata.handlers;

import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import nl.appelgebakje22.xdata.serializers.LongSerializer;

public class LongHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(Class<?> clazz) {
		return clazz == long.class || clazz == Long.class;
	}

	@Override
	public Serializer<?> readFromReference(Operation operation, Reference ref) {
		return LongSerializer.of((long) ref.getValueHolder().get());
	}

	@Override
	public void writeToReference(Operation operation, Reference ref, Serializer<?> serializer) {
		LongSerializer s = this.testSerializer(serializer, LongSerializer.class);
		ref.getValueHolder().set(s.getData());
	}
}
