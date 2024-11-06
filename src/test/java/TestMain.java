import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.querz.nbt.tag.CompoundTag;
import nl.appelgebakje22.xdata.ManagedDataMap;
import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.XData;
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

	@Persisted
	private Managed2 managed2 = new Managed2();

	public static void main(String[] args) throws IOException {
		XData.init();
		TestMain main = new TestMain();
		var map = main.getDataMap();
		map.tick();
		System.out.println(XData.make(new CompoundTag(), nbt -> map.saveToNbt(Operation.PARTIAL, nbt, null)));
		main.test3 = 10;
		map.tick();
		System.out.println(XData.make(new CompoundTag(), nbt -> map.saveToNbt(Operation.PARTIAL, nbt, null)));
	}

	@Override
	public ManagedDataMap getDataMap() {
		return map;
	}
}
