package com.osroyale.net.packet;

import com.osroyale.net.codec.ByteModification;
import com.osroyale.net.codec.ByteOrder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;

/**
 * Represents a single game packet.
 * 
 * @author nshusa
 */
public class GamePacket extends DefaultByteBufHolder {

	/**
	 * The opcode for this packet.
	 */
	private final int opcode;

	/**
	 * The header for this packet.
	 */
	private final PacketType header;

	/**
	 * The buffer that contains the data for this packet.
	 */
	private final ByteBuf payload;

	/**
	 * The size of this packet.
	 */
	private final int size;

	/**
	 * Creates a {@code GamePacket}.
	 * 
	 * @param opcode
	 *            The opcode.
	 * @param header
	 *            The header.
	 * @param payload
	 *            The payload.
	 */
	public GamePacket(final int opcode, final PacketType header, final ByteBuf payload) {
		super(payload);

		this.opcode = opcode;
		this.header = header;		
		this.payload = payload;
		this.size = payload.readableBytes();
	}

	/**
	 * Gets the size of this payload.
	 * 
	 * @return The amount of readable bytes.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Gets the opcode.
	 * 
	 * @return The opcode.
	 */
	public int getOpcode() {
		return opcode;
	}
	
	/**
	 * Gets the type.
	 * 
	 * @return The type.
	 */
	public PacketType getHeader() {
		return header;		
	}
	
	/**
	 * Gets the payload.
	 * 
	 * @return The payload.
	 */
	public ByteBuf getPayload() {
		return payload;
	}
	
	/**
	 * Reads a {@code STANDARD} {@code signed} byte from the payload.
	 *
	 * @return The byte.
	 */
	public int readByte() {
		return payload.readByte();
	}

	/**
	 * Reads a {@code STANDARD} byte from the payload.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @return The byte.
	 */
	public int readByte(boolean signed) {
		return readByte(signed, ByteModification.NONE);
	}

	/**
	 * Reads a {@code signed} byte from the payload.
	 * 
	 * @param mod
	 *            The modification performed on this value.
	 * 
	 * @return The signed byte.
	 */
	public int readByte(ByteModification mod) {
		return readByte(true, mod);
	}

	/**
	 * Reads a single byte from the payload.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @param mod
	 *            The modification performed on this value.
	 * 
	 * @return The byte.
	 */
	public int readByte(boolean signed, ByteModification mod) {
		int value = payload.readByte();
		switch (mod) {
		case ADD:
			value = value - 128;
			break;

		case SUB:
			value = 128 - value;
			break;

		case NEG:
			value = -value;
			break;

		case NONE:
			break;

		}
		return signed ? value : value & 0xFF;
	}

	/**
	 * Reads a {@code STANDARD} {@code signed} short value from the payload in
	 * {@code BIG} order.
	 * 
	 * @return The standard signed short value.
	 */
	public int readShort() {
		return payload.readShort();
	}

	/**
	 * Reads a {@code STANDARD} short value from the payload in {@code BIG}
	 * order.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @return The standard short value.
	 */
	public int readShort(boolean signed) {
		return readShort(signed, ByteOrder.BE, ByteModification.NONE);
	}

	/**
	 * Reads a {@code STANDARD} {@code signed} short value from the payload.
	 * 
	 * @param order
	 *            The order in which this value is written.
	 * 
	 * @return The signed standard short value.
	 */
	public int readShort(ByteOrder order) {
		return readShort(true, order, ByteModification.NONE);
	}

	/**
	 * Reads a {@code STANDARD} short value from the payload.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @param order
	 *            The order in which this value is written.
	 * 
	 * @return The standard short value.
	 */
	public int readShort(boolean signed, ByteOrder order) {
		return readShort(signed, order, ByteModification.NONE);
	}

	/**
	 * Reads a {@code signed} short value from the payload.
	 * 
	 * @param order
	 *            The order in which this value is written.
	 * 
	 * @param mod
	 *            The modifications performed on this value.
	 * 
	 * @return The short value.
	 */
	public int readShort(ByteOrder order, ByteModification mod) {
		return readShort(true, order, mod);
	}

	/**
	 * Reads a {@code signed} short value from the payload {@code BIG} order.
	 * 
	 * @param mod
	 *            The modifications performed on this value.
	 * 
	 * @return The short value.
	 */
	public int readShort(ByteModification mod) {
		return readShort(true, ByteOrder.BE, mod);
	}

