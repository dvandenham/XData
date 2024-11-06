package nettest;

import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import nl.appelgebakje22.xdata.adapter.NetworkInput;

@RequiredArgsConstructor
public class ByteArrayNetworkInput implements NetworkInput {

	private final byte[] bytes;
	private int i = 0;

	@Override
	public boolean readBoolean() {
		return this.bytes[this.i++] == 1;
	}

	@Override
	public byte readByte() {
		return this.bytes[this.i++];
	}

	@Override
	public short readShort() {
		return (short) (((this.readByte() & 0xff) << 8)
				| (this.readByte() & 0xff));
	}

	@Override
	public int readInt() {
		return ((this.readByte() & 0xff) << 24)
				| ((this.readByte() & 0xff) << 16)
				| ((this.readByte() & 0xff) << 8)
				| (this.readByte() & 0xff);
	}

	@Override
	public long readLong() {
		return ((long) (this.readByte() & 0xff) << 56)
				| ((long) (this.readByte() & 0xff) << 48)
				| ((long) (this.readByte() & 0xff) << 40)
				| ((long) (this.readByte() & 0xff) << 32)
				| ((long) (this.readByte() & 0xff) << 24)
				| ((this.readByte() & 0xff) << 16)
				| ((this.readByte() & 0xff) << 8)
				| (this.readByte() & 0xff);
	}

	@Override
	public float readFloat() {
		return Float.intBitsToFloat(this.readInt());
	}

	@Override
	public double readDouble() {
		return Double.longBitsToDouble(this.readLong());
	}

	@Override
	public char readChar() {
		return (char) this.readInt();
	}

	@Override
	public String readString() {
		final byte[] bytes = new byte[this.readInt()];
		this.read(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}

	@Override
	public void read(final byte[] bytes, final int start, final int length) {
		System.arraycopy(this.bytes, this.i, bytes, start, length);
		this.i += length;
	}
}
