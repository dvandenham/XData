package nl.appelgebakje22.xdata.handlers;

import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;
import nl.appelgebakje22.xdata.serializers.StringSerializer;
import org.jetbrains.annotations.Nullable;

public class EnumHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(Class<?> clazz) {
		return clazz.isEnum();
	}

	@Override
	public Serializer<?> readFromReference(Operation operation, AdapterFactory adapters, Reference ref) {
		Object data = ref.getValueHolder().get();
		return StringSerializer.of(((Enum<?>) data).name());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void writeToReference(Operation operation, AdapterFactory adapters, Reference ref, Serializer<?> rawSerializer) {
		StringSerializer serializer = this.testSerializer(rawSerializer, StringSerializer.class);
		Enum<?> newValue = getEnum((Class<Enum>) ref.getKey().getRawField().getType(), serializer.getData());
		ref.getValueHolder().set(newValue);
	}

	@Nullable
	private static <T extends Enum<T>> T getEnum(final Class<T> type, final String name) {
		for (final T constant : type.getEnumConstants()) {
			if (constant.name().equals(name)) {
				return constant;
			}
		}
		return null;
	}
}
