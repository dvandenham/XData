package nl.appelgebakje22.xdata.adapter;

public class BaseFloatAdapter extends BaseNumberAdapter<Float> implements FloatAdapter {

	private float value;

	public void setNumber(float number) {
		this.value = number;
	}

	@Override
	public Float getNumber() {
		return this.value;
	}
}
