package nl.appelgebakje22.xdata;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.api.Persisted;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.api.Synchronized;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import org.jetbrains.annotations.Nullable;

final class Utils {

	public static String getNotEmptyOrFallback(@Nullable String str, String fallback) {
		return str == null || str.isBlank() ? fallback : str.trim();
	}

	public static ReferenceKey[] collectFields(Class<?> clazz) {
		List<ReferenceKey> resultList = new ArrayList<>();
		for (Field field : clazz.getDeclaredFields()) {
			if (!Modifier.isStatic(field.getModifiers())) {
				boolean persist = field.isAnnotationPresent(Persisted.class);
				boolean synced = field.isAnnotationPresent(Synchronized.class);
				if (persist || synced) {
					if (!XDataRegister.canHandleType(field.getGenericType())) {
						throw new IllegalStateException("Field " + field + " is marked for XData but is not supported.");
					}
					resultList.add(ReferenceKey.of(field));
				}
			}
		}
		return resultList.toArray(new ReferenceKey[0]);
	}

	public static Class<?> getRawType(Type type) {
		return switch (type) {
			case Class<?> aClass -> aClass;
			case GenericArrayType genericArrayType -> getRawType(genericArrayType.getGenericComponentType());
			case ParameterizedType parameterizedType -> getRawType(parameterizedType.getRawType());
			case null, default -> null;
		};
	}

	@Nullable
	public static Tag serializeRef(Reference ref, HolderLookup_Provider registries) {
		ReferenceHandler handler = XDataRegister.getHandler(ref.getKey().getRawType());
		if (handler == null) {
			//TODO logging
			System.out.println("Cannot serialize field %s because no matching %s was registered".formatted(ref.getKey().getRawField(), ReferenceHandler.class.getName()));
			return null;
		}
		final Serializer<?> serializer = handler.readFromReference(ref, registries);
		return serializer.serialize(registries);
	}

	public static void deserializeRef(Reference ref, Tag tag, HolderLookup_Provider registries) {
		ReferenceHandler handler = XDataRegister.getHandler(ref.getKey().getRawType());
		if (handler == null) {
			//TODO logging
			System.out.println("Cannot deserialize field %s because no matching %s was registered".formatted(ref.getKey().getRawField(), ReferenceHandler.class.getName()));
			return;
		}
		final Serializer<?> serializer = XDataRegister.getSerializerByHandler(handler);
		serializer.deserialize(tag, registries);
		handler.writeToReference(ref, serializer, registries);
	}
}
