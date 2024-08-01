package com.osroyale.net.packet;

import com.osroyale.net.codec.AccessType;
import com.osroyale.net.codec.ByteModification;
import com.osroyale.net.codec.ByteOrder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.buffer.PooledByteBufAllocator;

import static com.google.common.base.Preconditions.checkState;

/**
 * The implementation that functions as a dynamic buffer wrapper
 * backed by a {@code ByteBuf} that is used for reading and writing data.
 *
 * @author lare96 <http://github.com/lare96>
 * @author blakeman8192
 */
public final class PacketBuilder extends DefaultByteBufHolder {

	private static final int[] BIT_MASK = new int[32];

	static {
		for (int i = 0; i < BIT_MASK.length; i++) {
			BIT_MASK[i] = (1 << i) - 1;
		}
	}

	private static final int DEFAULT_CAPACITY = 128;
	private int bitPosition;

	private PacketBuilder(ByteBuf buffer) {
		super(buffer);
	}

	public static PacketBuilder alloc(int initialCapacity, int maxCapacity) {
		return new PacketBuilder(PooledByteBufAllocator.DEFAULT.buffer(initialCapacity, maxCapacity));
	}

	public static PacketBuilder alloc(int capacity) {
		return new PacketBuilder(PooledByteBufAllocator.DEFAULT.buffer(capacity));
	}

	public static PacketBuilder alloc() {
		return alloc(DEFAULT_CAPACITY);
	}
	public static PacketBuilder wrap(ByteBuf buf) {
		return new PacketBuilder(buf);
	}

	public PacketBuilder initializeAccess(AccessType type) {
		switch (type) {
		case BIT:
			bitPosition = content().writerIndex() * 8;
			break;
		case BYTE:
			content().writerIndex((bitPosition + 7) / 8);
			bitPosition = -1;
			break;
		}
		return this;
	}

	public PacketBuilder writeByte(int value) {
		writeByte(value, ByteModification.NONE);
		return this;
	}

