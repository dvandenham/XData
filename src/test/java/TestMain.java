import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.querz.nbt.tag.CompoundTag;
import nl.appelgebakje22.xdata.ManagedDataMap;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.api.IManaged;
import nl.appelgebakje22.xdata.api.Persisted;

public class TestMain implements IManaged {

	@Persisted
	private int[] test = { 1, 2, 3 };
	@Persisted
	private List<Boolean> test2 = new ArrayList<>(Arrays.asList(true, true, false));

	public static void main(String[] args) {
		XData.init();
		TestMain main = new TestMain();
		var map = main.getDataMap();
		var nbt = XData.make(new CompoundTag(), t -> map.saveAllData(t, null));
		System.out.println(nbt);
		System.out.println(map.getReference(map.getPersistenceFields()[0]).getValueHolder().get());
		System.out.println(map.getReference(map.getPersistenceFields()[0]).getValueHolder().get());
		System.out.println(Arrays.toString(main.test));
		System.out.println(XData.make(new CompoundTag(), t -> map.saveAllData(t, null)));
		System.out.println(Arrays.toString(main.test));
		main.test2.clear();
		System.out.println(Arrays.toString(main.test));
		map.readAllData(nbt, null);
		System.out.println(Arrays.toString(main.test));
	}

	@Override
	public ManagedDataMap getDataMap() {
		return new ManagedDataMap(this);
	}
}
