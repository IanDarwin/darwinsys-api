package com.darwinsys.graphics;

public class PhotoUtils {
	
    public static String exifDateToIso(final String exifDateString) {
        return exifDateString
                .replace(' ', 'T')
                .replaceFirst(":","-")
                .replaceFirst(":","-");
    }

    public static String isoDateToExif(final String isoDateString) {
        return isoDateString
                .replace('T', ' ')
                .replaceFirst("-",":")
                .replaceFirst("-",":");
    }
}
