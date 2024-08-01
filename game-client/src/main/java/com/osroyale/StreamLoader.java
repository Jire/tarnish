package com.osroyale;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class StreamLoader {

	public StreamLoader(byte abyte0[]) {
		Buffer stream = new Buffer(abyte0);
		int i = stream.readUnsignedTriByte();
		int j = stream.readUnsignedTriByte();
		if (j != i) {
			byte abyte1[] = new byte[i];
			BZip2Decompressor.method225(abyte1, i, abyte0, j, 6);
			aByteArray726 = abyte1;
			stream = new Buffer(aByteArray726);
			aBoolean732 = true;
		} else {
			aByteArray726 = abyte0;
			aBoolean732 = false;
		}
		dataSize = stream.readUnsignedShort();
		anIntArray728 = new int[dataSize];
		anIntArray729 = new int[dataSize];
		anIntArray730 = new int[dataSize];
		anIntArray731 = new int[dataSize];
		int k = stream.position + dataSize * 10;
		for (int l = 0; l < dataSize; l++) {
			anIntArray728[l] = stream.readUnsignedInt();
			anIntArray729[l] = stream.readUnsignedTriByte();
			anIntArray730[l] = stream.readUnsignedTriByte();
			anIntArray731[l] = k;
			k += anIntArray730[l];
		}
	}

	public static int getFileID(String s) {
		int i = 0;
		s = s.toUpperCase();
		for (int j = 0; j < s.length(); j++)
			i = (i * 61 + s.charAt(j)) - 32;
		return i;
	}

	public byte[] getFile(String s) {
		byte abyte0[] = null; // was a parameter
		int fileID = getFileID(s);

		for (int k = 0; k < dataSize; k++)
			if (anIntArray728[k] == fileID) {
				if (abyte0 == null)
					abyte0 = new byte[anIntArray729[k]];
				if (!aBoolean732) {
					BZip2Decompressor.method225(abyte0, anIntArray729[k], aByteArray726, anIntArray730[k], anIntArray731[k]);
				} else {
					System.arraycopy(aByteArray726, anIntArray731[k], abyte0, 0, anIntArray729[k]);
				}
				return abyte0;
			}

		return null;
	}

	public byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Close the input stream and return bytes
		is.close();

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}


		return bytes;
	}

	private final byte[] aByteArray726;
	private final int dataSize;
	private final int[] anIntArray728;
	private final int[] anIntArray729;
	private final int[] anIntArray730;
	private final int[] anIntArray731;
	private final boolean aBoolean732;
}
