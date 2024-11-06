package nl.appelgebakje22.xdata.serializers;

import net.querz.nbt.tag.IntTag;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.api.SimpleSerializer;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import org.jetbrains.annotations.Nullable;

public class IntSerializer extends SimpleSerializer<Integer> {

	@Override
	public @Nullable Tag serialize(HolderLookup_Provider registries) {
		return new IntTag(getData());
	}

	@Override
	public void deserialize(Tag tag, HolderLookup_Provider registries) {
		if (!(tag instanceof IntTag integerTag)) {
			throw new IllegalArgumentException("Expected IntegerTag, got %s".formatted(tag.getClass().getName()));
		}
		setData(integerTag.asInt());
	}

	public static IntSerializer of(int data) {
		return XData.make(new IntSerializer(), serializer -> serializer.setData(data));
	}
}
