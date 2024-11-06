package nl.appelgebakje22.xdata.adapter;

import java.util.ArrayList;
import java.util.List;
import nl.appelgebakje22.xdata.XData;
import org.jetbrains.annotations.UnknownNullability;

public class BaseArrayAdapter implements ArrayAdapter {

	private final List<BaseAdapter> elements = new ArrayList<>();
	private final AdapterFactory adapters;

	public BaseArrayAdapter(AdapterFactory adapters) {
		this.adapters = adapters;
	}

	@Override
	public void add(boolean value) {
		this.add(this.adapters.ofBoolean(value));
	}

	@Override
	public void add(byte value) {
		this.add(this.adapters.ofByte(value));
	}

	@Override
	public void add(short value) {
		this.add(this.adapters.ofShort(value));
	}

	@Override
	public void add(int value) {
		this.add(this.adapters.ofInt(value));
	}

	@Override
	public void add(long value) {
		this.add(this.adapters.ofLong(value));
	}

	@Override
	public void add(float value) {
		this.add(this.adapters.ofFloat(value));
	}

	@Override
	public void add(double value) {
		this.add(this.adapters.ofDouble(value));
	}

	@Override
	public void add(char value) {
		this.add(this.adapters.ofChar(value));
	}

	@Override
	public void add(String value) {
		this.add(this.adapters.ofString(value));
	}

	@Override
	public void add(BaseAdapter value) {
		if (value != null) {
			this.elements.add(value);
		}
	}

	@Override
	public ArrayAdapter addArray() {
		return XData.make(this.adapters.array(), this.elements::add);
	}

	@Override
	public TableAdapter addTable() {
		return XData.make(this.adapters.table(), this.elements::add);
	}

	@Override
	public void set(int index, boolean value) {
		set(index, this.adapters.ofBoolean(value));
	}

	@Override
	public void set(int index, byte value) {
		set(index, this.adapters.ofByte(value));
	}

	@Override
	public void set(int index, short value) {
		set(index, this.adapters.ofShort(value));
	}

	@Override
	public void set(int index, int value) {
		set(index, this.adapters.ofInt(value));
	}

	@Override
	public void set(int index, long value) {
		set(index, this.adapters.ofLong(value));
	}

	@Override
	public void set(int index, float value) {
		set(index, this.adapters.ofFloat(value));
	}

	@Override
	public void set(int index, double value) {
		set(index, this.adapters.ofDouble(value));
	}

	@Override
	public void set(int index, char value) {
		set(index, this.adapters.ofChar(value));
	}

	@Override
	public void set(int index, String value) {
		set(index, this.adapters.ofString(value));
	}

	@Override
	public void set(int index, BaseAdapter value) {
		if (value != null) {
			this.elements.set(checkIndex(index), value);
		}
	}

	@Override
	public ArrayAdapter setArray(int index) {
		return XData.make(this.adapters.array(), array -> this.set(index, array));
	}

	@Override
	public TableAdapter setTable(int index) {
		return XData.make(this.adapters.table(), table -> this.set(index, table));
	}

	@Override
	public boolean has(int index) {
		return index >= 0 && index < this.elements.size();
	}

	@Override
	public boolean getBoolean(int index, boolean fallback) {
		BaseAdapter adapter = get(index);
		return adapter instanceof BooleanAdapter booleanAdapter ? booleanAdapter.getBoolean() : fallback;
	}

	@Override
	public byte getByte(int index, byte fallback) {
		BaseAdapter adapter = get(index);
		return adapter instanceof NumberAdapter<?> numberAdapter ? numberAdapter.getByte() : fallback;
	}

	@Override
	public short getShort(int index, short fallback) {
		BaseAdapter adapter = get(index);
		return adapter instanceof NumberAdapter<?> numberAdapter ? numberAdapter.getShort() : fallback;
	}

	@Override
	public int getInt(int index, int fallback) {
		BaseAdapter adapter = get(index);
		return adapter instanceof NumberAdapter<?> numberAdapter ? numberAdapter.getInt() : fallback;
	}

	@Override
	public long getLong(int index, long fallback) {
		BaseAdapter adapter = get(index);
		return adapter instanceof NumberAdapter<?> numberAdapter ? numberAdapter.getLong() : fallback;
	}

	@Override
	public float getFloat(int index, float fallback) {
		BaseAdapter adapter = get(index);
		return adapter instanceof NumberAdapter<?> numberAdapter ? numberAdapter.getFloat() : fallback;
	}

	@Override
	public double getDouble(int index, double fallback) {
		BaseAdapter adapter = get(index);
		return adapter instanceof NumberAdapter<?> numberAdapter ? numberAdapter.getDouble() : fallback;
	}

	@Override
	public char getChar(int index, char fallback) {
		BaseAdapter adapter = get(index);
		return adapter instanceof CharAdapter charAdapter ? charAdapter.getChar() : fallback;
	}

	@Override
	public @UnknownNullability String getString(int index, String fallback) {
		BaseAdapter adapter = get(index);
		return adapter instanceof StringAdapter stringAdapter ? stringAdapter.getString() : fallback;
	}

	@Override
	public @UnknownNullability ArrayAdapter getArray(int index) {
		BaseAdapter adapter = get(index);
		return adapter instanceof ArrayAdapter arrayAdapter ? arrayAdapter : null;
	}

	@Override
	public @UnknownNullability TableAdapter getTable(int index) {
		BaseAdapter adapter = get(index);
		return adapter instanceof TableAdapter tableAdapter ? tableAdapter : null;
	}

	@Override
	public @UnknownNullability BaseAdapter get(int index) {
		return this.elements.get(checkIndex(index));
	}

	@Override
	public void remove(int index) {
		this.elements.remove(checkIndex(index));
	}

	@Override
	public int size() {
		return this.elements.size();
	}

	private int checkIndex(int index) {
		if (!has(index)) {
			throw new IndexOutOfBoundsException("Index of %s does satisfy 0 <= index < %s".formatted(index, size()));
		}
		return index;
	}
}
