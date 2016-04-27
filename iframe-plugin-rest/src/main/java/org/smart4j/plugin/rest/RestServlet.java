package org.smart4j.plugin.rest;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.framework.helper.ClassHelper;
import org.framework.util.CollectionUtil;
import org.framework.util.StringUtil;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import java.util.Set;

/**
 * REST Servlet
 * Created by lizhaoz on 2016/4/27.
 */
@WebServlet(urlPatterns = RestConstant.SERVLET_URL,loadOnStartup = 0)
public class RestServlet extends CXFNonSpringServlet{
    @Override
    protected void loadBus(ServletConfig sc) {
        //初始化CXF总线
        super.loadBus(sc);
        Bus bus=getBus();
        BusFactory.setDefaultBus(bus);
        //发布REST服务
        publishRestService();
    }

    private void publishRestService() {
        //遍历所有标注了REST注解的类
        Set<Class<?>> restClassSet= ClassHelper.getClassSetByAnnotation(Rest.class);
        if (CollectionUtil.isNotEmpty(restClassSet)){
            for (Class<?> restClass:restClassSet){
                //获取REST地址
                String address=getAddress(restClass);
                //发布REST服务
                RestHelper.publishServie(address,restClass);
            }
        }
    }

    private String getAddress(Class<?> restClass) {
        String address;
        //若REST注解的value属性不为空，则获取当前值，否则获取类名
        String value=restClass.getAnnotation(Rest.class).value();
        if (StringUtil.isNotEmpty(value)){
            address=value;
        }
        else {
            address=restClass.getSimpleName();
        }
        //确保最前面只有一个/
        if (!address.startsWith("/")){
            address ="/" + address;
        }
        //如果有多个//替换为/
        address=address.replaceAll("\\/+","/");
        return address;
    }
}
