package com.darwinsys.csv;

import java.util.List;

public interface CSVParser {
	/** Parse one String, and return it as a list of strings. */
	public List<String> parse(String line);
}
