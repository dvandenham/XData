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
	private int[] test = { 3, 2, 1 };
	@Persisted
	private int test3 = 1;
	@Persisted
	private List<Boolean> test2 = new ArrayList<>(Arrays.asList(false, false, true));

//	@Persisted
//	private Managed2 managed2 = new Managed2();

	public static void main(String[] args) throws IOException {
		XData.init();
		NbtAdapterFactory adapters = new NbtAdapterFactory();
		TestMain main = new TestMain();
		var map = main.getDataMap();
		map.tick();
		String org = SNBTUtil.toSNBT(NbtAdapterFactory.toTag(map.serialize(Operation.FULL, adapters)));
		System.out.println(org);
		//
		//		map.serialize(Operation.PARTIAL, adapters);
		//		serialize(adapters, map);
		//		main.test[1] = 10;
		//		map.tick();
		//		serialize(adapters, map);
		var a = SNBTUtil.fromSNBT(org);
		map.deserialize(Operation.FULL, adapters, (TableAdapter) NbtAdapterFactory.fromTag(adapters, a));
		String org2 = SNBTUtil.toSNBT(NbtAdapterFactory.toTag(map.serialize(Operation.FULL, adapters)));
		System.out.println(org2);
		System.out.println(org.equals(org2));
	}

	private static String serialize(NbtAdapterFactory adapters, ManagedDataMap map) throws IOException {
		TableAdapter serialized = map.serialize(Operation.PARTIAL, adapters);
		String s = SNBTUtil.toSNBT(NbtAdapterFactory.toTag(serialized));
		System.out.println(s);
		return s;
	}

	@Override
	public ManagedDataMap getDataMap() {
		return map;
	}
}
