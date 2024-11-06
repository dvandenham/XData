package nl.appelgebakje22.xdata;

import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import nl.appelgebakje22.xdata.ref.Reference;
import org.jetbrains.annotations.Nullable;

public class XDataSerializationUtils {

	@Nullable
	public static Tag writeRefToNbt(Operation operation, Reference ref, HolderLookup_Provider registries) {
		ReferenceHandler handler = XDataRegister.getHandler(ref.getKey().getRawType());
		if (handler == null) {
			//TODO logging
			System.out.println("Cannot serialize field %s because no matching %s was registered".formatted(ref.getKey().getRawField(), ReferenceHandler.class.getName()));
			return null;
		}
		final Serializer<?> serializer = handler.readFromReference(operation, ref, registries);
		return serializer.serialize(registries);
	}

	public static void readRefFromNbt(Operation operation, Reference ref, Tag tag, HolderLookup_Provider registries) {
		ReferenceHandler handler = XDataRegister.getHandler(ref.getKey().getRawType());
		if (handler == null) {
			//TODO logging
			System.out.printf("Cannot deserialize field %s because no matching %s was registered%n", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return;
		}
		final Serializer<?> serializer = XDataRegister.getSerializerByHandler(handler);
		serializer.deserialize(tag, registries);
		handler.writeToReference(operation, ref, serializer, registries);
	}
}
