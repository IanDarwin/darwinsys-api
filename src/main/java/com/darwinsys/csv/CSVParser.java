package com.darwinsys.csv;

import java.util.List;

/** The parser interface */
public interface CSVParser {

	/**
	 * Parse one String, and return it as a list of strings.
	 * @param line The line to be parsed
	 * @return The parsed list of strings.
	 */
	public List<String> parse(String line);
}
