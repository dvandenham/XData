package nl.appelgebakje22.xdata;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import nl.appelgebakje22.xdata.api.Checker;
import nl.appelgebakje22.xdata.api.Copier;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.handlers.ArrayHandler;
import nl.appelgebakje22.xdata.handlers.CollectionHandler;
import nl.appelgebakje22.xdata.serializers.ArraySerializer;
import org.jetbrains.annotations.Nullable;

public final class XDataRegister {

	private static final int DEFAULT_PRIORITY = 1000;
	private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);

	private static final Object2IntMap<Class<?>> SERIALIZER_REGISTRY = new Object2IntArrayMap<>();
	private static final Int2ObjectMap<Supplier<? extends Serializer<?>>> SERIALIZER_FACTORIES = new Int2ObjectArrayMap<>();
	private static final AtomicInteger SERIALIZER_ID_HOLDER = new AtomicInteger(10); //Reserve a couple for the future

	private static final Object2IntMap<ReferenceHandler> HANDLERS_UNSORTED = new Object2IntArrayMap<>();
	private static final List<ReferenceHandler> HANDLERS_SORTED = new ArrayList<>();
	private static final Object2IntMap<ReferenceHandler> HANDLER_TO_SERIALIZER_MAPPING = new Object2IntArrayMap<>();
	private static final Object2ObjectMap<Class<?>, ReferenceHandler> HANDLER_TYPE_CACHE = new Object2ObjectArrayMap<>();

	private static final List<Copier<?>> COPIERS = new ArrayList<>();
	private static final Object2ObjectMap<Class<?>, Copier<?>> COPIER_TYPE_CACHE = new Object2ObjectArrayMap<>();
	private static final List<Checker<?>> CHECKERS = new ArrayList<>();
	private static final Object2ObjectMap<Class<?>, Checker<?>> CHECKER_TYPE_CACHE = new Object2ObjectArrayMap<>();

	public static <T extends Serializer<?>> int register(Class<T> serializerType, Supplier<T> factory) {
		if (INITIALIZED.get()) {
			throw new IllegalStateException("Cannot register %s %s after initialization!".formatted(Serializer.class.getName(), serializerType.getName()));
		}
		int id = SERIALIZER_REGISTRY.getOrDefault(serializerType, -1);
		if (id == -1) {
			id = SERIALIZER_ID_HOLDER.getAndIncrement();
			SERIALIZER_REGISTRY.put(serializerType, id);
			SERIALIZER_FACTORIES.put(id, factory);
		}
		return id;
	}

	public static <T extends Serializer<?>> void register(Class<T> serializerType, Supplier<T> factory, ReferenceHandler handler, int priority) {
		if (INITIALIZED.get()) {
			throw new IllegalStateException("Cannot register %s %s after initialization!".formatted(ReferenceHandler.class.getName(), handler.getClass().getName()));
		}
		int serializerId = register(serializerType, factory);
		HANDLERS_UNSORTED.put(handler, priority);
		HANDLER_TO_SERIALIZER_MAPPING.put(handler, serializerId);
	}

	public static <T extends Serializer<?>> void register(Class<T> serializerType, Supplier<T> factory, ReferenceHandler handler) {
		register(serializerType, factory, handler, DEFAULT_PRIORITY);
	}

	public static void register(Copier<?> copier) {
		if (INITIALIZED.get()) {
			throw new IllegalStateException("Cannot register %s %s after initialization!".formatted(Copier.class.getName(), copier.getClass().getName()));
		}
		COPIERS.add(copier);
	}

	public static void register(Checker<?> checker) {
		if (INITIALIZED.get()) {
			throw new IllegalStateException("Cannot register %s %s after initialization!".formatted(Checker.class.getName(), checker.getClass().getName()));
		}
		CHECKERS.add(checker);
	}

	static void freeze() {
		XDataRegister.INITIALIZED.set(true);
		HANDLERS_SORTED.addAll(HANDLERS_UNSORTED.object2IntEntrySet().stream().sorted((o1, o2) -> Integer.compare(o2.getIntValue(), o1.getIntValue())).map(Map.Entry::getKey).toList());
	}

	public static boolean canHandleType(Type type) {
		return getHandler(type) != null;
	}

	public static int getSerializerId(Serializer<?> serializer) {
		return SERIALIZER_REGISTRY.getInt(serializer.getClass());
	}

	public static Serializer<?> getSerializerById(int id) {
		return SERIALIZER_FACTORIES.get(id).get();
	}

	public static Serializer<?> getSerializerByHandler(ReferenceHandler handler) {
		if (handler instanceof ArrayHandler || handler instanceof CollectionHandler) {
			return new ArraySerializer();
		}
		int id = HANDLER_TO_SERIALIZER_MAPPING.getInt(handler);
		return getSerializerById(id);
	}

	@Nullable
	public static ReferenceHandler getHandler(Type clazz) {
		if (clazz instanceof GenericArrayType array) {
			Type contentType = array.getGenericComponentType();
			ReferenceHandler contentHandler = getHandler(contentType);
			Class<?> rawType = Utils.getRawType(contentType);
			return ArrayHandler.FACTORY.apply(contentHandler, rawType == null ? Object.class : rawType);
		}
		Class<?> rawType = Utils.getRawType(clazz);
		if (rawType != null) {
			if (rawType.isArray()) {
				Class<?> contentType = rawType.getComponentType();
				ReferenceHandler contentHandler = getHandler(contentType);
				return ArrayHandler.FACTORY.apply(contentHandler, contentType);
			}
			if (Collection.class.isAssignableFrom(rawType)) {
				Type contentType = ((ParameterizedType) clazz).getActualTypeArguments()[0];
				ReferenceHandler contentHandler = getHandler(contentType);
				Class<?> rawContentType = Utils.getRawType(contentType);
				return CollectionHandler.FACTORY.apply(contentHandler, rawContentType == null ? Object.class : rawContentType);
			}
			return XDataRegister.getHandlerByClass(rawType);
		}
		return null;
	}

	@Nullable
	private static ReferenceHandler getHandlerByClass(Class<?> clazz) {
		if (!INITIALIZED.get()) {
			throw new IllegalStateException("Cannot fetch %s during initialization!".formatted(ReferenceHandler.class.getName()));
		}
		return HANDLER_TYPE_CACHE.computeIfAbsent(clazz, ignored -> HANDLERS_SORTED.stream().filter(entry -> entry.canHandle(clazz)).findFirst().orElse(null));
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public static <T> Copier<T> getCopier(Class<T> clazz) {
		return (Copier<T>) COPIER_TYPE_CACHE.computeIfAbsent(clazz, ignored -> COPIERS.stream().filter(copier -> copier.canHandle(clazz)).findFirst().orElse(null));
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public static <T> Checker<T> getChecker(Class<T> clazz) {
		return (Checker<T>) CHECKER_TYPE_CACHE.computeIfAbsent(clazz, ignored -> CHECKERS.stream().filter(checker -> checker.canHandle(clazz)).findFirst().orElse(null));
	}
}
