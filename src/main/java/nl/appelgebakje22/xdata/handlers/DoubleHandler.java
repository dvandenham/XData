package nl.appelgebakje22.xdata.handlers;

import nl.appelgebakje22.xdata.Reference;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import nl.appelgebakje22.xdata.serializers.DoubleSerializer;

public class DoubleHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(Class<?> clazz) {
		return clazz == double.class || clazz == Double.class;
	}

	@Override
	public Serializer<?> readFromReference(Reference ref, HolderLookup_Provider registries) {
		return DoubleSerializer.of((double) ref.getValueHolder().get());
	}

	@Override
	public void writeToReference(Reference ref, Serializer<?> serializer, HolderLookup_Provider registries) {
		DoubleSerializer s = this.testSerializer(serializer, DoubleSerializer.class);
		ref.getValueHolder().set(s.getData());
	}
}
