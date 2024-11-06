package nl.appelgebakje22.xdata.adapter;

public class BaseCharAdapter implements CharAdapter {

	private char value;

	@Override
	public void setChar(char value) {
		this.value = value;
	}

	@Override
	public char getChar() {
		return this.value;
	}

	@Override
	public char getChar(char fallback) {
		return this.value != 0 ? this.value : fallback;
	}
}
