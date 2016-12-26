package com.cbinfo.utils;

import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public final class WebsiteUtils {

    public static boolean checkUrl(String url) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+[.][a-zA-Z0-9]+");
        return isNotBlank(url)&&pattern.matcher(url).matches();
    }
}
