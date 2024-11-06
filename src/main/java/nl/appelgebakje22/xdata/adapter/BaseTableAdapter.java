package nl.appelgebakje22.xdata.adapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import nl.appelgebakje22.xdata.XData;
import org.jetbrains.annotations.UnknownNullability;

public class BaseTableAdapter implements TableAdapter {

	private final Map<String, BaseAdapter> elements = new HashMap<>();
	private final AdapterFactory adapters;

	public BaseTableAdapter(AdapterFactory adapters) {
		this.adapters = adapters;
	}

	@Override
	public void set(String key, boolean value) {
		set(key, this.adapters.ofBoolean(value));
	}

	@Override
	public void set(String key, byte value) {
		set(key, this.adapters.ofByte(value));
	}

	@Override
	public void set(String key, short value) {
		set(key, this.adapters.ofShort(value));
	}

	@Override
	public void set(String key, int value) {
		set(key, this.adapters.ofInt(value));
	}

	@Override
	public void set(String key, long value) {
		set(key, this.adapters.ofLong(value));
	}

	@Override
	public void set(String key, float value) {
		set(key, this.adapters.ofFloat(value));
	}

	@Override
	public void set(String key, double value) {
		set(key, this.adapters.ofDouble(value));
	}

	@Override
	public void set(String key, char value) {
		set(key, this.adapters.ofChar(value));
	}

	@Override
	public void set(String key, String value) {
		set(key, this.adapters.ofString(value));
	}

	@Override
	public void set(String key, BaseAdapter value) {
		if (value != null) {
			this.elements.put(checkKey(key), value);
		}
	}

	@Override
	public ArrayAdapter setArray(String key) {
		return XData.make(this.adapters.array(), array -> this.set(key, array));
	}

	@Override
	public TableAdapter setTable(String key) {
		return XData.make(this.adapters.table(), table -> this.set(key, table));
	}

	@Override
	public boolean has(String key) {
		return this.elements.containsKey(checkKey(key));
	}

	@Override
	public boolean getBoolean(String key, boolean fallback) {
		BaseAdapter adapter = get(key);
		return adapter instanceof BooleanAdapter booleanAdapter ? booleanAdapter.getBoolean() : fallback;
	}

	@Override
	public byte getByte(String key, byte fallback) {
		BaseAdapter adapter = get(key);
		return adapter instanceof NumberAdapter<?> numberAdapter ? numberAdapter.getByte() : fallback;
	}

	@Override
	public short getShort(String key, short fallback) {
		BaseAdapter adapter = get(key);
		return adapter instanceof NumberAdapter<?> numberAdapter ? numberAdapter.getShort() : fallback;
	}

	@Override
	public int getInt(String key, int fallback) {
		BaseAdapter adapter = get(key);
		return adapter instanceof NumberAdapter<?> numberAdapter ? numberAdapter.getInt() : fallback;
	}

	@Override
	public long getLong(String key, long fallback) {
		BaseAdapter adapter = get(key);
		return adapter instanceof NumberAdapter<?> numberAdapter ? numberAdapter.getLong() : fallback;
	}

	@Override
	public float getFloat(String key, float fallback) {
		BaseAdapter adapter = get(key);
		return adapter instanceof NumberAdapter<?> numberAdapter ? numberAdapter.getFloat() : fallback;
	}

	@Override
	public double getDouble(String key, double fallback) {
		BaseAdapter adapter = get(key);
		return adapter instanceof NumberAdapter<?> numberAdapter ? numberAdapter.getDouble() : fallback;
	}

	@Override
	public char getChar(String key, char fallback) {
		BaseAdapter adapter = get(key);
		return adapter instanceof CharAdapter charAdapter ? charAdapter.getChar() : fallback;
	}

	@Override
	public @UnknownNullability String getString(String key, String fallback) {
		BaseAdapter adapter = get(key);
		return adapter instanceof StringAdapter stringAdapter ? stringAdapter.getString() : fallback;
	}

	@Override
	public @UnknownNullability ArrayAdapter getArray(String key) {
		BaseAdapter adapter = get(key);
		return adapter instanceof ArrayAdapter arrayAdapter ? arrayAdapter : null;
	}

	@Override
	public @UnknownNullability TableAdapter getTable(String key) {
		BaseAdapter adapter = get(key);
		return adapter instanceof TableAdapter tableAdapter ? tableAdapter : null;
	}

	@Override
	public @UnknownNullability BaseAdapter get(String key) {
		return this.elements.get(checkKey(key));
	}

	@Override
	public void remove(String key) {
		this.elements.remove(checkKey(key));
	}

	@Override
	public int size() {
		return this.elements.size();
	}

	@Override
	public String[] getKeys() {
		return this.elements.keySet().toArray(new String[0]);
	}

	private String checkKey(String key) {
		return Objects.requireNonNull(key);
	}
}
