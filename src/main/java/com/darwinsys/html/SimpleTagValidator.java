package com.darwinsys.html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Check a String to ensure that it contains either no HTML tags, or only
 * the HTML tags listed in OKTAGS (or the corresponding end-tags).
 * It is <em>not</em> intended as a full-function HTML validator!
 * Nor is any claim advanced that this is a complete solution to preventing
 * Cross-Site Scripting. It is just, well, a SIMPLE Tag Validator.
 */
public class SimpleTagValidator {
	/** The default list of OK tags.
	 * As of this writing it allows only a, b, br, em, i and p tags.
	 */
	private static final String[] OKTAGS = {
			"a", "b", "br", "em", "i", "p"
	};
	private final String[] okTags;
	
	private final Pattern patt = Pattern.compile("<\\s*/?(\\w+)");
	private String failedTag = null;
	
	/** Construct a Validator using the default OK list */
	public SimpleTagValidator() {
		okTags = OKTAGS;
	}
	
	/** Construct a Validator using a non-default OK list */
	public SimpleTagValidator(final String[] okTagList) {
		okTags = new String[okTagList.length];
		System.arraycopy(okTagList, 0, okTags, 0, okTagList.length);
	}
	
	/** Validate a String that may contain HTML to ensure it contains
	 * only the tags listed in the OK list.
	 * @param s The HTML String to validate.
	 * @return true iff s does not contain any non-OK strings.
	 */
	public boolean validate(String s) {
		if (s == null)
			throw new NullPointerException("validate: null input");
		failedTag = null;
		Matcher m = patt.matcher(s);
		while (m.find()) {
				String tagName = m.group(1);
				System.out.println(tagName);
				// If even one tag bad, we return false.
				if (!okTag(tagName)) {
					failedTag = tagName;
					return false;
				}
		}
		// No bad tags found...
		return true;
	}

	/**
	 * Check that the tag extracted is valid.
	 * @param tagName The tag name, e.g., "p" from "<p align='centre'>" or "/p"
	 * @return True  if the tagname is in the OK list.
	 */
	private boolean okTag(String tagName) {
		for (int i = 0; i < okTags.length; i++) {
			if (okTags[i].equalsIgnoreCase(tagName))
				return true;
		}
		return false;
	}
	
	/** Return the last tag that failed.
	 * Usage example:
	 * <code>System.out.printf("Invalid tag %s\n", val.getFailedTag());</code>
	 * @return The name of the tag that caused validate() to fail.
	 */
	public String getFailedTag() {
		return failedTag;
	}
	
	/**
	 * Return the list of valid tags as a single string.
	 * Usage example:
	 * <code>System.out.printf("Valid tags are %01s\n", val.tagsAsString());</code> 
	 * @param useCommas True to include commas ("a, b, c"); false just space ("a b c").
	 */
	public String getTagsAsString(boolean useCommas) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < okTags.length; i++) {
			sb.append(okTags[i]);
			if (i < okTags.length-1) {
				if (useCommas)
					sb.append(',');
				sb.append(' ');
			}
		}
		return sb.toString();
	}
}
