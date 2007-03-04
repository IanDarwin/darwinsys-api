package com.darwinsys.csv;

import java.util.List;

public class CSVExport {

    public static String toString(List data) {
        return toString(data, ',');
    }

	public static String toString(List data, char delim) {

        StringBuilder sb = new StringBuilder();

		for (Object o : data) {

			if (sb.length() > 0) {
				sb.append(delim);
			}
            if (o == null) {
                sb.append("\"\"");
                continue;
            }
			String val = o.toString();
            try {
                Double.parseDouble(val);
                sb.append(val);
                continue;
            } catch (NumberFormatException e) {
                // Nothing to do; it's just not numeric
            }
            boolean isQuoted = val.startsWith("\"");
            boolean mustQuote = false;
            boolean hasSpecial = val.indexOf(',') != -1 ||
            	val.indexOf('\n') != -1;
            if (hasSpecial && !isQuoted) {
                mustQuote = true;
            }
            if (mustQuote) {
            	sb.append('"').append(val).append('"');
            } else {
            	sb.append(val);
            }
		}
        return sb.toString();
	}

	public static String toCSV(Object data, char delim) {
		return null;
	}

	public static String toHeader(Object data, char delim) {
		return null;
	}

}
