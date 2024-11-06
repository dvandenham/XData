package nl.appelgebakje22.xdata.ref;

import java.util.Collection;

public class CollectionReference extends Reference {

	private CollectionReference(ReferenceKey key, Collection<?> collection, int index) {
		super(key, new CollectionHolder(collection, index));
	}

	@Override
	public void tick() {
		//TODO check array contents
	}

	public static CollectionReference of(ReferenceKey key, Collection<?> collection, int index) {
		return new CollectionReference(key, collection, index);
	}
}
