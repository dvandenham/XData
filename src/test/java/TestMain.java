import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nbt.NbtAdapterFactory;
import net.querz.nbt.io.SNBTUtil;
import nettest.ByteArrayNetworkOutput;
import nl.appelgebakje22.xdata.ManagedDataMap;
import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.TableAdapter;
import nl.appelgebakje22.xdata.api.IManaged;
import nl.appelgebakje22.xdata.api.Persisted;
import nl.appelgebakje22.xdata.api.Synchronized;

public class TestMain implements IManaged {

	private final ManagedDataMap map = new ManagedDataMap(this);
	@Persisted
	@Synchronized
	private final int[] test = { 3, 2, 1 };
	@Persisted
	@Synchronized
	private final int test3 = 1;
	@Persisted
	@Synchronized
	private final List<Boolean> test2 = new ArrayList<>(Arrays.asList(false, false, true));

	//	@Persisted
	//	private Managed2 managed2 = new Managed2();

	public static void main(final String[] args) throws IOException {
		XData.init();
		final NbtAdapterFactory adapters = new NbtAdapterFactory();
		final TestMain main = new TestMain();
		final var map = main.getDataMap();
		map.tick();
		final ByteArrayNetworkOutput out = new ByteArrayNetworkOutput();
		map.toNetwork(Operation.FULL, out);
		System.out.println(out.toByteArray().length);
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
