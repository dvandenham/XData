package nl.appelgebakje22.xdata.handlers;

import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.ref.Reference;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import nl.appelgebakje22.xdata.serializers.BooleanSerializer;

public class BooleanHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(Class<?> clazz) {
		return clazz == boolean.class || clazz == Boolean.class;
	}

	@Override
	public Serializer<?> readFromReference(Operation operation, Reference ref, HolderLookup_Provider registries) {
		return BooleanSerializer.of((boolean) ref.getValueHolder().get());
	}

	@Override
	public void writeToReference(Operation operation, Reference ref, Serializer<?> serializer, HolderLookup_Provider registries) {
		BooleanSerializer s = this.testSerializer(serializer, BooleanSerializer.class);
		ref.getValueHolder().set(s.getData());
	}
}
