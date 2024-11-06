package nl.appelgebakje22.xdata.serializers;

import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.api.SimpleSerializer;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import nl.appelgebakje22.xdata.dummyclasses.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

public class NbtSerializer extends SimpleSerializer<Tag> {

	@Override
	public @Nullable Tag serialize(HolderLookup_Provider registries) {
		return getData();
	}

	@Override
	public void deserialize(Tag tag, HolderLookup_Provider registries) {
		this.setData(tag);
	}

	@Override
	public void toNetwork(RegistryFriendlyByteBuf buf) {
		//TODO implement
	}

	@Override
	public void fromNetwork(RegistryFriendlyByteBuf buf) {
		//TODO implement
	}

	public static NbtSerializer of(Tag data) {
		return XData.make(new NbtSerializer(), serializer -> serializer.setData(data));
	}
}
