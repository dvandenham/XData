package nl.appelgebakje22.xdata.serializers;

import net.querz.nbt.tag.ShortTag;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.api.SimpleSerializer;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import org.jetbrains.annotations.Nullable;

public class ShortSerializer extends SimpleSerializer<Short> {

	@Override
	public @Nullable Tag serialize(HolderLookup_Provider registries) {
		return new ShortTag(getData());
	}

	@Override
	public void deserialize(Tag tag, HolderLookup_Provider registries) {
		if (!(tag instanceof ShortTag shortTag)) {
			throw new IllegalArgumentException("Expected ShortTag, got %s".formatted(tag.getClass().getName()));
		}
		setData(shortTag.asShort());
	}

	public static ShortSerializer of(short data) {
		return XData.make(new ShortSerializer(), serializer -> serializer.setData(data));
	}
}
