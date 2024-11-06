package nbt;

import net.querz.nbt.tag.StringTag;
import net.querz.nbt.tag.Tag;
import nl.appelgebakje22.xdata.adapter.CharAdapter;

class NbtCharAdapter extends NbtObjectAdapter<Character> implements CharAdapter {

	public static Tag toTag(CharAdapter charAdapter) {
		return new StringTag(String.valueOf(charAdapter.get()));
	}
}
