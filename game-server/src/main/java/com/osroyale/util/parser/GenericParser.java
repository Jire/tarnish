package com.osroyale.util.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Represents an abstract parser.
 *
 * @author  Seven
 */
public abstract class GenericParser implements Runnable {

	private static final Logger logger = LogManager.getLogger();

	/**
	 * The path of the file to parse.
	 */
	protected final Path path;

	/**
	 * The index of the current line being parsed.
	 */
	protected int index;

	/**
	 * The file name extension to parse.
	 */
	private final String extension;

	private final boolean log;

	/**
	 * Creates a new {@code GenericParser}.
	 *
	 * @param path
	 * 		The path of the file to parse.
	 *
	 * @param extension
	 * 		The file name extension.
	 *
	 * @param log
	 * 		The flag that denotes to log messages.
	 */
	public GenericParser(String path, String extension , boolean log) {
		this.path = Paths.get("./data/", path + extension);
		this.extension = extension;
		this.log = log;
	}

	/**
	 * The method that deserializes the file information.
	 */
	public abstract void deserialize();

	@Override
	public void run() {
		deserialize();
		onRead();
		if (log) {
			logger.info(toString());
		}
	}

	/**
	 * The method called after all the data has been parsed.
	 */
	public void onRead() {

	}

	/**
	 * Gets the current index of the line being parsed.
	 *
	 * @return The index of this line.
	 */
	public final int getIndex() {
		return index;
	}

	@Override
	public String toString() {
		return String.format("Loaded: %d %s.", index, path.getFileName().toString().replace("_", " ").replace(extension, ""));
	}

}
