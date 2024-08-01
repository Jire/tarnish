package com.osroyale;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class VarBit {

	public static void unpackConfig(StreamLoader streamLoader) {
		byte[] array = streamLoader.getFile("varbit.dat");
		/*try {
			array = Files.readAllBytes(Path.of("varbit.dat"));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		Buffer stream = new Buffer(array);
		int cacheSize = stream.readUnsignedShort();
		if (varBits == null)
			varBits = new VarBit[cacheSize];
		for (int j = 0; j < cacheSize; j++) {
			int id = stream.readUnsignedShort();
			if (id == -1 || id == 65535) continue;
			if (varBits[id] == null)
				varBits[id] = new VarBit();
			int length = stream.readUnsignedShort();
			byte[] data = new byte[length];
			stream.readBytes(length, 0, data);
			varBits[id].readValues(new Buffer(data));
			if (varBits[id].loaded)
				Varp.varps[varBits[id].index].aBoolean713 = true;
		}
	}

	private void readValues(Buffer stream) {
		while (true) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0) break;
			if (opcode == 1) {
				index = stream.readUnsignedShort();
				leastSignificantBit = stream.readUnsignedByte();
				mostSignificantBit = stream.readUnsignedByte();
			} else {
				System.out.println("Unknown varbit opcode: " + opcode);
			}
		}
	}

	private VarBit() {
		loaded = false;
	}

	public static VarBit varBits[];
	public int index;
	public int leastSignificantBit;
	public int mostSignificantBit;
	private boolean loaded;
}
