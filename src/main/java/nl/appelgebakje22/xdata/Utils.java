package nl.appelgebakje22.xdata;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.api.Checker;
import nl.appelgebakje22.xdata.api.Copier;
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
			System.out.printf("Cannot deserialize field %s because no matching %s was registered%n", ref.getKey().getRawField(), ReferenceHandler.class.getName());
			return;
		}
		final Serializer<?> serializer = XDataRegister.getSerializerByHandler(handler);
		serializer.deserialize(tag, registries);
		handler.writeToReference(ref, serializer, registries);
	}

	@SuppressWarnings({ "SuspiciousSystemArraycopy", "rawtypes", "unchecked" })
	public static Object cloneGenericArray(Object array, boolean isArray) {
		if (isArray) {
			Class<?> componentType = array.getClass().getComponentType();
			if (componentType.isPrimitive()) {
				Object newArray = Array.newInstance(componentType, Array.getLength(array));
				System.arraycopy(array, 0, newArray, 0, Array.getLength(newArray));
				return newArray;
			}
			Object[] newArray = new Object[Array.getLength(array)];
			for (int i = 0; i < newArray.length; ++i) {
				Object item = Array.get(array, i);
				if (item != null) {
					Copier copier = XDataRegister.getCopier(item.getClass());
					newArray[i] = copier != null ? copier.copy(item) : item;
				}
			}
			return newArray;
		}
		Collection c = (Collection) array;
		Object[] result = new Object[c.size()];
		int i = 0;
		for (Object item : c) {
			if (item != null) {
				Copier copier = XDataRegister.getCopier(item.getClass());
				result[i++] = copier != null ? copier.copy(item) : item;
			}
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean isEqual(Object value1, Object value2) {
		if ((value1 == null) != (value2 == null)) {
			return false;
		}
		if (value1 == null) {
			return true;
		}
		if (!value1.getClass().isAssignableFrom(value2.getClass()) && !value2.getClass().isAssignableFrom(value1.getClass())) {
			return false;
		}
		Checker checker1 = XDataRegister.getChecker(value1.getClass());
		Checker checker2 = XDataRegister.getChecker(value2.getClass());
		if (checker1 != checker2) {
			return false;
		}
		return checker1 == null ? Objects.equals(value1, value2) : checker1.equals(value1, value2) && checker2.equals(value1, value2);
	}
}
