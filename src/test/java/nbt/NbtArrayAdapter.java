package nbt;

import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.ArrayAdapter;
import nl.appelgebakje22.xdata.adapter.BaseArrayAdapter;

class NbtArrayAdapter extends BaseArrayAdapter {

	public NbtArrayAdapter(NbtAdapterFactory adapters) {
		super(adapters);
	}

	public static Tag toTag(ArrayAdapter adapter) {
		return XData.make(new ListTag(CompoundTag.class), nbt -> {
			for (int i = 0; i < adapter.size(); ++i) {
				CompoundTag entry = new CompoundTag();
				entry.putInt("i", i);
				entry.put("dat", NbtAdapterFactory.toTag(adapter.get(i)));
				nbt.add(entry);
			}
		});
	}
}
