package csv;

import java.util.List;

public class CSVExport {

	public static String toString(List data) {
        
        StringBuilder sb = new StringBuilder();
        
		for (Object o : data) {
            
            if (o == null) {
                sb.append("\"\"");
                continue;
            }
            if (sb.length() > 0) {
                sb.append(',');
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
