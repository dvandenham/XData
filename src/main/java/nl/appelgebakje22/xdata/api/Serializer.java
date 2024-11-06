package nl.appelgebakje22.xdata.api;

import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.XDataRegister;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import nl.appelgebakje22.xdata.dummyclasses.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

public interface Serializer<T> {

	@Nullable
	Tag serialize(HolderLookup_Provider registries);

	void deserialize(Tag tag, HolderLookup_Provider registries);

	void toNetwork(RegistryFriendlyByteBuf buf);

	void fromNetwork(RegistryFriendlyByteBuf buf);

	T getData();

	default <A extends Tag> A testTag(Tag tag, Class<A> expectedType) {
		if (expectedType.isAssignableFrom(tag.getClass())) {
			return expectedType.cast(tag);
		}
		throw new IllegalArgumentException("Expected %s of type %s, got: %s".formatted(Tag.class.getName(), expectedType.getName(), tag.getClass().getName()));
	}

	default int getSid() {
		return XDataRegister.getSerializerId(this);
	}
}
