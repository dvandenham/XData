package nl.appelgebakje22.xdata.serializers;

import net.querz.nbt.tag.FloatTag;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.api.SimpleSerializer;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import org.jetbrains.annotations.Nullable;

public class FloatSerializer extends SimpleSerializer<Float> {

	@Override
	public @Nullable Tag serialize(HolderLookup_Provider registries) {
		return new FloatTag(getData());
	}

	@Override
	public void deserialize(Tag tag, HolderLookup_Provider registries) {
		if (!(tag instanceof FloatTag floatTag)) {
			throw new IllegalArgumentException("Expected FloatTag, got %s".formatted(tag.getClass().getName()));
		}
		setData(floatTag.asFloat());
	}

	public static FloatSerializer of(float data) {
		return XData.make(new FloatSerializer(), serializer -> serializer.setData(data));
	}
}
