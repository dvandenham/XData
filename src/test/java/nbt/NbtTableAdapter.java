package nbt;

import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.BaseTableAdapter;
import nl.appelgebakje22.xdata.adapter.TableAdapter;

class NbtTableAdapter extends BaseTableAdapter {

	public NbtTableAdapter(NbtAdapterFactory adapters) {
		super(adapters);
	}

	public static Tag toTag(TableAdapter adapter) {
		return XData.make(new CompoundTag(), nbt -> {
			for (String key : adapter.getKeys()) {
				nbt.put(key, NbtAdapterFactory.toTag(adapter.get(key)));
			}
		});
	}
}
