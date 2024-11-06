import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nbt.NbtAdapterFactory;
import net.querz.nbt.io.SNBTUtil;
import nl.appelgebakje22.xdata.ManagedDataMap;
import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.TableAdapter;
import nl.appelgebakje22.xdata.api.IManaged;
import nl.appelgebakje22.xdata.api.Persisted;

public class TestMain implements IManaged {

	private final ManagedDataMap map = new ManagedDataMap(this);
	@Persisted
	private final int[] test = { 3, 2, 1 };
	@Persisted
	private final int test3 = 1;
	@Persisted
	private final List<Boolean> test2 = new ArrayList<>(Arrays.asList(false, false, true));

	//	@Persisted
	//	private Managed2 managed2 = new Managed2();

	public static void main(final String[] args) throws IOException {
		XData.init();
		final NbtAdapterFactory adapters = new NbtAdapterFactory();
		final TestMain main = new TestMain();
		final var map = main.getDataMap();
		map.tick();
		final String org = SNBTUtil.toSNBT(NbtAdapterFactory.toTag(map.serialize(Operation.FULL, adapters)));
		System.out.println(org);
		//
		//		map.serialize(Operation.PARTIAL, adapters);
		//		serialize(adapters, map);
		//		main.test[1] = 10;
		//		map.tick();
		//		serialize(adapters, map);
		final var a = SNBTUtil.fromSNBT(org);
		map.deserialize(Operation.FULL, adapters, (TableAdapter) NbtAdapterFactory.fromTag(adapters, a));
		final String org2 = SNBTUtil.toSNBT(NbtAdapterFactory.toTag(map.serialize(Operation.FULL, adapters)));
		System.out.println(org2);
		System.out.println(org.equals(org2));
	}

	private static String serialize(final NbtAdapterFactory adapters, final ManagedDataMap map) throws IOException {
		final TableAdapter serialized = map.serialize(Operation.PARTIAL, adapters);
		final String s = SNBTUtil.toSNBT(NbtAdapterFactory.toTag(serialized));
		System.out.println(s);
		return s;
	}

	@Override
	public ManagedDataMap getDataMap() {
		return this.map;
	}
}
