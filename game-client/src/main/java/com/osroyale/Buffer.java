package com.osroyale;

import java.math.BigInteger;

public final class Buffer extends Cacheable {


	public void setOffset(int offset) {
		this.position = offset;
	}

	public int readSmart() {
		int value = array[position] & 0xff;
		if (value < 128)
			return readUnsignedByte() - 64;
		else
			return readUShort() - 49152;
	}
	public static Buffer create() {
		synchronized (nodeList) {
			Buffer stream = null;
			if (anInt1412 > 0) {
				anInt1412--;
				stream = (Buffer) nodeList.popHead();
			}
			if (stream != null) {
				stream.position = 0;
				return stream;
			}
		}
		Buffer stream_1 = new Buffer();
		stream_1.position = 0;
		stream_1.array = new byte[40_000];
		return stream_1;
	}

	public String readStringNew() {
		int i = position;
		while (array[position++] != 0)
			;
		return new String(array, i, position - i - 1);
	}

	public int readShort2() {
		position += 2;
		int i = ((array[position - 2] & 0xff) << 8) + (array[position - 1] & 0xff);
		if (i > 60000)
			i = -65535 + i;
		return i;

	}

	public int readSignedShort() {
		this.position += 2;
		int value = ((this.array[this.position - 2] & 0xff) << 8) + (this.array[this.position - 1] & 0xff);
		if (value > 32767)
			value -= 0x10000; //theres no errors its just gradle being slow as fck

		return value;
	}

	public int readShortSmart()
	{
		int peek = array[position] & 0xFF;
		return peek < 128 ? this.readUnsignedByte() - 64 : this.readUnsignedShort() - 0xc000;
	}

	public int read24Int() {
		position += 3;
		return ((array[position - 3] & 0xff) << 16) + ((array[position - 2] & 0xff) << 8) + (array[position - 1] & 0xff);
	}

	public int read24BitInt()
	{
		return (this.readUnsignedByte() << 16) + (this.readUnsignedByte() << 8) + this.readUnsignedByte();
	}

	public int readMedium() {
		this.position += 3;
		return ((this.array[this.position - 3] & 255) << 16) + (this.array[this.position - 1] & 255) + ((this.array[this.position - 2] & 255) << 8);
	}


	public int readUShort() {
		position += 2;
		return ((array[position - 2] & 0xff) << 8)
				+ (array[position - 1] & 0xff);
	}

	public int readInt() {
		position += 4;
		return ((array[position - 4] & 0xff) << 24)
				+ ((array[position - 3] & 0xff) << 16)
				+ ((array[position - 2] & 0xff) << 8)
				+ (array[position - 1] & 0xff);
	}

	public int readShort() {
		position += 2;
		int value = ((array[position - 2] & 0xff) << 8)
				+ (array[position - 1] & 0xff);

		if (value > 32767) {
			value -= 0x10000;
		}
		return value;
	}

	public int readShortOSRS() {
		this.position += 2;
		int var1 = (this.array[this.position - 1] & 255) + ((this.array[this.position - 2] & 255) << 8);
		if (var1 > 32767) {
			var1 -= 65536;
		}

		return var1;
	}

	final int v(int i) {
		position += 3;
		return (0xff & array[position - 3] << 16) + (0xff & array[position - 2] << 8) + (0xff & array[position - 1]);
	}

	private Buffer() {
	}

	public Buffer(byte[] array) {
		this.array = array;
		position = 0;
	}

	public int readUnsignedShortSmart()
	{
		int peek = this.peek() & 0xFF;
		return peek < 128 ? this.readUnsignedByte() : this.readUnsignedShort() - 0x8000;
	}

	public int readUnsignedIntSmartShortCompat()
	{
		int var1 = 0;

		int var2;
		for (var2 = this.readUnsignedShortSmart(); var2 == 32767; var2 = this.readUnsignedShortSmart())
		{
			var1 += 32767;
		}

		var1 += var2;
		return var1;
	}

	public int readUSmart2() {
		int baseVal = 0;
		int lastVal = 0;
		while ((lastVal = method422()) == 32767) {
			baseVal += 32767;
		}
		return baseVal + lastVal;
	}

	public String readNewString() {
		int i = position;
		while (array[position++] != 0)
			;
		return new String(array, i, position - i - 1);
	}

	public void writeOpcode(int i) {
		// System.out.println("Frame: " + i);
		array[position++] = (byte) (i + encryption.getNextKey());
	}

	public void writeByte(int i) {
		array[position++] = (byte) i;
	}

	public void writeShort(int i) {
		array[position++] = (byte) (i >> 8);
		array[position++] = (byte) i;
	}

	public void writeDWordBigEndian(int i) {
		array[position++] = (byte) (i >> 16);
		array[position++] = (byte) (i >> 8);
		array[position++] = (byte) i;
	}

	public void writeDWord(int i) {
		array[position++] = (byte) (i >> 24);
		array[position++] = (byte) (i >> 16);
		array[position++] = (byte) (i >> 8);
		array[position++] = (byte) i;
	}

