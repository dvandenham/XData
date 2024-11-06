package nl.appelgebakje22.xdata.handlers;

import java.util.function.Supplier;
import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.ref.Reference;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.api.SimpleSerializer;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;

public class SimpleObjectHandler implements ReferenceHandler {

	private final Class<?> typeClass;
	private final boolean shallowEqualityCheck;
	private final Supplier<? extends SimpleSerializer<?>> serializerFactory;

	public SimpleObjectHandler(Class<?> typeClass, boolean shallowEqualityCheck, Supplier<? extends SimpleSerializer<?>> serializerFactory) {
		this.typeClass = typeClass;
		this.shallowEqualityCheck = shallowEqualityCheck;
		this.serializerFactory = serializerFactory;
	}

	@Override
	public boolean canHandle(Class<?> clazz) {
		return this.shallowEqualityCheck ? clazz.equals(this.typeClass) : this.typeClass.isAssignableFrom(clazz);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Serializer<?> readFromReference(Operation operation, Reference ref, HolderLookup_Provider registries) {
		return XData.make(this.serializerFactory.get(), serializer -> ((SimpleSerializer) serializer).setData(ref.getValueHolder().get()));
	}

	@Override
	public void writeToReference(Operation operation, Reference ref, Serializer<?> rawSerializer, HolderLookup_Provider registries) {
		SimpleSerializer<?> serializer = this.testSerializer(rawSerializer, SimpleSerializer.class);
		ref.getValueHolder().set(serializer.getData());
	}
}
