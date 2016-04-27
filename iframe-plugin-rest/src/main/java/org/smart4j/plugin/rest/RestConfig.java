package org.smart4j.plugin.rest;

import org.framework.helper.ConfigHelper;
import org.framework.util.StringUtil;

import java.util.Arrays;
import java.util.List;

/**
 * 获取配置文件相关属性
 * Created by lizhaoz on 2016/4/27.
 */

public class RestConfig {
    public static boolean isLog() {

        return ConfigHelper.getBoolean(RestConstant.LOG);
    }

    public static boolean isJsonp() {
        return ConfigHelper.getBoolean(RestConstant.JSONP);
    }

    public static String getJsonpFunction() {
        return ConfigHelper.getString(RestConstant.JSONP_FUNCTION);
    }

    public static boolean isCors() {
        return ConfigHelper.getBoolean(RestConstant.CORS);
    }
    public static List<String> getCorsOriginList(){
        String corsOrigin =ConfigHelper.getString(RestConstant.CORS_ORGIN);
        return Arrays.asList(StringUtil.splitString(corsOrigin, ","));
    }
}
