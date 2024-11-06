package nl.appelgebakje22.xdata.handlers;

import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.ref.Reference;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import nl.appelgebakje22.xdata.serializers.FloatSerializer;

public class FloatHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(Class<?> clazz) {
		return clazz == float.class || clazz == Float.class;
	}

	@Override
	public Serializer<?> readFromReference(Operation operation, Reference ref, HolderLookup_Provider registries) {
		return FloatSerializer.of((float) ref.getValueHolder().get());
	}

	@Override
	public void writeToReference(Operation operation, Reference ref, Serializer<?> serializer, HolderLookup_Provider registries) {
		FloatSerializer s = this.testSerializer(serializer, FloatSerializer.class);
		ref.getValueHolder().set(s.getData());
	}
}
