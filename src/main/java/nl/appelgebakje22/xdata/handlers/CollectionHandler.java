package nl.appelgebakje22.xdata.handlers;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.BiFunction;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import nl.appelgebakje22.xdata.CollectionReference;
import nl.appelgebakje22.xdata.Reference;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import nl.appelgebakje22.xdata.serializers.ArraySerializer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionHandler implements ReferenceHandler {

	public static final BiFunction<ReferenceHandler, Class<?>, CollectionHandler> FACTORY = XData.memoize(CollectionHandler::new);

	private final ReferenceHandler contentHandler;
	private final Class<?> contentType;

	@Override
	public boolean canHandle(Class<?> clazz) {
		return Collections.class.isAssignableFrom(clazz);
	}

	@Override
	public Serializer<?> readFromReference(Reference ref, HolderLookup_Provider registries) {
		final Object currentData = ref.getValueHolder().get();
		if (!(currentData instanceof final Collection<?> collection)) {
			throw new IllegalStateException("Field is not a collection");
		}
		final Iterator<?> iterator = collection.iterator();
		final Serializer<?>[] arr = new Serializer[collection.size()];
		for (int i = 0; i < arr.length; ++i) {
			final Reference wrapped = CollectionReference.of(ref.getKey(), collection, i);
			arr[i] = this.contentHandler.readFromReference(wrapped, registries);
		}
		return XData.make(new ArraySerializer(), serializer -> serializer.setData(arr));
	}

	@Override
	public void writeToReference(Reference ref, Serializer<?> serializer, HolderLookup_Provider registries) {

	}
}
