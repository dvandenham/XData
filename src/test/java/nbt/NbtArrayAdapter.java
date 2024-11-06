package nbt;

import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.ArrayAdapter;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.BaseArrayAdapter;
import nl.appelgebakje22.xdata.adapter.NullTypeAdapter;

class NbtArrayAdapter extends BaseArrayAdapter {

	public NbtArrayAdapter(NbtAdapterFactory adapters) {
		super(adapters);
	}

	public static Tag toTag(ArrayAdapter adapter) {
		return XData.make(new ListTag(CompoundTag.class), nbt -> {
			for (int i = 0; i < adapter.size(); ++i) {
				CompoundTag entry = new CompoundTag();
				entry.putInt("i", i);
				Tag serializedTag = NbtAdapterFactory.toTag(adapter.get(i));
				if (serializedTag != null) {
					entry.put("dat", serializedTag);
				}
				nbt.add(entry);
			}
		});
	}

	public static ArrayAdapter fromTag(AdapterFactory adapters, ListTag list) {
		BaseAdapter[] adapterArray = new BaseAdapter[list.size()];
		for (int i = 0; i < list.size(); ++i) {
			Tag entry = list.get(i);
			if (entry instanceof CompoundTag compoundTag) {
				int index = compoundTag.getInt("i");
				if (compoundTag.containsKey("dat")) {
					adapterArray[index] = NbtAdapterFactory.fromTag(adapters, compoundTag.get("dat"));
				}
			}
		}
		return XData.make(adapters.array(), result -> {
			for (BaseAdapter baseAdapter : adapterArray) {
				result.add(baseAdapter != null ? baseAdapter : new NullTypeAdapter());
			}
		});
	}
}
