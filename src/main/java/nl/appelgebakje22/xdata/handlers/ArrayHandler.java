package nl.appelgebakje22.xdata.handlers;

import java.lang.reflect.Array;
import java.util.function.BiFunction;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import nl.appelgebakje22.xdata.ArrayReference;
import nl.appelgebakje22.xdata.Reference;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import nl.appelgebakje22.xdata.serializers.ArraySerializer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArrayHandler implements ReferenceHandler {

	public static final BiFunction<ReferenceHandler, Class<?>, ArrayHandler> FACTORY = XData.memoize(ArrayHandler::new);

	private final ReferenceHandler contentHandler;
	private final Class<?> contentType;

	@Override
	public boolean canHandle(Class<?> clazz) {
		return clazz.isArray();
	}

	@Override
	public Serializer<?> readFromReference(Reference ref, HolderLookup_Provider registries) {
		final Object currentData = ref.getValueHolder().get();
		if (currentData == null || !currentData.getClass().isArray()) {
			throw new IllegalStateException("Field %s is not an array".formatted(ref.getKey().getRawField()));
		}
		final int length = Array.getLength(currentData);
		final Serializer<?>[] arr = new Serializer[length];
		for (int i = 0; i < arr.length; ++i) {
			final Reference wrapped = ArrayReference.of(ref.getKey(), currentData, i, this.contentType);
			arr[i] = this.contentHandler.readFromReference(wrapped, registries);
		}
		return ArraySerializer.of(arr);
	}

	@Override
	public void writeToReference(Reference ref, Serializer<?> serializer, HolderLookup_Provider registries) {

	}
}
