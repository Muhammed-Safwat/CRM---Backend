package com.gws.crm.common.utils;

import java.util.HashMap;
import java.util.Map;

public class CountryCodeMapper {

    private static final Map<String, String> countryCodeToRegionMap = new HashMap<String, String>() {{
        put("+20", "EG");   // Egypt
        put("+966", "SA");  // Saudi Arabia
        put("+971", "AE");  // United Arab Emirates
        put("+974", "QA");  // Qatar
        put("+962", "JO");  // Jordan
        put("+973", "BH");  // Bahrain
        put("+961", "LB");  // Lebanon
        put("+965", "KW");  // Kuwait
        put("+968", "OM");  // Oman
        put("+212", "MA");  // Morocco
        put("+213", "DZ");  // Algeria
        put("+216", "TN");  // Tunisia
        put("+249", "SD");  // Sudan
        put("+967", "YE");  // Yemen
        put("+964", "IQ");  // Iraq
        put("+1", "US");    // United States
        put("+44", "GB");   // United Kingdom
        put("+33", "FR");   // France
        put("+49", "DE");   // Germany
        put("+91", "IN");   // India
        put("+86", "CN");   // China
        put("+81", "JP");   // Japan
        put("+55", "BR");   // Brazil
        put("+61", "AU");   // Australia
        put("+27", "ZA");   // South Africa
        put("+234", "NG");  // Nigeria
        put("+7", "RU");    // Russia
        put("+82", "KR");   // South Korea
        put("+52", "MX");   // Mexico
    }};

    public static String getRegionCode(String countryCode) {
        return countryCodeToRegionMap.get(countryCode);
    }


}
