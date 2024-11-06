package nl.appelgebakje22.xdata.handlers;

import net.querz.nbt.tag.CompoundTag;
import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.api.IManaged;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import nl.appelgebakje22.xdata.ref.Reference;
import nl.appelgebakje22.xdata.serializers.NbtSerializer;

public class IManagedHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(Class<?> clazz) {
		return IManaged.class.isAssignableFrom(clazz);
	}

	@Override
	public Serializer<?> readFromReference(Operation operation, Reference ref, HolderLookup_Provider registries) {
		IManaged iManaged = (IManaged) ref.getValueHolder().get();
		return NbtSerializer.of(XData.make(new CompoundTag(), tag -> iManaged.getDataMap().saveToNbt(operation, tag, registries)));
	}

	@Override
	public void writeToReference(Operation operation, Reference ref, Serializer<?> serializer, HolderLookup_Provider registries) {
		NbtSerializer nbt = this.testSerializer(serializer, NbtSerializer.class);
		if (!(nbt.getData() instanceof CompoundTag compoundTag)) {
			throw new IllegalArgumentException("Expected %s, got: %s".formatted(CompoundTag.class.getName(), nbt.getData().getClass().getName()));
		}
		((IManaged) ref.getValueHolder().get()).getDataMap().readFromNbt(operation, compoundTag, registries);
	}
}
