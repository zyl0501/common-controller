package com.ray.common.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.ray.common.lang.Strings;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public final class Networks {

  public static final int NETWORK_TYPE_EHRPD = 14; // Level 11
  public static final int NETWORK_TYPE_EVDO_B = 12; // Level 9
  public static final int NETWORK_TYPE_HSPAP = 15; // Level 13
  public static final int NETWORK_TYPE_IDEN = 11; // Level 8
  public static final int NETWORK_TYPE_LTE = 13; // Level 11

  /**
   * Check if there is any connectivity
   *
   * @param context
   * @return
   */
  public static boolean isConnected(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo info = cm.getActiveNetworkInfo();
    return (info != null && info.isConnected());
  }

  /**
   * Check if there is fast connectivity
   *
   * @param context
   * @return
   */
  public static boolean isFast(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo info = cm.getActiveNetworkInfo();
    return (info != null && info.isConnected() && isFast(info.getType(), info.getSubtype()));
  }

  public static boolean isWIFI(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo info = cm.getActiveNetworkInfo();
    return (info != null && info.isConnected() && ConnectivityManager.TYPE_WIFI == info.getType());
  }

  /**
   * Check if the connection is fast
   *
   * @param type
   * @param subType
   * @return
   */
  public static boolean isFast(int type, int subType) {
    if (type == ConnectivityManager.TYPE_WIFI) {

      return true;
    } else if (type == ConnectivityManager.TYPE_MOBILE) {
      switch (subType) {
        case TelephonyManager.NETWORK_TYPE_1xRTT:
          return false; // ~ 50-100 kbps
        case TelephonyManager.NETWORK_TYPE_CDMA:
          return false; // ~ 14-64 kbps
        case TelephonyManager.NETWORK_TYPE_EDGE:
          return false; // ~ 50-100 kbps
        case TelephonyManager.NETWORK_TYPE_EVDO_0:
          return true; // ~ 400-1000 kbps
        case TelephonyManager.NETWORK_TYPE_EVDO_A:
          return true; // ~ 600-1400 kbps
        case TelephonyManager.NETWORK_TYPE_GPRS:
          return false; // ~ 100 kbps
        case TelephonyManager.NETWORK_TYPE_HSDPA:
          return true; // ~ 2-14 Mbps
        case TelephonyManager.NETWORK_TYPE_HSPA:
          return true; // ~ 700-1700 kbps
        case TelephonyManager.NETWORK_TYPE_HSUPA:
          return true; // ~ 1-23 Mbps
        case TelephonyManager.NETWORK_TYPE_UMTS:
          return true; // ~ 400-7000 kbps
        // NOT AVAILABLE YET IN API LEVEL 7
        case NETWORK_TYPE_EHRPD:
          return true; // ~ 1-2 Mbps
        case NETWORK_TYPE_EVDO_B:
          return true; // ~ 5 Mbps
        case NETWORK_TYPE_HSPAP:
          return true; // ~ 10-20 Mbps
        case NETWORK_TYPE_IDEN:
          return false; // ~25 kbps
        case NETWORK_TYPE_LTE:
          return true; // ~ 10+ Mbps
        // Unknown
        case TelephonyManager.NETWORK_TYPE_UNKNOWN:
        default:
          return false;
      }
    } else {
      return false;
    }
  }

  /**
   * 获取网络连接的类型，如果网络未连接返回null
   *
   * @param context
   * @return
   */
  public static String getNetworkName(Context context) {
    if (isConnected(context)) {
      ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo info = cm.getActiveNetworkInfo();
      if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
        return info.getTypeName();
      } else {
        return info.getSubtypeName();
      }
    } else {
      return null;
    }
  }


  /**
   * 获取网络类型
   *
   * @param context
   * @return {@link android.net.ConnectivityManager#TYPE_WIFI}或者
   * {@link android.net.ConnectivityManager#TYPE_MOBILE}
   */
  public static int getNetworkType(Context context) {
    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    if (activeNetworkInfo == null) return -1;
    return activeNetworkInfo.getType();
  }


  public static String getNetworkOperator(Context context) {
    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    return tm.getNetworkOperator();
  }


  /**
   * Get IP address from first non-localhost interface
   *
   * @param ipv4 true=return ipv4, false=return ipv6
   * @return address or empty string
   */
  public static String getIPAddress(boolean ipv4) {
    try {
      List<NetworkInterface> interfaces = Collections
          .list(NetworkInterface.getNetworkInterfaces());
      for (NetworkInterface intf : interfaces) {
        List<InetAddress> addrs = Collections.list(intf
            .getInetAddresses());
        for (InetAddress addr : addrs) {
          if (addr.isLoopbackAddress()) return Strings.EMPTY;
          String sAddr = addr.getHostAddress().toUpperCase();
          boolean isIPv4 = isIPv4Address(sAddr);
          if (ipv4) {
            if (isIPv4) return sAddr;
          } else {
            if (!isIPv4) {
              int index = sAddr.indexOf('%'); // drop ip6 port
              return index < 0 ? sAddr : sAddr.substring(0, index);
            }
          }
        }
      }
    } catch (Exception ex) {
    }
    return Strings.EMPTY;
  }

  public enum NetworkType {
    CDMA(TelephonyManager.NETWORK_TYPE_CDMA, "CDMA: Either IS95A or IS95B (2G)"),
    EDGE(TelephonyManager.NETWORK_TYPE_EDGE, "EDGE (2.75G)"),
    GPRS(TelephonyManager.NETWORK_TYPE_GPRS, "GPRS (2.5G)"),
    UMTS(TelephonyManager.NETWORK_TYPE_UMTS, "UMTS (3G)"),
    EVDO_0(TelephonyManager.NETWORK_TYPE_EVDO_0, "EVDO revision 0 (3G)"),
    EVDO_A(TelephonyManager.NETWORK_TYPE_EVDO_A, "EVDO revision A (3G - Transitional)"),
    IDEN(11, "iDen (2G)"), //TelephonyManager.NETWORK_TYPE_IDEN
    EVDO_B(12, "EVDO revision B (3G - Transitional)"),//TelephonyManager.NETWORK_TYPE_EVDO_B
    LTE(13, "LTE (3G)"),//TelephonyManager.NETWORK_TYPE_LTE
    _1X_RTT(TelephonyManager.NETWORK_TYPE_1xRTT, "1xRTT  (2G - Transitional)"),
    HSDPA(TelephonyManager.NETWORK_TYPE_HSDPA, "HSDPA (3G - Transitional)"),
    HSUPA(TelephonyManager.NETWORK_TYPE_HSUPA, "HSUPA (3G - Transitional)"),
    HSPA(TelephonyManager.NETWORK_TYPE_HSPA, "HSPA (3G - Transitional)"),
    UNKOWN(-100, "Unknown"),;

    public final int code;
    public final String desc;

    NetworkType(int code, String desc) {
      this.code = code;
      this.desc = desc;
    }

    public static NetworkType valueOf(int code) {
      for (NetworkType nt : NetworkType.values()) {
        if (code == nt.code) {
          return nt;
        }
      }
      return NetworkType.UNKOWN;
    }
  }


  private static final String IPV4_BASIC_PATTERN_STRING =
      "(([1-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){1}" + // initial first field, 1-255
          "(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){2}" + // following 2 fields, 0-255 followed by .
          "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])"; // final field, 0-255

  private static final Pattern IPV4_PATTERN =
      Pattern.compile("^" + IPV4_BASIC_PATTERN_STRING + "$");

  /**
   * Checks whether the parameter is a valid IPv4 address
   *
   * @param input the address string to check for validity
   * @return true if the input parameter is a valid IPv4 address
   */
  public static boolean isIPv4Address(final String input) {
    return IPV4_PATTERN.matcher(input).matches();
  }

}
