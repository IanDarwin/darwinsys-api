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
            boolean hasComma = val.indexOf(',') != -1;
            if (hasComma && !isQuoted) {
                val = val.replaceAll(",", "\\,");
            }
            sb.append(val);
		}
        return sb.toString();
	}

}
