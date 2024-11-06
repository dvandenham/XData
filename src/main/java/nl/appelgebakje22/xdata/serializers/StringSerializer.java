package nl.appelgebakje22.xdata.serializers;

import net.querz.nbt.tag.StringTag;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.api.SimpleSerializer;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import org.jetbrains.annotations.Nullable;

public class StringSerializer extends SimpleSerializer<String> {

	@Override
	public @Nullable Tag serialize(HolderLookup_Provider registries) {
		return new StringTag(getData());
	}

	@Override
	public void deserialize(Tag tag, HolderLookup_Provider registries) {
		setData(this.testTag(tag, StringTag.class).getValue());
	}

	public static StringSerializer of(String data) {
		return XData.make(new StringSerializer(), serializer -> serializer.setData(data));
	}
}
