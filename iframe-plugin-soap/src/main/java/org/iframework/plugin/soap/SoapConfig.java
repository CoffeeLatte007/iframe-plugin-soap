package org.iframework.plugin.soap;

import org.framework.helper.ConfigHelper;

/**
 * Created by lizhaoz on 2016/4/27.
 */

public class SoapConfig {
    public static boolean isLog() {
        return ConfigHelper.getBoolean(SoapConstant.LOG);
    }
}
