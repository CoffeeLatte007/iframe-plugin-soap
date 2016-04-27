package org.iframework.plugin.soap;




import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * SOAP助手
 * Created by lizhaoz on 2016/4/27.
 */

public class SoapHelper {
    private static final List<Interceptor<? extends Message>> inInterceptorList=new ArrayList<Interceptor<? extends Message>>();
    private static final List<Interceptor<? extends Message>> outInterceptorList=new ArrayList<Interceptor<? extends Message>>();
    static {
        //如果开启了日志，添加日志拦截器
        if (SoapConfig.isLog()){
            //日志拦截器
            LoggingInInterceptor loggingInInterceptor=new LoggingInInterceptor();
            inInterceptorList.add(loggingInInterceptor);
            LoggingOutInterceptor loggingOutInterceptor=new LoggingOutInterceptor();
            outInterceptorList.add(loggingOutInterceptor);
        }
    }

    /**
     * 发布SOAP服务
     * @param wsdl
     * @param interfaceClass
     * @param implementInstance
     */
    public static void publishService(String wsdl,Class<?> interfaceClass,Object implementInstance){
        ServerFactoryBean factory=new ServerFactoryBean();
        factory.setAddress(wsdl);
        factory.setServiceClass(interfaceClass);
        factory.setServiceBean(implementInstance);
        factory.setInInterceptors(inInterceptorList);
        factory.setOutInterceptors(outInterceptorList);
        factory.create();
    }
    /**
     * 创建SOAP客户端
     */
    public static <T> T createClient(String wsdl,Class<? extends T> interfaceClass){
        ClientProxyFactoryBean factory=new ClientProxyFactoryBean();
        factory.setAddress(wsdl);
        factory.setServiceClass(interfaceClass);
        factory.setInInterceptors(inInterceptorList);
        factory.setOutInterceptors(outInterceptorList);
        return factory.create(interfaceClass);
    }
}
