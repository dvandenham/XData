package nl.appelgebakje22.xdata;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.Pair;
import nl.appelgebakje22.xdata.handlers.BooleanHandler;
import nl.appelgebakje22.xdata.handlers.ByteHandler;
import nl.appelgebakje22.xdata.handlers.CharHandler;
import nl.appelgebakje22.xdata.handlers.DoubleHandler;
import nl.appelgebakje22.xdata.handlers.FloatHandler;
import nl.appelgebakje22.xdata.handlers.IManagedHandler;
import nl.appelgebakje22.xdata.handlers.IntHandler;
import nl.appelgebakje22.xdata.handlers.LongHandler;
import nl.appelgebakje22.xdata.handlers.ShortHandler;
import nl.appelgebakje22.xdata.serializers.ArraySerializer;
import nl.appelgebakje22.xdata.serializers.BooleanSerializer;
import nl.appelgebakje22.xdata.serializers.ByteSerializer;
import nl.appelgebakje22.xdata.serializers.CharSerializer;
import nl.appelgebakje22.xdata.serializers.DoubleSerializer;
import nl.appelgebakje22.xdata.serializers.FloatSerializer;
import nl.appelgebakje22.xdata.serializers.IntSerializer;
import nl.appelgebakje22.xdata.serializers.LongSerializer;
import nl.appelgebakje22.xdata.serializers.NbtSerializer;
import nl.appelgebakje22.xdata.serializers.ShortSerializer;

public final class XData {

	public static final String XDATA = "xdata";

	public static void init() {
		XDataRegister.register(BooleanSerializer.class, BooleanSerializer::new, new BooleanHandler());
		XDataRegister.register(ByteSerializer.class, ByteSerializer::new, new ByteHandler());
		XDataRegister.register(ShortSerializer.class, ShortSerializer::new, new ShortHandler());
		XDataRegister.register(IntSerializer.class, IntSerializer::new, new IntHandler());
		XDataRegister.register(LongSerializer.class, LongSerializer::new, new LongHandler());
		XDataRegister.register(FloatSerializer.class, FloatSerializer::new, new FloatHandler());
		XDataRegister.register(DoubleSerializer.class, DoubleSerializer::new, new DoubleHandler());
		XDataRegister.register(CharSerializer.class, CharSerializer::new, new CharHandler());

		XDataRegister.register(ArraySerializer.class, ArraySerializer::new);

		XDataRegister.register(NbtSerializer.class, NbtSerializer::new, new IManagedHandler());

		XDataRegister.freeze();
	}

	public static <T> T make(T obj, Consumer<T> mod) {
		mod.accept(obj);
		return obj;
	}

	public static <A, B, Z> BiFunction<A, B, Z> memoize(final BiFunction<A, B, Z> memoBiFunction) {
		return new BiFunction<>() {

			private final Map<Pair<A, B>, Z> cache = new ConcurrentHashMap<>();

			public Z apply(A a, B b) {
				return this.cache.computeIfAbsent(Pair.of(a, b), key -> memoBiFunction.apply(key.first(), key.second()));
			}

			public String toString() {
				return "memoize/2[function=" + memoBiFunction + ", size=" + this.cache.size() + "]";
			}
		};
	}
}
