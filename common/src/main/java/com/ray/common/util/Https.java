package com.ray.common.util;

import com.ray.common.Constants;
import com.ray.common.lang.Strings;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Https {

  public static final String HTTP_SCHEME = "http://";

  public static String parseCharset(Map<String, String> headers, String defaultCharset) {
    String contentType = headers.get(Constants.CONTENT_TYPE);
    if (contentType != null) {
      String[] params = contentType.split(";");
      for (int i = 1; i < params.length; i++) {
        String[] pair = params[i].trim().split("=");
        if (pair.length == 2) {
          if (pair[0].equals("charset")) {
            return pair[1];
          }
        }
      }
    }

    return defaultCharset;
  }

  /**
   * Returns the charset specified in the Content-Type of this header,
   * or the HTTP default (ISO-8859-1) if none can be found.
   */
  public static String parseCharset(Map<String, String> headers) {
    return parseCharset(headers, Constants.UTF_8_NAME);
  }


  public static String encodeUrl(String url) {
    try {
      return URLEncoder.encode(url, Constants.UTF_8_NAME);
    } catch (UnsupportedEncodingException e) {
    }
    return url;
  }


  public static String findPath(String url) {
    if (Strings.isBlank(url)) return Strings.EMPTY;
    int index1 = url.indexOf("://");
    if (index1 == -1) index1 = 0;
    else index1 = index1 + 3;
    int index2 = url.indexOf('/', index1);
    if (index2 == -1) return Strings.EMPTY;
    int index3 = url.indexOf('?', index2 + 1);
    if (index3 == -1) index3 = url.length();
    return url.substring(index2, index3);
  }

  public static String findQuery(String url) {
    if (Strings.isBlank(url)) return Strings.EMPTY;
    int index3 = url.indexOf('?');
    if (index3 == -1) return Strings.EMPTY;
    return url.substring(index3 + 1);
  }


  public static Map<String, String> toQueryMap(String query) {
    if (Strings.isBlank(query)) return Collections.EMPTY_MAP;
    Map<String, String> params = new HashMap<>();
    int start = 0, L = query.length();
    do {
      int next = query.indexOf('&', start);
      int end = (next == -1) ? L : next;

      int separator = query.indexOf('=', start);
      if (separator > end || separator == -1) break;

      String name = query.substring(start, separator);
      String value = query.substring(separator + 1, end);
      params.put(name, value);

      // Move start to end of name.
      start = end + 1;
    } while (start < L);
    return params;
  }
}
