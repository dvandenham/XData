package nl.appelgebakje22.xdata.handlers;

import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.TableAdapter;
import nl.appelgebakje22.xdata.api.IManaged;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import nl.appelgebakje22.xdata.serializers.AdapterSerializer;

public class IManagedHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(Class<?> clazz) {
		return IManaged.class.isAssignableFrom(clazz);
	}

	@Override
	public Serializer<?> readFromReference(Operation operation, AdapterFactory adapters, Reference ref) {
		IManaged iManaged = (IManaged) ref.getValueHolder().get();
		return AdapterSerializer.of(iManaged.getDataMap().serialize(operation, adapters));
	}

	@Override
	public void writeToReference(Operation operation, AdapterFactory adapters, Reference ref, Serializer<?> serializer) {
		AdapterSerializer adapterSerializer = this.testSerializer(serializer, AdapterSerializer.class);
		if (!(adapterSerializer.getData() instanceof TableAdapter table)) {
			throw new IllegalArgumentException("Expected %s, got: %s".formatted(TableAdapter.class.getName(), adapterSerializer.getData().getClass().getName()));
		}
		((IManaged) ref.getValueHolder().get()).getDataMap().deserialize(operation, adapters, table);
	}
}
