package nl.appelgebakje22.xdata.ref;

import nl.appelgebakje22.xdata.ManagedDataMap;
import nl.appelgebakje22.xdata.api.Holder;
import nl.appelgebakje22.xdata.api.IManaged;

public class IManagedOuterReference extends Reference {

	IManagedOuterReference(ReferenceKey key, Holder valueHolder) {
		super(key, valueHolder);
	}

	@Override
	public void tick() {
		super.tick();
		ManagedDataMap map = ((IManaged) getValueHolder().get()).getDataMap();
		map.tick();
		if (map.hasDirtyPersistentFields()) {
			markPersistenceDirty();
		}
		if (map.hasDirtySyncFields()) {
			markSyncDirty();
		}
	}
}
