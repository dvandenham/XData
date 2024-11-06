package nbt;

import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseTableAdapter;
import nl.appelgebakje22.xdata.adapter.TableAdapter;

class NbtTableAdapter extends BaseTableAdapter {

	public NbtTableAdapter(NbtAdapterFactory adapters) {
		super(adapters);
	}

	public static Tag toTag(TableAdapter adapter) {
		return XData.make(new CompoundTag(), nbt -> {
			for (String key : adapter.getKeys()) {
				Tag serializedTag = NbtAdapterFactory.toTag(adapter.get(key));
				if (serializedTag != null) {
					nbt.put(key, serializedTag);
				}
			}
		});
	}

	public static TableAdapter fromTag(AdapterFactory adapters, CompoundTag compoundTag) {
		TableAdapter result = adapters.table();
		compoundTag.keySet().forEach(key -> {
			result.set(key, NbtAdapterFactory.fromTag(adapters, compoundTag.get(key)));
		});
		return result;
	}
}
