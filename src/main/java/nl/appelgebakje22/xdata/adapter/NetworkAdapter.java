package nl.appelgebakje22.xdata.adapter;

public interface NetworkAdapter {

	void write(boolean data);

	void write(byte data);

	void write(short data);

	void write(int data);

	void write(long data);

	void write(float data);

	void write(double data);

	void write(char data);

	void write(String data);

	boolean readBoolean();

	byte readByte();

	short readShort();

	int readInt();

	long readLong();

	float readFloat();

	double readDouble();

	char readChar();

	String readString();
}
