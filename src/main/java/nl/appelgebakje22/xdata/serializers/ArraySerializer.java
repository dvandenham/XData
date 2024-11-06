package nl.appelgebakje22.xdata.serializers;

import lombok.Getter;
import lombok.Setter;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.XDataRegister;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import nl.appelgebakje22.xdata.dummyclasses.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

public class ArraySerializer implements Serializer<Serializer<?>[]> {

	@Getter
	@Setter
	private Serializer<?>[] data;

	@Override
	public @Nullable Tag serialize(HolderLookup_Provider registries) {
		ListTag result = ListTag.createUnchecked(CompoundTag.class);
		for (int i = 0; i < getData().length; ++i) {
			Serializer<?> serializer = this.getData()[i];
			CompoundTag entryTag = new CompoundTag();
			entryTag.putInt("sid", serializer.getSid());
			Tag dataTag = serializer.serialize(registries);
			if (dataTag != null) {
				entryTag.put("dat", dataTag);
			}
			result.add(entryTag);
		}
		return result;
	}

	@Override
	public void deserialize(Tag tag, HolderLookup_Provider registries) {
		ListTag list = this.testTag(tag, ListTag.class);
		Serializer<?>[] arr = new Serializer[list.size()];
		for (int i = 0; i < arr.length; ++i) {
			CompoundTag entryTag = this.testTag(list.get(i), CompoundTag.class);
			arr[i] = XDataRegister.getSerializerById(entryTag.getInt("sid"));
			if (arr[i] == null) {
				throw new IllegalStateException("Could not create %s with id %s".formatted(Serializer.class.getName(), entryTag.getInt("sid")));
			}
			final Tag dataTag = entryTag.get("dat");
			if (dataTag != null) {
				arr[i].deserialize(dataTag, registries);
			}
		}
		this.setData(arr);
	}

	@Override
	public void toNetwork(RegistryFriendlyByteBuf buf) {
		//TODO implement
	}

	@Override
	public void fromNetwork(RegistryFriendlyByteBuf buf) {
		//TODO implement
	}

	public static ArraySerializer of(Serializer<?>[] data) {
		return XData.make(new ArraySerializer(), serializer -> serializer.setData(data));
	}
}
