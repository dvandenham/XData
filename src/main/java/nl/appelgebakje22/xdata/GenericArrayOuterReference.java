package nl.appelgebakje22.xdata;

import java.lang.reflect.Array;
import java.util.Collection;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import nl.appelgebakje22.xdata.api.Holder;

public class GenericArrayOuterReference extends Reference {

	private final IntSet dirtyElements = new IntOpenHashSet();
	private final boolean isArray;
	private Object lastValue;
	private int lastLength;

	GenericArrayOuterReference(ReferenceKey key, Holder valueHolder) {
		super(key, valueHolder);
		this.isArray = key.getRawField().getType().isArray();
		Object currentValue = valueHolder.get();
		if (currentValue != null) {
			this.lastValue = Utils.cloneGenericArray(currentValue, this.isArray);
			this.lastLength = Array.getLength(this.lastLength);
		}
	}

	@Override
	public void tick() {
		Object currentValue = getValueHolder().get();
		if ((currentValue == null && this.lastValue != null) || (currentValue != null && this.lastValue == null) || (this.lastValue != null && hasChanged(currentValue))) {
			if (currentValue != null) {
				this.lastValue = Utils.cloneGenericArray(currentValue, this.isArray);
				this.lastLength = Array.getLength(this.lastValue);
			} else {
				this.lastValue = null;
				this.lastLength = 0;
			}
			this.markDirty();
		}
	}

	@SuppressWarnings("rawtypes")
	protected boolean hasChanged(Object newValue) {
		if (isArray) {
			if (Array.getLength(newValue) != this.lastLength) {
				this.markDirty();
				this.dirtyElements.clear();
				return true;
			}
			boolean hasChanged = false;
			for (int i = 0; i < this.lastLength; ++i) {
				if (!Utils.isEqual(Array.get(this.lastValue, i), Array.get(newValue, i))) {
					markDirty();
					dirtyElements.add(i);
					hasChanged = true;
				}
			}
			return hasChanged;
		}
		Collection c = (Collection) newValue;
		if (c.size() != this.lastLength) {
			this.markDirty();
			this.dirtyElements.clear();
			return true;
		}
		int i = 0;
		boolean hasChanged = false;
		for (Object item : c) {
			if (!Utils.isEqual(Array.get(this.lastValue, i++), item)) {
				markDirty();
				dirtyElements.add(i);
				hasChanged = true;
			}
		}
		return hasChanged;
	}
}
