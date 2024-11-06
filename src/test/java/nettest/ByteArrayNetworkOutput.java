package nettest;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import nl.appelgebakje22.xdata.adapter.NetworkOutput;

public class ByteArrayNetworkOutput implements NetworkOutput {

	private final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

	@Override
	public void write(final boolean data) {
		this.byteOut.write(data ? 1 : 0);
	}

	@Override
	public void write(final byte data) {
		this.byteOut.write(data);
	}

	@Override
	public void write(final short data) {
		this.write((byte) ((data >> 8) & 0xff));
		this.write((byte) (data & 0xff));
	}

	@Override
	public void write(final int data) {
		this.write((byte) ((data >> 24) & 0xff));
		this.write((byte) ((data >> 16) & 0xff));
		this.write((byte) ((data >> 8) & 0xff));
		this.write((byte) (data & 0xff));
	}

	@Override
	public void write(final long data) {
		this.write((byte) ((data >> 56) & 0xff));
		this.write((byte) ((data >> 48) & 0xff));
		this.write((byte) ((data >> 40) & 0xff));
		this.write((byte) ((data >> 32) & 0xff));
		this.write((byte) ((data >> 24) & 0xff));
		this.write((byte) ((data >> 16) & 0xff));
		this.write((byte) ((data >> 8) & 0xff));
		this.write((byte) (data & 0xff));
	}

	@Override
	public void write(final float data) {
		this.write(Float.floatToIntBits(data));
	}

	@Override
	public void write(final double data) {
		this.write(Double.doubleToLongBits(data));
	}

	@Override
	public void write(final char data) {
		this.write((int) data);
	}

	@Override
	public void write(final String data) {
		final byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
		this.write(bytes.length);
		this.write(bytes);
	}

	@Override
	public void write(final byte[] bytes, final int start, final int length) {
		this.byteOut.write(bytes, start, length);
	}

	public byte[] toByteArray() {
		return this.byteOut.toByteArray();
	}
}
