package nbt;

import nl.appelgebakje22.xdata.adapter.BaseStringAdapter;
import nl.appelgebakje22.xdata.adapter.CharAdapter;

class NbtStringAdapter extends BaseStringAdapter implements CharAdapter {

	@Override
	public void setChar(char value) {
		setString(String.valueOf(value));
	}

	@Override
	public char getChar() {
		String s = getString();
		return !s.isEmpty() ? s.charAt(0) : 0;
	}

	@Override
	public char getChar(char fallback) {
		char result = getChar();
		return result != 0 ? result : fallback;
	}
}
