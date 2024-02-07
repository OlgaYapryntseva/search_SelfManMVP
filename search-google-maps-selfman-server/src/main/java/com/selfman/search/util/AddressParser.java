package com.selfman.search.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressParser {
    public static String parseCountryLocality(String placeAddress) {
        StringBuilder builder = new StringBuilder();
        String[] spans = placeAddress.split("</span>");
        for (String span : spans) {
            int index = span.indexOf('>');
            if (span.contains("locality")) {
                builder.append(span.substring(index + 1))
                        .append(" ");
            } else if (span.contains("country-name")) {
                builder.append(span.substring(index + 1));
            }
        }
        return builder.toString();
    }
}
