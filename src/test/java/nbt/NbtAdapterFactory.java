package nbt;

import net.querz.nbt.tag.ByteTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.DoubleTag;
import net.querz.nbt.tag.FloatTag;
import net.querz.nbt.tag.IntTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.LongTag;
import net.querz.nbt.tag.ShortTag;
import net.querz.nbt.tag.StringTag;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.ArrayAdapter;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.BaseBooleanAdapter;
import nl.appelgebakje22.xdata.adapter.BaseCharAdapter;
import nl.appelgebakje22.xdata.adapter.BaseDoubleAdapter;
import nl.appelgebakje22.xdata.adapter.BaseFloatAdapter;
import nl.appelgebakje22.xdata.adapter.BaseIntAdapter;
import nl.appelgebakje22.xdata.adapter.BaseLongAdapter;
import nl.appelgebakje22.xdata.adapter.BaseShortAdapter;
import nl.appelgebakje22.xdata.adapter.BooleanAdapter;
import nl.appelgebakje22.xdata.adapter.ByteAdapter;
import nl.appelgebakje22.xdata.adapter.CharAdapter;
import nl.appelgebakje22.xdata.adapter.DoubleAdapter;
import nl.appelgebakje22.xdata.adapter.FloatAdapter;
import nl.appelgebakje22.xdata.adapter.IntAdapter;
import nl.appelgebakje22.xdata.adapter.LongAdapter;
import nl.appelgebakje22.xdata.adapter.NullTypeAdapter;
import nl.appelgebakje22.xdata.adapter.ShortAdapter;
import nl.appelgebakje22.xdata.adapter.StringAdapter;
import nl.appelgebakje22.xdata.adapter.TableAdapter;
import nl.appelgebakje22.xdata.adapter.UnknownTypeAdapter;
import org.jetbrains.annotations.Nullable;

public class NbtAdapterFactory implements AdapterFactory {

	@Override
	public BooleanAdapter ofBoolean(boolean value) {
		return XData.make(new BaseBooleanAdapter(), adapter -> adapter.setBoolean(value));
	}

	@Override
	public ByteAdapter ofByte(byte value) {
		return XData.make(new NbtByteAdapter(), adapter -> adapter.setNumber(value));
	}

	@Override
	public ShortAdapter ofShort(short value) {
		return XData.make(new BaseShortAdapter(), adapter -> adapter.setNumber(value));
	}

	@Override
	public IntAdapter ofInt(int value) {
		return XData.make(new BaseIntAdapter(), adapter -> adapter.setNumber(value));
	}

	@Override
	public LongAdapter ofLong(long value) {
		return XData.make(new BaseLongAdapter(), adapter -> adapter.setNumber(value));
	}

	@Override
	public FloatAdapter ofFloat(float value) {
		return XData.make(new BaseFloatAdapter(), adapter -> adapter.setNumber(value));
	}

	@Override
	public DoubleAdapter ofDouble(double value) {
		return XData.make(new BaseDoubleAdapter(), adapter -> adapter.setNumber(value));
	}

	@Override
	public CharAdapter ofChar(char value) {
		return XData.make(new BaseCharAdapter(), adapter -> adapter.setChar(value));
	}

	@Override
	public StringAdapter ofString(@Nullable String value) {
		return XData.make(new NbtStringAdapter(), adapter -> adapter.setString(value));
	}

	@Override
	public ArrayAdapter array() {
		return new NbtArrayAdapter(this);
	}

	@Override
	public TableAdapter table() {
		return new NbtTableAdapter(this);
	}

	public static BaseAdapter fromTag(AdapterFactory adapters, Tag tag) {
		return switch (tag) {
			case ByteTag byteTag -> adapters.ofByte(byteTag.asByte());
			case ShortTag shortTag -> adapters.ofShort(shortTag.asShort());
			case IntTag intTag -> adapters.ofInt(intTag.asInt());
			case LongTag longTag -> adapters.ofLong(longTag.asLong());
			case FloatTag floatTag -> adapters.ofFloat(floatTag.asFloat());
			case DoubleTag doubleTag -> adapters.ofDouble(doubleTag.asDouble());
			case StringTag stringTag -> adapters.ofString(stringTag.getValue());
			case CompoundTag compoundTag -> NbtTableAdapter.fromTag(adapters, compoundTag);
			case ListTag listTag -> NbtArrayAdapter.fromTag(adapters, listTag);
			case null -> new NullTypeAdapter();
			default -> UnknownTypeAdapter.of(tag);
		};
	}

	@Nullable
	public static Tag toTag(BaseAdapter adapter) {
		return switch (adapter) {
			case BooleanAdapter booleanAdapter -> new ByteTag(booleanAdapter.getBoolean());
			case ByteAdapter byteAdapter -> new ByteTag(byteAdapter.getByte());
			case ShortAdapter shortAdapter -> new ShortTag(shortAdapter.getShort());
			case IntAdapter intAdapter -> new IntTag(intAdapter.getInt());
			case LongAdapter longAdapter -> new LongTag(longAdapter.getLong());
			case FloatAdapter floatAdapter -> new FloatTag(floatAdapter.getFloat());
			case DoubleAdapter doubleAdapter -> new DoubleTag(doubleAdapter.getDouble());
			case CharAdapter charAdapter -> new StringTag(String.valueOf(charAdapter.getChar()));
			case StringAdapter stringAdapter -> new StringTag(stringAdapter.getString());
			case ArrayAdapter arrayAdapter -> NbtArrayAdapter.toTag(arrayAdapter);
			case TableAdapter tableAdapter -> NbtTableAdapter.toTag(tableAdapter);
			case NullTypeAdapter nullAdapter -> null;
			case UnknownTypeAdapter unknown -> unknown.get() instanceof Tag tag ? tag : null;
			default -> throw new IllegalStateException("Unexpected value: " + adapter);
		};
	}
}
