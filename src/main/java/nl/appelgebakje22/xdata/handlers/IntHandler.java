package nl.appelgebakje22.xdata.handlers;

import nl.appelgebakje22.xdata.Reference;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import nl.appelgebakje22.xdata.serializers.IntSerializer;

public class IntHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(Class<?> clazz) {
		return clazz == int.class || clazz == Integer.class;
	}

	@Override
	public Serializer<?> readFromReference(Reference ref, HolderLookup_Provider registries) {
		return IntSerializer.of((int) ref.getValueHolder().get());
	}

	@Override
	public void writeToReference(Reference ref, Serializer<?> serializer, HolderLookup_Provider registries) {
		IntSerializer s = this.testSerializer(serializer, IntSerializer.class);
		ref.getValueHolder().set(s.getData());
	}
}
