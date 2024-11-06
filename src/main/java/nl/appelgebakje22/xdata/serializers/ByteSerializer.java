package nl.appelgebakje22.xdata.serializers;

import net.querz.nbt.tag.ByteTag;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.api.SimpleSerializer;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import org.jetbrains.annotations.Nullable;

public class ByteSerializer extends SimpleSerializer<Byte> {

	@Override
	public @Nullable Tag serialize(HolderLookup_Provider registries) {
		return new ByteTag(getData());
	}

	@Override
	public void deserialize(Tag tag, HolderLookup_Provider registries) {
		if (!(tag instanceof ByteTag byteTag)) {
			throw new IllegalArgumentException("Expected ByteTag, got %s".formatted(tag.getClass().getName()));
		}
		setData(byteTag.asByte());
	}

	public static ByteSerializer of(byte data) {
		return XData.make(new ByteSerializer(), serializer -> serializer.setData(data));
	}
}
