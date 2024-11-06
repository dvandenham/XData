package nbt;

import net.querz.nbt.tag.ByteTag;
import net.querz.nbt.tag.DoubleTag;
import net.querz.nbt.tag.FloatTag;
import net.querz.nbt.tag.IntTag;
import net.querz.nbt.tag.LongTag;
import net.querz.nbt.tag.ShortTag;
import net.querz.nbt.tag.StringTag;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.ArrayAdapter;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.BooleanAdapter;
import nl.appelgebakje22.xdata.adapter.ByteAdapter;
import nl.appelgebakje22.xdata.adapter.CharAdapter;
import nl.appelgebakje22.xdata.adapter.DoubleAdapter;
import nl.appelgebakje22.xdata.adapter.FloatAdapter;
import nl.appelgebakje22.xdata.adapter.IntAdapter;
import nl.appelgebakje22.xdata.adapter.LongAdapter;
import nl.appelgebakje22.xdata.adapter.ShortAdapter;
import nl.appelgebakje22.xdata.adapter.StringAdapter;
import nl.appelgebakje22.xdata.adapter.TableAdapter;
import org.jetbrains.annotations.Nullable;

public class NbtAdapterFactory implements AdapterFactory {

	@Override
	public BooleanAdapter ofBoolean(boolean value) {
		return XData.make(new NbtBooleanAdapter(), adapter -> adapter.set(value));
	}

	@Override
	public ByteAdapter ofByte(byte value) {
		return XData.make(new NbtByteAdapter(), adapter -> adapter.set(value));
	}

	@Override
	public ShortAdapter ofShort(short value) {
		return XData.make(new NbtShortAdapter(), adapter -> adapter.set(value));
	}

	@Override
	public IntAdapter ofInt(int value) {
		return XData.make(new NbtIntAdapter(), adapter -> adapter.set(value));
	}

	@Override
	public LongAdapter ofLong(long value) {
		return XData.make(new NbtLongAdapter(), adapter -> adapter.set(value));
	}

	@Override
	public FloatAdapter ofFloat(float value) {
		return XData.make(new NbtFloatAdapter(), adapter -> adapter.set(value));
	}

	@Override
	public DoubleAdapter ofDouble(double value) {
		return XData.make(new NbtDoubleAdapter(), adapter -> adapter.set(value));
	}

	@Override
	public CharAdapter ofChar(char value) {
		return XData.make(new NbtCharAdapter(), adapter -> adapter.set(value));
	}

	@Override
	public StringAdapter ofString(@Nullable String value) {
		return XData.make(new NbtStringAdapter(), adapter -> adapter.set(value));
	}

	@Override
	public ArrayAdapter array() {
		return new NbtArrayAdapter(this);
	}

	@Override
	public TableAdapter table() {
		return new NbtTableAdapter(this);
	}

	public static Tag toTag(BaseAdapter adapter) {
		return switch (adapter) {
			case BooleanAdapter booleanAdapter -> new ByteTag(booleanAdapter.get());
			case ByteAdapter byteAdapter -> new ByteTag(byteAdapter.get());
			case ShortAdapter shortAdapter -> new ShortTag(shortAdapter.asShort());
			case IntAdapter intAdapter -> new IntTag(intAdapter.asInt());
			case LongAdapter longAdapter -> new LongTag(longAdapter.asLong());
			case FloatAdapter floatAdapter -> new FloatTag(floatAdapter.asFloat());
			case DoubleAdapter doubleAdapter -> new DoubleTag(doubleAdapter.asDouble());
			case CharAdapter charAdapter -> NbtCharAdapter.toTag(charAdapter);
			case StringAdapter stringAdapter -> new StringTag(stringAdapter.get());
			case ArrayAdapter arrayAdapter -> NbtArrayAdapter.toTag(arrayAdapter);
			case TableAdapter tableAdapter -> NbtTableAdapter.toTag(tableAdapter);
			default -> throw new IllegalStateException("Unexpected value: " + adapter);
		};
	}
}
