package nl.appelgebakje22.xdata;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import nl.appelgebakje22.xdata.api.Holder;
import org.jetbrains.annotations.Nullable;

public class CollectionHolder implements Holder {

	private final Collection<?> collection;
	private final int index;

	CollectionHolder(Collection<?> collection, int index) {
		this.collection = collection;
		this.index = index;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void set(@Nullable Object object) {
		if (this.collection instanceof List list) {
			list.set(this.index, object);
		} else {
			Iterator<?> iterator = this.collection.iterator();
			for (int i = 0; i < this.index; ++i) {
				iterator.next();
			}
			throw new UnsupportedOperationException("Cannot set value for unsupported non-List type collection: " + this.collection.getClass().getName());
		}
	}

	@Override
	public @Nullable Object get() {
		if (this.collection instanceof List<?> list) {
			return list.get(this.index);
		} else {
			Iterator<?> iterator = this.collection.iterator();
			for (int i = 0; i < this.index; ++i) {
				iterator.next();
			}
			return iterator.next();
		}
	}
}
