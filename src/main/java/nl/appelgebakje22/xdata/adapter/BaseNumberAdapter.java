package nl.appelgebakje22.xdata.adapter;

public interface BaseNumberAdapter<TYPE extends Number> extends BaseObjectAdapter<TYPE> {

	default byte asByte() {
		Number num = this.get();
		return num != null ? num.byteValue() : 0;
	}

	default short asShort() {
		Number num = this.get();
		return num != null ? num.shortValue() : 0;
	}

	default int asInt() {
		Number num = this.get();
		return num != null ? num.intValue() : 0;
	}

	default long asLong() {
		Number num = this.get();
		return num != null ? num.longValue() : 0;
	}

	default float asFloat() {
		Number num = this.get();
		return num != null ? num.floatValue() : 0;
	}

	default double asDouble() {
		Number num = this.get();
		return num != null ? num.doubleValue() : 0;
	}
}