	public void method403(int j) {
		array[position++] = (byte) j;
		array[position++] = (byte) (j >> 8);
		array[position++] = (byte) (j >> 16);
		array[position++] = (byte) (j >> 24);
	}

	public void writeQWord(long l) {
		try {
			array[position++] = (byte) (int) (l >> 56);
			array[position++] = (byte) (int) (l >> 48);
			array[position++] = (byte) (int) (l >> 40);
			array[position++] = (byte) (int) (l >> 32);
			array[position++] = (byte) (int) (l >> 24);
			array[position++] = (byte) (int) (l >> 16);
			array[position++] = (byte) (int) (l >> 8);
			array[position++] = (byte) (int) l;
		} catch (RuntimeException runtimeexception) {
			Utility.reporterror("14395, " + 5 + ", " + l + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public void writeString(String s) {
		// s.getBytes(0, s.length(), buffer, currentOffset); //deprecated
		System.arraycopy(s.getBytes(), 0, array, position, s.length());
		position += s.length();
		array[position++] = 10;
	}

	public void writeBytes(byte[] abyte0, int i, int j) {
		for (int k = j; k < j + i; k++)
			array[position++] = abyte0[k];
	}

	public void writeBytes(int i) {
		array[position - i - 1] = (byte) i;
	}

	public int readUnsignedByte() {
		return array[position++] & 0xff;
	}

	public int readSignedSmart() {
		int value = peek() & 0xff;
		if (value < 128)
			return this.readUnsignedByte() - 64;
		else
			return this.readUnsignedShort() - 49152;
	}

	public byte peek()
	{
		return array[position];
	}

	public int readUnsignedShortSmartMinusOne()
	{
		int peek = this.peek() & 0xFF;
		return peek < 128 ? this.readUnsignedByte() - 1 : this.readUnsignedShort() - 0x8001;
	}

	public int readBigSmart2()
	{
		if (peek() < 0)
		{
			return readInt() & Integer.MAX_VALUE; // and off sign bit
		}
		int value = readUnsignedShort();
		return value == 32767 ? -1 : value;
	}

	public byte readSignedByte() {
		return array[position++];
	}

	public int readUnsignedShort() {
		position += 2;
		return ((array[position - 2] & 0xff) << 8) + (array[position - 1] & 0xff);
	}

	public int readSignedWord() {
		position += 2;
		int i = ((array[position - 2] & 0xff) << 8) + (array[position - 1] & 0xff);
		if (i > 32767) {
			i -= 0x10000;
		}
		return i;
	}

	public int getUIncrementalSmart() {
		int value = 0, remainder;
		for (remainder = method422(); remainder == 32767; remainder = method422()) {
			value += 32767;
		}
		value += remainder;
		return value;
	}

	public int readUnsignedTriByte() {
		position += 3;
		return ((array[position - 3] & 0xff) << 16) + ((array[position - 2] & 0xff) << 8) + (array[position - 1] & 0xff);
	}

	public int readUnsignedInt() {
		position += 4;
		return ((array[position - 4] & 0xff) << 24) + ((array[position - 3] & 0xff) << 16) + ((array[position - 2] & 0xff) << 8) + (array[position - 1] & 0xff);
	}

	public long readUnsignedLong() {
		long big = (long) readUnsignedInt() & 0xFFFF_FFFFL;
		long little = (long) readUnsignedInt() & 0xFFFF_FFFFL;
		return (big << 32) + little;
	}

	public String readString() {
		int start = position;
		while (array[position++] != 10);
		return new String(array, start, position - start - 1);
	}

	public String readStringCp1252NullTerminated() {
		int var1 = this.position;

		while(this.array[++this.position - 1] != 0) {
			;
		}

		int var2 = this.position - var1 - 1;
		return var2 == 0 ? "" : decodeStringCp1252(this.array, var1, var2);
	}

	public static String decodeStringCp1252(byte[] var0, int var1, int var2) {
		char[] var3 = new char[var2];
		int var4 = 0;

		for(int var5 = 0; var5 < var2; ++var5) {
			int var6 = var0[var5 + var1] & 255;
			if (var6 != 0) {
				if (var6 >= 128 && var6 < 160) {
					char var7 = cp1252AsciiExtension[var6 - 128];
					if (var7 == 0) {
						var7 = '?';
					}

					var6 = var7;
				}

				var3[var4++] = (char)var6;
			}
		}

		return new String(var3, 0, var4);
	}

	public static final char[] cp1252AsciiExtension = new char[]{'€', '\u0000', '‚', 'ƒ', '„', '…', '†', '‡', 'ˆ', '‰', 'Š', '‹', 'Œ', '\u0000', 'Ž', '\u0000', '\u0000', '‘', '’', '“', '”', '•', '–', '—', '˜', '™', 'š', '›', 'œ', '\u0000', 'ž', 'Ÿ'};

	public byte[] readBytes() {
		int start = position;
		while (array[position++] != 10);
		byte[] bytes = new byte[position - start - 1];
		System.arraycopy(array, start, bytes, start - start, position - 1 - start);
		return bytes;
	}

	public void readBytes(int amount, int offset, byte[] destination) {
		for (int l = offset; l < offset + amount; l++)
			destination[l] = array[position++];
	}

	public void initBitAccess() {
		bitPosition = position * 8;
	}

	public int readBits(int amount) {
		int bytePos = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		int value = 0;

		bitPosition += amount;

		for (; amount > bitOffset; bitOffset = 8) {
			value += (array[bytePos++] & BIT_MASK[bitOffset]) << amount - bitOffset;
			amount -= bitOffset;
		}

		if (amount == bitOffset) {
			value += array[bytePos] & BIT_MASK[bitOffset];
		} else {
			value += array[bytePos] >> bitOffset - amount & BIT_MASK[amount];
		}

		return value;
	}

	public void finishBitAccess() {
		position = (bitPosition + 7) / 8;
	}

	public int method421() {
		int i = array[position] & 0xff;
		if (i < 128)
			return readUnsignedByte() - 64;
		else
			return readUnsignedShort() - 49152;
	}

	public int method422() {
		int i = array[position] & 0xff;
		if (i < 128)
			return readUnsignedByte();
		else
			return readUnsignedShort() - 32768;
	}

	public void encodeRSA(BigInteger exponent, BigInteger modulus) {
		int i = position;
		position = 0;
		byte[] abyte0 = new byte[i];
		readBytes(i, 0, abyte0);

		byte[] rsa = abyte0;

		if (Configuration.ENABLE_RSA) {
			rsa = new BigInteger(abyte0).modPow(exponent, modulus).toByteArray();
		}
		position = 0;
		writeByte(rsa.length);
		writeBytes(rsa, rsa.length, 0);
	}

	public void writeNegatedByte(int i) {
		array[position++] = (byte) (-i);
	}

	public void method425(int j) {
		array[position++] = (byte) (128 - j);
	}

	public int readUByteA() {
		return array[position++] - 128 & 0xff;
	}

	public int readNegUByte() {
		return -array[position++] & 0xff;
	}

	public int readUByteS() {
		return 128 - array[position++] & 0xff;
	}

	public byte method429() {
		return (byte) (-array[position++]);
	}

	public byte method430() {
		return (byte) (128 - array[position++]);
	}

	public void writeLEShort(int i) {
		array[position++] = (byte) i;
		array[position++] = (byte) (i >> 8);
	}

	public void writeShortA(int j) {
		array[position++] = (byte) (j >> 8);
		array[position++] = (byte) (j + 128);
	}

	public void writeLEShortA(int j) {
		array[position++] = (byte) (j + 128);
		array[position++] = (byte) (j >> 8);
	}

	public int readLEUShort() {
		position += 2;
		return ((array[position - 1] & 0xff) << 8) + (array[position - 2] & 0xff);
	}

	public int readUShortA() {
		position += 2;
		return ((array[position - 2] & 0xff) << 8) + (array[position - 1] - 128 & 0xff);
	}

	public int readLEUShortA() {
		position += 2;
		return ((array[position - 1] & 0xff) << 8) + (array[position - 2] - 128 & 0xff);
	}

	public int readLEShort() {
		position += 2;
		int j = ((array[position - 1] & 0xff) << 8) + (array[position - 2] & 0xff);
		if (j > 32767)
			j -= 0x10000;
		return j;
	}

	public int method438() {
		position += 2;
		int j = ((array[position - 1] & 0xff) << 8) + (array[position - 2] - 128 & 0xff);
		if (j > 32767)
			j -= 0x10000;
		return j;
	}

	public int method439() {
		position += 4;
		return ((array[position - 2] & 0xff) << 24) + ((array[position - 1] & 0xff) << 16) + ((array[position - 4] & 0xff) << 8) + (array[position - 3] & 0xff);
	}

	public int method440() {
		position += 4;
		return ((array[position - 3] & 0xff) << 24) + ((array[position - 4] & 0xff) << 16) + ((array[position - 1] & 0xff) << 8) + (array[position - 2] & 0xff);
	}

	public void method441(int i, byte[] abyte0, int j) {
		for (int k = (i + j) - 1; k >= i; k--)
			array[position++] = (byte) (abyte0[k] + 128);

	}

	public void readReverseData(int i, int j, byte[] abyte0) {
		for (int k = (j + i) - 1; k >= j; k--)
			abyte0[k] = array[position++];

	}

	public float readFloat() {
		return Float.intBitsToFloat(readInt());
	}

	public byte[] readBytes(final int length) {
		final byte[] bytes = new byte[length];
		System.arraycopy(array, position, bytes, 0, length);
		position += length;
		return bytes;
	}

	public byte[] array;
	public int position;
	public int bitPosition;
	private static final int[] BIT_MASK = {0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 0x1ffff, 0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff, -1};
	public ISAACRandomGen encryption;
	private static int anInt1412;
	private static final Deque nodeList = new Deque();

	public void setPosition(int offset) {
		this.position = offset;
	}
}