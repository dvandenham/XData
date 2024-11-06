package nl.appelgebakje22.xdata.serializers;

import net.querz.nbt.tag.LongTag;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.api.SimpleSerializer;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import org.jetbrains.annotations.Nullable;

public class LongSerializer extends SimpleSerializer<Long> {

	@Override
	public @Nullable Tag serialize(HolderLookup_Provider registries) {
		return new LongTag(getData());
	}

	@Override
	public void deserialize(Tag tag, HolderLookup_Provider registries) {
		if (!(tag instanceof LongTag longTag)) {
			throw new IllegalArgumentException("Expected LongTag, got %s".formatted(tag.getClass().getName()));
		}
		setData(longTag.asLong());
	}

	public static LongSerializer of(long data) {
		return XData.make(new LongSerializer(), serializer -> serializer.setData(data));
	}
}