	/**
	 * Reads a short value from the payload in {@code BIG} order.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @param mod
	 *            The modifications performed on this value.
	 * 
	 * @return The short value.
	 */
	public int readShort(boolean signed, ByteModification mod) {
		return readShort(signed, ByteOrder.BE, mod);
	}

	/**
	 * Reads a short value from the payload.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @param order
	 *            The order in which the value is written.
	 * 
	 * @param mod
	 *            The modifications performed on this value.
	 * 
	 * @return The short value.
	 */
	public int readShort(boolean signed, ByteOrder order, ByteModification mod) {
		int value = 0;
		switch (order) {
		case BE:
			value |= readByte(false) << 8;
			value |= readByte(false, mod);
			break;

		case IME:
			throw new UnsupportedOperationException("Inverse-middle-endian short is impossible!");

		case ME:
			throw new UnsupportedOperationException("Middle-endian short " + "is impossible!");

		case LE:
			value |= readByte(false, mod);
			value |= readByte(false) << 8;
			break;
		}
		return signed ? value : value & 0xFFFF;
	}

	/**
	 * Reads a {@code STANDARD} {@code signed} integer value from the payload in
	 * {@code BIG} order.
	 *
	 * @return The integer value.
	 */
	public int readInt() {
		return readInt(true, ByteOrder.BE, ByteModification.NONE);
	}

	/**
	 * Reads a {@code STANDARD} integer value from the payload in {@code BIG}
	 * order.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @return The integer value.
	 */
	public int readInt(boolean signed) {
		return readInt(signed, ByteOrder.BE, ByteModification.NONE);
	}

	/**
	 * Reads a {@code signed} integer value from the payload in
	 * {@code ByteOrder} {@code BIG} order.
	 * 
	 * @param mod
	 *            The modifications performed on this value.
	 * 
	 * @return The integer value.
	 */
	public int readInt(ByteModification mod) {
		return readInt(true, ByteOrder.BE, mod);
	}

	/**
	 * Reads an integer value from the payload in {@code BIG} order.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @param mod
	 *            The modifications performed on this value.
	 * 
	 * @return The integer value.
	 */
	public int readInt(boolean signed, ByteModification mod) {
		return readInt(signed, ByteOrder.BE, mod);
	}

	/**
	 * Reads a {@code STANDARD} integer value from the payload.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @param order
	 *            The order in which the value is written.
	 * 
	 * @return The integer value.
	 */
	public int readInt(boolean signed, ByteOrder order) {
		return readInt(signed, order, ByteModification.NONE);
	}

	/**
	 * Reads an integer value from the payload.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @param order
	 *            The order in which the value is written.
	 * 
	 * @param mod
	 *            The modifications performed on this value.
	 * 
	 * @return The integer value.
	 */
	public int readInt(boolean signed, ByteOrder order, ByteModification mod) {
		long value = 0;
		switch (order) {
		case BE:
			value |= readByte(false) << 24;
			value |= readByte(false) << 16;
			value |= readByte(false) << 8;
			value |= readByte(false, mod);
			break;

		case ME:
			value |= readByte(false) << 8;
			value |= readByte(false, mod);
			value |= readByte(false) << 24;
			value |= readByte(false) << 16;
			break;
		case IME:
			value |= readByte(false) << 16;
			value |= readByte(false) << 24;
			value |= readByte(false, mod);
			value |= readByte(false) << 8;
			break;

		case LE:
			value |= readByte(false, mod);
			value |= readByte(false) << 8;
			value |= readByte(false) << 16;
			value |= readByte(false) << 24;
			break;

		}
		return (int) (signed ? value : value & 0xFFFFFFFFL);
	}

	/**
	 * Reads a {@code STANDARD} {@code signed} long value from the payload in
	 * {@code BIG} order.
	 * 
	 * @return The long value.
	 */
	public long readLong() {
		return readLong(true, ByteOrder.BE, ByteModification.NONE);
	}

	/**
	 * Reads a {@code STANDARD} long value from the payload in {@code BIG}
	 * order.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @return The long value.
	 */
	public long readLong(boolean signed) {
		return readLong(signed, ByteOrder.BE, ByteModification.NONE);
	}

	/**
	 * Reads a {@code signed} long value from the payload in {@code BIG} order.
	 * 
	 * @param mod
	 *            The modifications performed on this value.
	 * 
	 * @return The long value.
	 */
	public long readLong(ByteModification mod) {
		return readLong(true, ByteOrder.BE, mod);
	}

