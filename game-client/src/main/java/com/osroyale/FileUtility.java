package com.osroyale;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class FileUtility {

	private FileUtility() {

	}

	public static File getOrCreate(String filePath, String fileName) throws IOException {
		File directory = new File(filePath);

		if (!directory.exists() && !directory.mkdir()) {
			throw new IOException("Could not create path '" + filePath + "'");
		}

		File file = new File(directory, fileName);
		if (!file.exists() && !file.createNewFile()) {
			throw new IOException("Could not create file '" + fileName + "'");
		}

		return file;
	}

	public static void replaceData(File source, File target) throws IOException {
		ZipEntry entry;
		int length;
		byte[] buffer = new byte[1024];
		try (ZipInputStream in = new ZipInputStream(new BufferedInputStream(new FileInputStream(source))); ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(target)))) {
			while ((entry = in.getNextEntry()) != null) {
				if (entry.isDirectory())
					continue;

				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				while ((length = in.read(buffer, 0, buffer.length)) > -1) {
					bytes.write(buffer, 0, length);
				}

				out.putNextEntry(entry);
				out.write(bytes.toByteArray());
				out.closeEntry();
			}
			out.finish();
		}
	}

	public static final byte[] readFile(String name) {
		try {
			File file = new File(name);
			int i = (int) file.length();
			byte abyte0[] = new byte[i];
			DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new FileInputStream(name)));
			datainputstream.readFully(abyte0, 0, i);
			datainputstream.close();
			totalRead++;
			return abyte0;
		} catch (Exception exception) {
		}
		return null;
	}

	public static final void writeFile(String name, byte data[]) {
		try {
			(new File((new File(name)).getParent())).mkdirs();
			FileOutputStream fileoutputstream = new FileOutputStream(name);
			fileoutputstream.write(data, 0, data.length);
			fileoutputstream.close();
			totalWrite++;
			completeWrite++;
		} catch (Throwable throwable) {
			System.out.println((new StringBuilder()).append("Write Error: ").append(name).toString());
		}
	}

	public static int totalRead = 0;
	public static int totalWrite = 0;

	public static int completeWrite = 0;

}