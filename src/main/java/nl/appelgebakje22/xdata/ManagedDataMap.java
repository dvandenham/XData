package nl.appelgebakje22.xdata;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import lombok.Getter;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.api.IManaged;
import nl.appelgebakje22.xdata.api.Persisted;
import nl.appelgebakje22.xdata.api.Synchronized;
import nl.appelgebakje22.xdata.dummyclasses.HolderLookup_Provider;
import nl.appelgebakje22.xdata.ref.Reference;
import nl.appelgebakje22.xdata.ref.ReferenceKey;
import nl.appelgebakje22.xdata.ref.ReflectionHolder;
import org.jetbrains.annotations.Nullable;

public class ManagedDataMap {

	private final IManaged managed;
	private final ReferenceKey[] fields;
	private final Map<ReferenceKey, Reference> references;

	@Getter
	private final Object2IntMap<ReferenceKey> persistenceFields;
	private final Map<String, ReferenceKey> persistenceMapping;
	private final BitSet dirtyPersistenceFields;

	@Getter
	private final Object2IntMap<ReferenceKey> syncFields;
	private final Map<String, ReferenceKey> syncMapper;
	private final BitSet dirtySyncFields;

	public ManagedDataMap(IManaged managed) {
		this.managed = managed;
		this.fields = collectFields(managed.getClass());

		Map<ReferenceKey, Reference> referenceMap = new HashMap<>();
		ArrayList<ReferenceKey> persistenceFieldList = new ArrayList<>();
		ArrayList<ReferenceKey> syncFieldList = new ArrayList<>();
		for (ReferenceKey field : this.fields) {
			ReflectionHolder holder = ReflectionHolder.of(field.getRawField(), managed);
			Reference reference = Reference.of(field, holder);
			referenceMap.put(field, reference);
			if (field.isPersisted()) {
				persistenceFieldList.add(field);
			}
			if (field.isSynchronized()) {
				syncFieldList.add(field);
			}
		}
		this.references = Collections.unmodifiableMap(referenceMap);

		Object2IntMap<ReferenceKey> persistenceFields = new Object2IntArrayMap<>();
		for (int i = 0; i < persistenceFieldList.size(); ++i) {
			ReferenceKey refKey = persistenceFieldList.get(i);
			Reference ref = this.references.get(refKey);
			int finalI = i;
			ref.setPersistenceStateCallback(dirty -> this.onFieldPersistenceDirty(ref, finalI, dirty));
			persistenceFields.put(refKey, i);
		}
		this.persistenceFields = Object2IntMaps.unmodifiable(persistenceFields);
		Object2IntMap<ReferenceKey> syncFields = new Object2IntArrayMap<>();
		for (int i = 0; i < syncFieldList.size(); ++i) {
			ReferenceKey refKey = syncFieldList.get(i);
			Reference ref = this.references.get(refKey);
			int finalI = i;
			ref.setSyncStateCallback(dirty -> this.onFieldSyncDirty(ref, finalI, dirty));
			syncFields.put(refKey, i);
		}
		this.syncFields = Object2IntMaps.unmodifiable(syncFields);

		this.persistenceMapping = Collections.unmodifiableMap(XData.make(new Object2ObjectArrayMap<>(), map -> this.persistenceFields.keySet().forEach(field -> {
			if (map.containsKey(field.getPersistenceKey())) {
				throw new RuntimeException("Duplicate field with persistence key %s (%s, %s)".formatted(field.getPersistenceKey(), map.get(field.getPersistenceKey()).getRawField(), field.getRawField()));
			}
			map.put(field.getPersistenceKey(), field);
		})));
		this.dirtyPersistenceFields = new BitSet(this.persistenceFields.size());

		this.syncMapper = Collections.unmodifiableMap(XData.make(new Object2ObjectArrayMap<>(), map -> this.syncFields.keySet().forEach(field -> {
			if (map.containsKey(field.getSyncKey())) {
				throw new RuntimeException("Duplicate field with synchronization key %s (%s, %s)".formatted(field.getSyncKey(), map.get(field.getSyncKey()).getRawField(), field.getRawField()));
			}
			map.put(field.getSyncKey(), field);
		})));
		this.dirtySyncFields = new BitSet(this.syncFields.size());
	}

	private void onFieldPersistenceDirty(Reference reference, int index, boolean isDirty) {
		this.dirtyPersistenceFields.set(index, isDirty);
	}

	private void onFieldSyncDirty(Reference reference, int index, boolean isDirty) {
		this.dirtySyncFields.set(index, isDirty);
	}

	public void init() {
		this.tick();
	}

	public void tick() {
		this.references.values().forEach(Reference::tick);
		if (hasDirtySyncFields()) {
			//TODO SYNC
		}
	}

	@Nullable
	public Reference getReference(ReferenceKey field) {
		return this.references.get(field);
	}

	public boolean hasDirtyPersistentFields() {
		return !this.dirtyPersistenceFields.isEmpty();
	}

	public boolean hasDirtySyncFields() {
		return !this.dirtySyncFields.isEmpty();
	}

	public void saveToNbt(Operation operation, CompoundTag nbt, HolderLookup_Provider registries) {
		nbt.put(this.managed.getSerializationRootTag(), XData.make(new CompoundTag(), tag -> this.persistenceMapping.entrySet().stream().filter(entry -> switch (operation) {
			case FULL -> true;
			case PARTIAL -> {
				var key = entry.getValue();
				var index = this.persistenceFields.getInt(key);
				boolean enable = this.dirtyPersistenceFields.get(index);
				yield enable;
			}
		}).forEach(entry -> {
			Reference ref = getReference(entry.getValue());
			Tag serializedRef = XDataSerializationUtils.writeRefToNbt(operation, ref, registries);
			if (serializedRef != null) {
				tag.put(entry.getKey(), serializedRef);
			}
			ref.clearPersistenceMark();
		})));
	}

	public void readFromNbt(Operation operation, CompoundTag nbt, HolderLookup_Provider registries) {
		CompoundTag tag = nbt.getCompoundTag(this.managed.getSerializationRootTag());
		tag.keySet().forEach(tagKey -> {
			ReferenceKey field = this.persistenceMapping.get(tagKey);
			if (field == null) {
				//TODO log
				System.out.println("Cannot deserialize data for key " + tagKey + " since it has no mapping.");
			} else {
				Reference ref = getReference(field);
				XDataSerializationUtils.readRefFromNbt(operation, ref, tag.get(tagKey), registries);
				ref.clearPersistenceMark();
			}
		});
	}

	private static ReferenceKey[] collectFields(Class<?> clazz) {
		List<ReferenceKey> resultList = new ArrayList<>();
		for (Field field : clazz.getDeclaredFields()) {
			if (!Modifier.isStatic(field.getModifiers())) {
				boolean persist = field.isAnnotationPresent(Persisted.class);
				boolean synced = field.isAnnotationPresent(Synchronized.class);
				if (persist || synced) {
					if (!XDataRegister.canHandleType(field.getGenericType())) {
						throw new IllegalStateException("Field " + field + " is marked for XData but is not supported.");
					}
					resultList.add(ReferenceKey.of(field));
				}
			}
		}
		return resultList.toArray(new ReferenceKey[0]);
	}
}
