package com.osroyale;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

final class BufferedConnection implements Runnable {

	public BufferedConnection(Socket socket1) throws IOException {
		closed = false;
		isWriter = false;
		hasIOError = false;
		socket = socket1;
		socket.setSoTimeout(30000);
		socket.setTcpNoDelay(true);
		socket.setReceiveBufferSize(2 << 15);
		socket.setSendBufferSize(2 << 15);
		try {
			// not supported on early Windows versions like 2003 and XP
			socket.setTrafficClass(0b101_110_00);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		inputStream = socket.getInputStream();
		outputStream = socket.getOutputStream();
	}

	public void close() {
		closed = true;
		try {
			if (inputStream != null)
				inputStream.close();
			if (outputStream != null)
				outputStream.close();
			if (socket != null)
				socket.close();
		} catch (IOException _ex) {
			System.out.println("Error closing stream");
		}
		isWriter = false;
		synchronized (this) {
			notify();
		}
		buffer = null;
	}

	public int read() throws IOException {
		if (closed)
			return 0;
		else
			return inputStream.read();
	}

	public int available() throws IOException {
		if (closed)
			return 0;
		else
			return inputStream.available();
	}

	public void flushInputStream(byte abyte0[], int j) throws IOException {
		int i = 0;//was parameter
		if (closed)
			return;
		int k;
		for (; j > 0; j -= k) {
			k = inputStream.read(abyte0, i, j);
			if (k <= 0)
				throw new IOException("EOF");
			i += k;
		}

	}

	public void queueBytes(int i, byte abyte0[]) throws IOException {
		if (closed)
			return;
		if (hasIOError) {
			hasIOError = false;
			throw new IOException("Error in writer thread");
		}
		if (buffer == null)
			buffer = new byte[5000];
		synchronized (this) {
			for (int l = 0; l < i; l++) {
				buffer[buffIndex] = abyte0[l];
				buffIndex = (buffIndex + 1) % 5000;
				if (buffIndex == (writeIndex + 4900) % 5000)
					throw new IOException("buffer overflow");
			}

			if (!isWriter) {
				isWriter = true;
				Thread t = new Thread(this);
				t.setPriority(3);
				t.start();
			}
			notify();
		}
	}

	public void run() {
		while (isWriter) {
			int i;
			int j;
			synchronized (this) {
				if (buffIndex == writeIndex)
					try {
						wait();
					} catch (InterruptedException _ex) {
					}
				if (!isWriter)
					return;
				j = writeIndex;
				if (buffIndex >= writeIndex)
					i = buffIndex - writeIndex;
				else
					i = 5000 - writeIndex;
			}
			if (i > 0) {
				try {
					outputStream.write(buffer, j, i);
				} catch (IOException _ex) {
					hasIOError = true;
				}
				writeIndex = (writeIndex + i) % 5000;
				try {
					if (buffIndex == writeIndex)
						outputStream.flush();
				} catch (IOException _ex) {
					hasIOError = true;
				}
			}
		}
	}

	public void printDebug() {
		System.out.println("dummy:" + closed);
		System.out.println("tcycl:" + writeIndex);
		System.out.println("tnum:" + buffIndex);
		System.out.println("writer:" + isWriter);
		System.out.println("ioerror:" + hasIOError);
		try {
			System.out.println("available:" + available());
		} catch (IOException _ex) {
		}
	}

	private InputStream inputStream;
	private OutputStream outputStream;
	private final Socket socket;
	private boolean closed;

	private byte[] buffer;
	private int writeIndex;
	private int buffIndex;
	private boolean isWriter;
	private boolean hasIOError;
}
