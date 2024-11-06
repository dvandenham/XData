package nl.appelgebakje22.xdata.serializers;

import net.querz.nbt.tag.StringTag;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.api.SimpleSerializer;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import org.jetbrains.annotations.Nullable;

public class CharSerializer extends SimpleSerializer<Character> {

	@Override
	public @Nullable Tag serialize(HolderLookup_Provider registries) {
		return new StringTag(String.valueOf(getData()));
	}

	@Override
	public void deserialize(Tag tag, HolderLookup_Provider registries) {
		if (!(tag instanceof StringTag stringTag)) {
			throw new IllegalArgumentException("Expected StringTag, got %s".formatted(tag.getClass().getName()));
		}
		if (stringTag.getValue().length() != 1) {
			throw new IllegalArgumentException("Expected a StringTag with a single character, got '%s'".formatted(stringTag.getValue()));
		}
		setData(stringTag.getValue().charAt(0));
	}

	public static CharSerializer of(char data) {
		return XData.make(new CharSerializer(), serializer -> serializer.setData(data));
	}
}
