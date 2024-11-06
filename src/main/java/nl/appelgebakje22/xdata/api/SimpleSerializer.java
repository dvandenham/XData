package nl.appelgebakje22.xdata.api;

import lombok.Getter;
import lombok.Setter;
import nl.appelgebakje22.xdata.dummyclasses.RegistryFriendlyByteBuf;

public abstract class SimpleSerializer<T> implements Serializer<T> {

	@Getter
	@Setter
	private T data;

	@Override
	public void toNetwork(RegistryFriendlyByteBuf buf) {
		//TODO write nbt by default
	}

	@Override
	public void fromNetwork(RegistryFriendlyByteBuf buf) {
		//TODO read nbt by default
	}
}
