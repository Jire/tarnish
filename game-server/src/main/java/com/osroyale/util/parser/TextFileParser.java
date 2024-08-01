package com.osroyale.util.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * A simple parser designed for text files.
 *
 * @author Seven
 */
public abstract class TextFileParser extends GenericParser {

	/**
	 * Creates a new {@code TextFileParser}.
	 *
	 * @param path
	 *      The path of the file to parse.
	 */
	public TextFileParser(String path) {
		this(path, true);
	}

	/**
	 * Creates a new {@code TextFileParser}.
	 *
	 * @param path
	 *      The path of the file to parse.
	 *
	 * @param log
	 * 		The flag that denotes to log messages.
	 */
	public TextFileParser(String path, boolean log) {
		super(path, ".txt", log);
	}

	/**
	 * The method called when the file is being parsed.
	 *
	 * @param reader
	 *      The underlying parser.
	 */
	public abstract void parse(BufferedReader reader) throws IOException;

	@Override
	public void deserialize() {
		try(BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
			while(reader.readLine() != null) {
				parse(reader);
				index++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

