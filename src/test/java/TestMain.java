import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.querz.nbt.io.SNBTUtil;
import net.querz.nbt.tag.CompoundTag;
import nl.appelgebakje22.xdata.ManagedDataMap;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.api.IManaged;
import nl.appelgebakje22.xdata.api.Persisted;

public class TestMain implements IManaged {

	private final ManagedDataMap map = new ManagedDataMap(this);
	@Persisted
	private int[] test = { 3, 2, 1 };
	@Persisted
	private List<Boolean> test2 = new ArrayList<>(Arrays.asList(false, false, true));

	@Persisted
	private Managed2 managed2 = new Managed2();

	public static void main(String[] args) throws IOException {
		XData.init();
		TestMain main = new TestMain();
		var map = main.getDataMap();
		String input = "{xdata:{\"test2\":[{dat:1b,sid:10},{dat:1b,sid:10},{dat:0b,sid:10}],\"managed2\":{xdata:{testinner:0b}},test:[{dat:1,sid:13},{dat:2,sid:13},{dat:3,sid:13}]}}";
		var nbt = SNBTUtil.fromSNBT(input);
		map.readAllData((CompoundTag) nbt, null);
		String output = SNBTUtil.toSNBT(XData.make(new CompoundTag(), tag -> map.saveAllData(tag, null)));
		System.out.println(map.hasDirtyPersistentFields());
		System.out.println(input.equals(output));
		System.out.println(map.hasDirtyPersistentFields());
		main.test[2] = 100;
		map.tick();
		System.out.println(map.hasDirtyPersistentFields());
		main.test2.add(false);
		System.out.println(map.hasDirtyPersistentFields());
		map.tick();
		System.out.println(map.hasDirtyPersistentFields());
		main.managed2.testinner = !main.managed2.testinner;
		map.tick();
		System.out.println(map.hasDirtyPersistentFields());
		//		var nbt = XData.make(new CompoundTag(), t -> map.saveAllData(t, null));
		//		System.out.println(nbt);
		//		System.out.println(map.getReference(map.getPersistenceFields()[0]).getValueHolder().get());
		//		System.out.println(map.getReference(map.getPersistenceFields()[0]).getValueHolder().get());
		//		System.out.println(Arrays.toString(main.test));
		//		System.out.println(XData.make(new CompoundTag(), t -> map.saveAllData(t, null)));
		//		System.out.println(Arrays.toString(main.test));
		//		main.test2.clear();
		//		System.out.println(Arrays.toString(main.test));
		//		map.readAllData(nbt, null);
		//		System.out.println(Arrays.toString(main.test));
		//		System.out.println(SNBTUtil.toSNBT(nbt));
	}

	@Override
	public ManagedDataMap getDataMap() {
		return map;
	}
}
