package nl.appelgebakje22.xdata.serializers;

import java.util.UUID;
import net.querz.nbt.tag.StringTag;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.api.SimpleSerializer;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import org.jetbrains.annotations.Nullable;

public class UUIDSerializer extends SimpleSerializer<UUID> {

	@Override
	public @Nullable Tag serialize(HolderLookup_Provider registries) {
		return new StringTag(getData().toString());
	}

	@Override
	public void deserialize(Tag tag, HolderLookup_Provider registries) {
		setData(UUID.fromString(this.testTag(tag, StringTag.class).getValue()));
	}

	public static UUIDSerializer of(UUID data) {
		return XData.make(new UUIDSerializer(), serializer -> serializer.setData(data));
	}
}