	/**
	 * Reads a long value from the payload in {@code BIG} order.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @param mod
	 *            The modifications performed on this value.
	 * 
	 * @return The long value.
	 */
	public long readLong(boolean signed, ByteModification mod) {
		return readLong(signed, ByteOrder.BE, mod);
	}

	/**
	 * Reads a {@code STANDARD} long value from the payload.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @param order
	 *            The order in which the value is written.
	 * 
	 * @return The long value.
	 */
	public long readLong(boolean signed, ByteOrder order) {
		return readLong(signed, order, ByteModification.NONE);
	}

	/**
	 * Reads a long value from the payload.
	 * 
	 * @param signed
	 *            The flag that denotes this value is signed.
	 * 
	 * @param order
	 *            The order in which the value is written.
	 * 
	 * @param mod
	 *            The modifications performed on this value.
	 * 
	 * @return The long value.
	 */
	public long readLong(boolean signed, ByteOrder order, ByteModification mod) {
		long value = 0;
		switch (order) {
		case BE:
			value |= (long) readByte(false) << 56L;
			value |= (long) readByte(false) << 48L;
			value |= (long) readByte(false) << 40L;
			value |= (long) readByte(false) << 32L;
			value |= (long) readByte(false) << 24L;
			value |= (long) readByte(false) << 16L;
			value |= (long) readByte(false) << 8L;
			value |= readByte(false, mod);
			break;

		case ME:
		case IME:
			throw new UnsupportedOperationException("Middle and " + "inverse-middle value types not supported!");

		case LE:
			value |= readByte(false, mod);
			value |= (long) readByte(false) << 8L;
			value |= (long) readByte(false) << 16L;
			value |= (long) readByte(false) << 24L;
			value |= (long) readByte(false) << 32L;
			value |= (long) readByte(false) << 40L;
			value |= (long) readByte(false) << 48L;
			value |= (long) readByte(false) << 56L;
			break;

		}
		return signed ? value : value & 0xFFFFFFFFL;
	}

	/**
	 * Reads the amount of bytes into the array, starting at the current
	 * position.
	 *
	 * @param amount
	 *            the amount to read.
	 * @return a buffer filled with the data.
	 */
	public byte[] readBytes(int amount) {
		return readBytes(amount, ByteModification.NONE);
	}

	/**
	 * Reads a series of bytes from a buffer.
	 * 
	 * @param amount
	 *            The amount of bytes to read.
	 * 
	 * @param mod
	 *            The modifications performed on the bytes values.
	 * 
	 * @return The bytes that where read.
	 */
	public byte[] readBytes(int amount, ByteModification mod) {
		byte[] data = new byte[amount];
		for (int i = 0; i < amount; i++) {
			data[i] = (byte) readByte(mod);
		}
		return data;
	}

	/**
	 * Reads a series of bytes in reverse.
	 * 
	 * @param amount
	 *            The amount of bytes to read.
	 * 
	 * @return The bytes in reverse.
	 */
	public byte[] readBytesReverse(int amount) {
		return readBytesReverse(amount, ByteModification.NONE);
	}

	/**
	 * Reads a series of bytes in reverse.
	 * 
	 * @param amount
	 *            The amount of bytes to read.
	 * 
	 * @param mod
	 *            The modification performed on these bytes.
	 * 
	 * @return The bytes in reverse.
	 */
	public byte[] readBytesReverse(int amount, ByteModification mod) {
		byte[] data = new byte[amount];

		int dataPosition = 0;

		for (int index = payload.readerIndex() + amount - 1; index >= payload.readerIndex(); index--) {
			int value = payload.getByte(index);

			switch (mod) {

			case ADD:
				value -= 128;
				break;

			case NEG:
				value = -value;
				break;

			case SUB:
				value = 128 - value;
				break;

			case NONE:
				break;

			}
			data[dataPosition++] = (byte) value;
		}
		return data;
	}

	/**
	 * Reads a RuneScape string.
	 * 
	 * @return The string.
	 */
	public String getRS2String() {
		final StringBuilder bldr = new StringBuilder();
		byte b;
		while (payload.isReadable() && (b = payload.readByte()) != 10) {
			bldr.append((char) b);
		}
		return bldr.toString();
	}

	@Override
	public String toString() {
		return String.format("[opcode=%d], [type=%s], [size= %d]", opcode, header.name(), size);
	}

}