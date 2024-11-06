package nl.appelgebakje22.xdata.ref;

public class ArrayReference extends Reference {

	private ArrayReference(ReferenceKey key, Object array, int index, Class<?> arrayType) {
		super(key, ArrayHolder.of(array, index, arrayType));
	}

	@Override
	public void tick() {
		//TODO check array contents
	}

	public static ArrayReference of(ReferenceKey key, Object array, int index, Class<?> arrayType) {
		return new ArrayReference(key, array, index, arrayType);
	}
}
