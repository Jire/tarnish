package com.osroyale.fs.cache;

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

/**
 * Represents a {@link Sector} and {@link Index} cache.
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class Cache {

	/**
	 * Represents the size of a index file.
	 * Calculating the total size of a index file. the total size may be that of a {@code long}.
	 */
	public static final int INDEX_SIZE = 6;

	/**
	 * Represents the size of a {@link Sector}s header.
	 * Calculating the total size of the sector header. the total size may be that of a {@code long}.
	 */
	public static final int SECTOR_HEADER_SIZE = 8;

	/**
	 * Represents the size of a {@link Sector}s header.
	 * Calculating the total size of the sector header. the total size may be that of a {@code long}
	 */
	public static final int SECTOR_SIZE = 520;

	/**
	 * A {@link ByteBuffer} allocated to {@link #SECTOR_SIZE}.
	 * <p>
	 * This byte buffer is used to read index and sector data from their
	 * respective byte channels.
	 * </p>
	 */
	private final ByteBuffer buffer = ByteBuffer.allocate(SECTOR_SIZE);

	/**
	 * A byte channel that contains a series of variable-length bytes which
	 * represent a sector.
	 */
	private final SeekableByteChannel sectorChannel;

	/**
	 * A byte channel that contains a series of variable-length bytes which
	 * represent a index.
	 */
	private final SeekableByteChannel indexChannel;

	/**
	 * Represents the id of this {@link Cache}.
	 */
	private final int id;

	/**
	 * Constructs a new {@link Cache} with the specified sector and index
	 * channels and id.
	 * @param sectorChannel The cache sectors byte channel.
	 * @param indexChannel  The cache sectors index channel.
	 * @param id            This caches id.
	 */
	protected Cache(SeekableByteChannel sectorChannel, SeekableByteChannel indexChannel, int id) {
		this.sectorChannel = sectorChannel;
		this.indexChannel = indexChannel;
		this.id = ++id;
	}

	/**
	 * Gets a {@link ByteBuffer} of data within this cache for the specified
	 * index id.
	 * @param indexId The file id to get.
	 * @return A wrapped byte buffer of the specified files data, never
	 * {@code null}.
	 * @throws IOException If some I/O exception occurs.
	 */
	public ByteBuffer get(final int indexId) throws IOException {
		final Index index = readIndex(indexId);
		if (!index.check()) {
			return null;
		}

		long position = (long) index.getId() * SECTOR_HEADER_SIZE;

		Preconditions.checkArgument(sectorChannel.size() >= position + SECTOR_SIZE);

		byte[] data = new byte[index.getLength()];
		int next = index.getId();
		int offset = 0;

		for (int chunk = 0; offset < index.getLength(); chunk++) {
			int read = Math.min(index.getLength() - offset, 512);

			Sector sector = readSector(next, data, offset, read);
			sector.check(id, indexId, chunk);

			next = sector.getNextIndexId();
			offset += read;
		}

		return ByteBuffer.wrap(data);
	}

	/**
	 * Reads an {@link Index} for the specified {@code indexId} and returns the
	 * decoded data.
	 * @param indexId The id of the index to read.
	 * @return The decoded index.
	 * @throws IOException If some I/O exception occurs.
	 */
	private Index readIndex(int indexId) throws IOException {
		long position = (long) indexId * INDEX_SIZE;

		buffer.clear().limit(INDEX_SIZE);
		indexChannel.position(position);
		indexChannel.read(buffer);
		buffer.flip();

		return Index.decode(buffer);
	}

	/**
	 * Reads a {@link Sector} for the specified {@code sectorId} and returns the
	 * decoded data.
	 * @param sectorId The id of the sector to read.
	 * @param data     The sectors data.
	 * @param offset   The sectors data offset.
	 * @param length   The length of the sectors data.
	 * @return The decoded sector.
	 * @throws IOException If some I/O exception occurs.
	 */
	private Sector readSector(int sectorId, byte[] data, int offset, int length) throws IOException {
		long position = (long) sectorId * SECTOR_SIZE;

		buffer.clear().limit(length + SECTOR_HEADER_SIZE);
		sectorChannel.position(position);
		sectorChannel.read(buffer);
		buffer.flip();

		return Sector.decode(buffer, data, offset, length);
	}

}