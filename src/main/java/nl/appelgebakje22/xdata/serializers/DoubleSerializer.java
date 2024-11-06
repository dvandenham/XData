package nl.appelgebakje22.xdata.serializers;

import net.querz.nbt.tag.DoubleTag;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.api.SimpleSerializer;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import org.jetbrains.annotations.Nullable;

public class DoubleSerializer extends SimpleSerializer<Double> {

	@Override
	public @Nullable Tag serialize(HolderLookup_Provider registries) {
		return new DoubleTag(getData());
	}

	@Override
	public void deserialize(Tag tag, HolderLookup_Provider registries) {
		if (!(tag instanceof DoubleTag doubleTag)) {
			throw new IllegalArgumentException("Expected DoubleTag, got %s".formatted(tag.getClass().getName()));
		}
		setData(doubleTag.asDouble());
	}

	public static DoubleSerializer of(double data) {
		return XData.make(new DoubleSerializer(), serializer -> serializer.setData(data));
	}
}
