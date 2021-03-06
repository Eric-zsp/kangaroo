package io.github.pactstart.commonutils;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验工具类
 */
public class ValidUtils {

    /**
     * 判断字符串有效性
     */
    public static boolean isValid(String src) {
        if (src == null || "".equals(src.trim())) {
            return false;
        }
        return true;
    }

    /**
     * 判断集合的有效性
     */
    public static boolean isValid(Collection<?> col) {
        if (col == null || col.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 判断数组是否有效
     */
    public static boolean isValid(Object[] arr) {
        if (arr == null || arr.length == 0) {
            return false;
        }
        return true;
    }

    /**
     * 判断布尔对象是否有效
     *
     * @param bool
     * @return
     */
    public static boolean isValid(Boolean bool) {
        return bool != null && bool;
    }

    /**
     * 验证手机是否合法，如果合法返回true,否则返回false
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone, boolean strict) {
        if (!ValidUtils.isValid(phone)) {
            return false;
        }
        if (strict) {
            return !PhoneUtils.isUnknown(phone);
        } else {
            return phone.matches("1[3456789]\\d{9}");
        }
    }

    /**
     * 验证手机是否邮箱，如果合法返回true,否则返回false
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        return isValid(email) && email.matches("(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,3}){1,3})");
    }

    /**
     * 验证URL是否有效
     *
     * @param url
     * @return
     */
    public static boolean isURL(String url) {
        if (!isValid(url)) {
            return false;
        }
        url = url.toLowerCase();
        String regex = "^((https|http|ftp|rtsp|mms)?://)" + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?"
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "[a-z]{2,6})" // first level domain- .com or .museum
                + "(:[0-9]{1,4})?" // 端口- :80
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        return url.matches(regex);
    }

    public static boolean isAllWildcardVariableValid(String template, Map<String, String> params) {
        Matcher m = Pattern.compile("\\$\\{\\w+\\}").matcher(template);
        while (m.find()) {
            String param = m.group();
            String varName = param.substring(2, param.length() - 1);
            String varValue = params.get(varName);
            if (varValue == null) {
                return false;
            }
        }
        return true;
    }
}
