package com.osroyale;

import org.jire.swiftfup.client.FileIndex;

import java.io.IOException;
import java.io.RandomAccessFile;

public final class RSFileStore implements FileIndex {

	public static final int CACHE_INDEX_COUNT = 6;

	public RSFileStore(RandomAccessFile dat, RandomAccessFile idx, int fileId) {
		indexFileId = fileId;
		dataFile = dat;
		indexFile = idx;
	}

	public synchronized byte[] readFile(int i) {
		try {
			seekTo(indexFile, i * 6);
			int l;
			for (int j = 0; j < 6; j += l) {
				l = indexFile.read(buffer, j, 6 - j);
				if (l == -1)
					return null;
			}
			int i1 = ((buffer[0] & 0xff) << 16) + ((buffer[1] & 0xff) << 8) + (buffer[2] & 0xff);
			int j1 = ((buffer[3] & 0xff) << 16) + ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);
			//if(i1 < 0 || i1 > 0xffffff)
			//	return null;
			if (j1 <= 0 || (long) j1 > dataFile.length() / 520L)
				return null;
			byte[] abyte0 = new byte[i1];
			int k1 = 0;
			for (int l1 = 0; k1 < i1; l1++) {
				if (j1 == 0)
					return null;
				seekTo(dataFile, j1 * 520);
				int k = 0;
				int i2 = i1 - k1;
				if (i2 > 512)
					i2 = 512;
				int j2;
				for (; k < i2 + 8; k += j2) {
					j2 = dataFile.read(buffer, k, (i2 + 8) - k);
					if (j2 == -1)
						return null;
				}
				int k2 = ((buffer[0] & 0xff) << 8) + (buffer[1] & 0xff);
				int l2 = ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);
				int i3 = ((buffer[4] & 0xff) << 16) + ((buffer[5] & 0xff) << 8) + (buffer[6] & 0xff);
				int j3 = buffer[7] & 0xff;
				if (k2 != i || l2 != l1 || j3 != indexFileId)
					return null;
				if (i3 < 0 || (long) i3 > dataFile.length() / 520L)
					return null;
				for (int k3 = 0; k3 < i2; k3++)
					abyte0[k1++] = buffer[k3 + 8];

				j1 = i3;
			}

			return abyte0;
		} catch (IOException _ex) {
			return null;
		}
	}

	public synchronized boolean writeFile(int length, byte[] data, int fileID) {
		boolean flag = writeFile(true, fileID, length, data);
		if (!flag)
			flag = writeFile(false, fileID, length, data);
		return flag;
	}

	private synchronized boolean writeFile(boolean exists, int fileID, int length, byte[] data) {
		try {
			int l;
			if (exists) {
				seekTo(indexFile, fileID * 6);
				int k1;
				for (int i1 = 0; i1 < 6; i1 += k1) {
					k1 = indexFile.read(buffer, i1, 6 - i1);
					if (k1 == -1)
						return false;
				}
				l = ((buffer[3] & 0xff) << 16) + ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);
				if (l <= 0 || (long) l > dataFile.length() / 520L)
					return false;
			} else {
				l = (int) ((dataFile.length() + 519L) / 520L);
				if (l == 0)
					l = 1;
			}
			buffer[0] = (byte) (length >> 16);
			buffer[1] = (byte) (length >> 8);
			buffer[2] = (byte) length;
			buffer[3] = (byte) (l >> 16);
			buffer[4] = (byte) (l >> 8);
			buffer[5] = (byte) l;
			seekTo(indexFile, fileID * 6);
			indexFile.write(buffer, 0, 6);
			int j1 = 0;
			for (int l1 = 0; j1 < length; l1++) {
				int i2 = 0;
				if (exists) {
					seekTo(dataFile, l * 520);
					int j2;
					int l2;
					for (j2 = 0; j2 < 8; j2 += l2) {
						l2 = dataFile.read(buffer, j2, 8 - j2);
						if (l2 == -1)
							break;
					}
					if (j2 == 8) {
						int i3 = ((buffer[0] & 0xff) << 8) + (buffer[1] & 0xff);
						int j3 = ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);
						i2 = ((buffer[4] & 0xff) << 16) + ((buffer[5] & 0xff) << 8) + (buffer[6] & 0xff);
						int k3 = buffer[7] & 0xff;
						if (i3 != fileID || j3 != l1 || k3 != indexFileId)
							return false;
						if (i2 < 0 || (long) i2 > dataFile.length() / 520L)
							return false;
					}
				}
				if (i2 == 0) {
					exists = false;
					i2 = (int) ((dataFile.length() + 519L) / 520L);
					if (i2 == 0)
						i2++;
					if (i2 == l)
						i2++;
				}
				if (length - j1 <= 512)
					i2 = 0;
				buffer[0] = (byte) (fileID >> 8);
				buffer[1] = (byte) fileID;
				buffer[2] = (byte) (l1 >> 8);
				buffer[3] = (byte) l1;
				buffer[4] = (byte) (i2 >> 16);
				buffer[5] = (byte) (i2 >> 8);
				buffer[6] = (byte) i2;
				buffer[7] = (byte) indexFileId;
				seekTo(dataFile, l * 520);
				dataFile.write(buffer, 0, 8);
				int k2 = length - j1;
				if (k2 > 512)
					k2 = 512;
				dataFile.write(data, j1, k2);
				j1 += k2;
				l = i2;
			}

			return true;
		} catch (IOException _ex) {
			return false;
		}
	}

	private synchronized void seekTo(RandomAccessFile randomaccessfile, int j) throws IOException {
		try {
			randomaccessfile.seek(j);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final byte[] buffer = new byte[520];
	private final RandomAccessFile dataFile;
	private final RandomAccessFile indexFile;
	private final int indexFileId;

	@Override
	public byte[] getFile(int fileID) {
		return readFile(fileID);
	}

	@Override
	public void writeFile(int fileID, byte[] data) {
		writeFile(data.length, data, fileID);
	}

}
