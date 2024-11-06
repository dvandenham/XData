package nl.appelgebakje22.xdata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import lombok.Getter;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.api.IManaged;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import org.jetbrains.annotations.Nullable;

public class ManagedDataMap {

	private final IManaged managed;
	private final ReferenceKey[] fields;
	private final Map<ReferenceKey, Reference> references;

	@Getter
	private final ReferenceKey[] persistenceFields;
	private final Map<String, ReferenceKey> persistenceMapping;
	private final BitSet dirtyPersistenceFields;

	@Getter
	private final ReferenceKey[] syncFields;
	private final Map<String, ReferenceKey> syncMapper;
	private final BitSet dirtySyncFields;

	public ManagedDataMap(IManaged managed) {
		this.managed = managed;
		this.fields = Utils.collectFields(managed.getClass());

		Map<ReferenceKey, Reference> referenceMap = new HashMap<>();
		ArrayList<ReferenceKey> persistenceFieldList = new ArrayList<>();
		ArrayList<ReferenceKey> syncFieldList = new ArrayList<>();
		for (ReferenceKey field : this.fields) {
			ReflectionHolder holder = ReflectionHolder.of(field.getRawField(), managed);
			Reference reference = Reference.of(field, holder);
			referenceMap.put(field, reference);

			if (field.isPersisted()) {
				reference.setPersistenceStateCallback(dirty -> this.onFieldPersistenceDirty(reference, persistenceFieldList.size(), dirty));
				persistenceFieldList.add(field);
			}
			if (field.isSynchronized()) {
				reference.setSyncStateCallback(dirty -> this.onFieldSyncDirty(reference, syncFieldList.size(), dirty));
				syncFieldList.add(field);
			}
		}
		this.references = Collections.unmodifiableMap(referenceMap);
		this.persistenceFields = persistenceFieldList.toArray(new ReferenceKey[0]);
		this.syncFields = syncFieldList.toArray(new ReferenceKey[0]);

		this.persistenceMapping = Collections.unmodifiableMap(XData.make(new Object2ObjectArrayMap<>(), map -> Arrays.stream(this.persistenceFields).forEach(field -> {
			if (map.containsKey(field.getPersistenceKey())) {
				throw new RuntimeException("Duplicate field with persistence key %s (%s, %s)".formatted(field.getPersistenceKey(), map.get(field.getPersistenceKey()).getRawField(), field.getRawField()));
			}
			map.put(field.getPersistenceKey(), field);
		})));
		this.dirtyPersistenceFields = new BitSet(this.persistenceFields.length);

		this.syncMapper = Collections.unmodifiableMap(XData.make(new Object2ObjectArrayMap<>(), map -> Arrays.stream(this.syncFields).forEach(field -> {
			if (map.containsKey(field.getSyncKey())) {
				throw new RuntimeException("Duplicate field with synchronization key %s (%s, %s)".formatted(field.getSyncKey(), map.get(field.getSyncKey()).getRawField(), field.getRawField()));
			}
			map.put(field.getSyncKey(), field);
		})));
		this.dirtySyncFields = new BitSet(this.syncFields.length);
	}

	private void onFieldPersistenceDirty(Reference reference, int index, boolean isDirty) {
		this.dirtyPersistenceFields.set(index, isDirty);
	}

	private void onFieldSyncDirty(Reference reference, int index, boolean isDirty) {
		this.dirtySyncFields.set(index, isDirty);
	}

	@Nullable
	public Reference getReference(ReferenceKey field) {
		return this.references.get(field);
	}

	public void saveAllData(CompoundTag nbt, HolderLookup_Provider registries) {
		nbt.put(XData.XDATA, XData.make(new CompoundTag(), tag -> this.persistenceMapping.forEach((persistenceKey, field) -> {
			Reference ref = getReference(field);
			Tag serializedRef = Utils.serializeRef(ref, registries);
			if (serializedRef != null) {
				tag.put(persistenceKey, serializedRef);
			}
		})));
	}

	public void readAllData(CompoundTag nbt, HolderLookup_Provider registries) {
		CompoundTag tag = nbt.getCompoundTag(XData.XDATA);
		tag.keySet().forEach(tagKey -> {
			ReferenceKey field = this.persistenceMapping.get(tagKey);
			if (field == null) {
				//TODO log
				System.out.println("Cannot deserialize data for key " + tagKey + " since it has no mapping.");
			} else {
				Reference ref = getReference(field);
				Utils.deserializeRef(ref, tag.get(tagKey), registries);
			}
		});
	}
}
