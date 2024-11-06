package nl.appelgebakje22.xdata.adapter;

public class BaseBooleanAdapter implements BooleanAdapter {

	private boolean value;

	@Override
	public void setBoolean(boolean value) {
		this.value = value;
	}

	@Override
	public boolean getBoolean() {
		return this.value;
	}

	@Override
	public boolean getBoolean(boolean fallback) {
		return this.value || fallback;
	}
}
