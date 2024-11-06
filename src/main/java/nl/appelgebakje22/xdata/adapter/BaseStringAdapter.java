package nl.appelgebakje22.xdata.adapter;

public class BaseStringAdapter implements StringAdapter {

	private String value;

	@Override
	public void setString(String value) {
		this.value = value;
	}

	@Override
	public String getString() {
		return this.value;
	}

	@Override
	public String getString(String fallback) {
		return this.value != null ? this.value : fallback;
	}
}
