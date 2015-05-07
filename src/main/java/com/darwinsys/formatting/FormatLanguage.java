package com.darwinsys.formatting;

/**
 * The language that an entry (such as text in a Blog or a Cookbook Site)
 * has been encoded in during authoring.
 * This explicitly does not define what the author typed, but what was stored
 * as a result (e.g., consider the various JavaScript edit widgets where you type
 * in text and use mouse editing, but the content is sadly stored in HTML).
 */
public enum FormatLanguage {
	ASCIIDOC,
	HTML,
	MARKDOWN,
	PLAIN,
	WIKI
}
