package nl.appelgebakje22.xdata.adapter;

public interface NumberAdapter<TYPE extends Number> extends BaseAdapter {

	TYPE getNumber();

	default byte getByte() {
		Number num = this.getNumber();
		return num != null ? num.byteValue() : 0;
	}

	default short getShort() {
		Number num = this.getNumber();
		return num != null ? num.shortValue() : 0;
	}

	default int getInt() {
		Number num = this.getNumber();
		return num != null ? num.intValue() : 0;
	}

	default long getLong() {
		Number num = this.getNumber();
		return num != null ? num.longValue() : 0;
	}

	default float getFloat() {
		Number num = this.getNumber();
		return num != null ? num.floatValue() : 0;
	}

	default double getDouble() {
		Number num = this.getNumber();
		return num != null ? num.doubleValue() : 0;
	}
}
