import nl.appelgebakje22.xdata.ManagedDataMap;
import nl.appelgebakje22.xdata.api.IManaged;
import nl.appelgebakje22.xdata.api.Persisted;

public class Managed2 implements IManaged {

	private final ManagedDataMap map = new ManagedDataMap(this);

	@Persisted
	public boolean testinner = true;

	@Override
	public ManagedDataMap getDataMap() {
		return map;
	}
}