	public PacketBuilder writeByte(int value, ByteModification type) {
		switch (type) {
		case ADD:
			value += 128;
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
		content().writeByte((byte) value);
		return this;
	}

	public PacketBuilder writeBit(boolean flag) {
		writeBits(1, flag ? 1 : 0);
		return this;
	}

	public PacketBuilder writeBits(int amount, int value) {
		checkState(amount >= 1 && amount <= 32, "Number of bits must be between 1 and 32 inclusive.");

		int bytePos = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		bitPosition = bitPosition + amount;
		int requiredSpace = bytePos - content().writerIndex() + 1;
		requiredSpace += (amount + 7) / 8;
		if (content().writableBytes() < requiredSpace) {
			content().capacity(content().capacity() + requiredSpace);
		}
		for (; amount > bitOffset; bitOffset = 8) {
			byte tmp = content().getByte(bytePos);
			tmp &= ~BIT_MASK[bitOffset];
			tmp |= (value >> (amount - bitOffset)) & BIT_MASK[bitOffset];
			content().setByte(bytePos++, tmp);
			amount -= bitOffset;
		}
		if (amount == bitOffset) {
			byte tmp = content().getByte(bytePos);
			tmp &= ~BIT_MASK[bitOffset];
			tmp |= value & BIT_MASK[bitOffset];
			content().setByte(bytePos, tmp);
		} else {
			byte tmp = content().getByte(bytePos);
			tmp &= ~(BIT_MASK[amount] << (bitOffset - amount));
			tmp |= (value & BIT_MASK[amount]) << (bitOffset - amount);
			content().setByte(bytePos, tmp);
		}
		return this;
	}

	public PacketBuilder writeBytes(byte[] from, int size) {
		content().writeBytes(from, 0, size);
		return this;
	}

	public PacketBuilder writeBytes(ByteBuf from) {
		for (int i = 0; i < from.writerIndex(); i++) {
			writeByte(from.getByte(i));
		}
		return this;
	}

	public PacketBuilder writeBytesReverse(byte[] data) {
		for (int i = data.length - 1; i >= 0; i--) {
			writeByte(data[i]);
		}
		return this;
	}

	public PacketBuilder writeInt(int value) {
		writeInt(value, ByteModification.NONE, ByteOrder.BE);
		return this;
	}

	public PacketBuilder writeInt(int value, ByteOrder order) {
		writeInt(value, ByteModification.NONE, order);
		return this;
	}

	public PacketBuilder writeInt(int value, ByteModification type) {
		writeInt(value, type, ByteOrder.BE);
		return this;
	}

	public PacketBuilder writeInt(int value, ByteModification type, ByteOrder order) {
		switch (order) {
		case BE:
			writeByte(value >> 24);
			writeByte(value >> 16);
			writeByte(value >> 8);
			writeByte(value, type);
			break;
		case ME:
			writeByte(value >> 8);
			writeByte(value, type);
			writeByte(value >> 24);
			writeByte(value >> 16);
			break;
		case IME:
			writeByte(value >> 16);
			writeByte(value >> 24);
			writeByte(value, type);
			writeByte(value >> 8);
			break;
		case LE:
			writeByte(value, type);
			writeByte(value >> 8);
			writeByte(value >> 16);
			writeByte(value >> 24);
			break;
		}
		return this;
	}

	public PacketBuilder writeLong(long value) {
		writeLong(value, ByteModification.NONE, ByteOrder.BE);
		return this;
	}

	public PacketBuilder writeLong(long value, ByteOrder order) {
		writeLong(value, ByteModification.NONE, order);
		return this;
	}

	public PacketBuilder writeLong(long value, ByteModification type) {
		writeLong(value, type, ByteOrder.BE);
		return this;
	}

	public PacketBuilder writeLong(long value, ByteModification type, ByteOrder order) {
		switch (order) {
		case BE:
			writeByte((int) (value >> 56));
			writeByte((int) (value >> 48));
			writeByte((int) (value >> 40));
			writeByte((int) (value >> 32));
			writeByte((int) (value >> 24));
			writeByte((int) (value >> 16));
			writeByte((int) (value >> 8));
			writeByte((int) value, type);
			break;

		case ME:
			throw new UnsupportedOperationException("Middle-endian long " + "is not implemented!");

		case IME:
			throw new UnsupportedOperationException("Inverse-middle-endian long is not implemented!");

		case LE:
			writeByte((int) value, type);
			writeByte((int) (value >> 8));
			writeByte((int) (value >> 16));
			writeByte((int) (value >> 24));
			writeByte((int) (value >> 32));
			writeByte((int) (value >> 40));
			writeByte((int) (value >> 48));
			writeByte((int) (value >> 56));
			break;

		}
		return this;
	}

	public PacketBuilder writeShort(int value) {
		writeShort(value, ByteModification.NONE, ByteOrder.BE);
		return this;
	}

	public PacketBuilder writeShort(int value, ByteOrder order) {
		writeShort(value, ByteModification.NONE, order);
		return this;
	}

	public PacketBuilder writeShort(int value, ByteModification type) {
		writeShort(value, type, ByteOrder.BE);
		return this;
	}

	public PacketBuilder writeShort(int value, ByteModification type, ByteOrder order) {
		switch (order) {
		case BE:
			writeByte(value >> 8);
			writeByte(value, type);
			break;
		case ME:
			throw new IllegalArgumentException("Middle-endian short is " + "impossible!");
		case IME:
			throw new IllegalArgumentException("Inverse-middle-endian " + "short is impossible!");
		case LE:
			writeByte(value, type);
			writeByte(value >> 8);
			break;
		default:
			break;
		}
		return this;
	}

	public PacketBuilder writeString(String string) {
		for (final byte value : string.getBytes()) {
			writeByte(value);
		}
		writeByte(10);
		return this;
	}

	public GamePacket toPacket(int opcode) {
		return new GamePacket(opcode, PacketType.FIXED, content());
	}

	public GamePacket toPacket(int opcode, PacketType type) {
		return new GamePacket(opcode, type, content());
	}

	public String getString() {
		byte temp;
		StringBuilder builder = new StringBuilder();
		while (content().isReadable() && (temp = content().readByte()) != 10) {
			builder.append((char) temp);
		}
		return builder.toString();
	}
	
	public PacketBuilder writeBuffer(ByteBuf buffer) {
		this.content().writeBytes(buffer);
		return this;
	}

	public PacketBuilder writeByteArray(byte[] bytes) {
		content().writeBytes(bytes);
		return this;
	}

	public PacketBuilder writeByteArray(byte[] bytes, int offset, int length) {
		content().writeBytes(bytes, offset, length);
		return this;
	}
}
