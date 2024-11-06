package nl.appelgebakje22.xdata.ref;

import java.lang.reflect.Array;
import java.util.Collection;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.XDataRegister;
import nl.appelgebakje22.xdata.api.Copier;
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
			this.lastValue = cloneGenericArray(currentValue, this.isArray);
			this.lastLength = Array.getLength(this.lastLength);
		}
	}

	@Override
	public void tick() {
		Object currentValue = getValueHolder().get();
		if ((currentValue == null && this.lastValue != null) || (currentValue != null && this.lastValue == null) || (this.lastValue != null && hasChanged(currentValue))) {
			if (currentValue != null) {
				this.lastValue = cloneGenericArray(currentValue, this.isArray);
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
				if (!XData.isEqual(Array.get(this.lastValue, i), Array.get(newValue, i))) {
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
			if (!XData.isEqual(Array.get(this.lastValue, i++), item)) {
				markDirty();
				dirtyElements.add(i);
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	@SuppressWarnings({ "SuspiciousSystemArraycopy", "rawtypes", "unchecked" })
	private static Object cloneGenericArray(Object array, boolean isArray) {
		if (isArray) {
			Class<?> componentType = array.getClass().getComponentType();
			if (componentType.isPrimitive()) {
				Object newArray = Array.newInstance(componentType, Array.getLength(array));
				System.arraycopy(array, 0, newArray, 0, Array.getLength(newArray));
				return newArray;
			}
			Object[] newArray = new Object[Array.getLength(array)];
			for (int i = 0; i < newArray.length; ++i) {
				Object item = Array.get(array, i);
				if (item != null) {
					Copier copier = XDataRegister.getCopier(item.getClass());
					newArray[i] = copier != null ? copier.copy(item) : item;
				}
			}
			return newArray;
		}
		Collection c = (Collection) array;
		Object[] result = new Object[c.size()];
		int i = 0;
		for (Object item : c) {
			if (item != null) {
				Copier copier = XDataRegister.getCopier(item.getClass());
				result[i++] = copier != null ? copier.copy(item) : item;
			}
		}
		return result;
	}
}
