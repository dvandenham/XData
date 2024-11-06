package nl.appelgebakje22.xdata.handlers;

import nl.appelgebakje22.xdata.Reference;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import nl.appelgebakje22.xdata.serializers.ShortSerializer;

public class ShortHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(Class<?> clazz) {
		return clazz == short.class || clazz == Short.class;
	}

	@Override
	public Serializer<?> readFromReference(Reference ref, HolderLookup_Provider registries) {
		return ShortSerializer.of((short) ref.getValueHolder().get());
	}

	@Override
	public void writeToReference(Reference ref, Serializer<?> serializer, HolderLookup_Provider registries) {
		ShortSerializer s = this.testSerializer(serializer, ShortSerializer.class);
		ref.getValueHolder().set(s.getData());
	}
}
