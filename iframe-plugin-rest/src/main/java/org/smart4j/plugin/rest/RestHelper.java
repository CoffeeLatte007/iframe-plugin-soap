package org.smart4j.plugin.rest;



import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.jaxrs.provider.jsonp.JsonpInInterceptor;
import org.apache.cxf.jaxrs.provider.jsonp.JsonpPostStreamInterceptor;
import org.apache.cxf.jaxrs.provider.jsonp.JsonpPreStreamInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharingFilter;
import org.framework.helper.BeanHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaoz on 2016/4/27.
 */

public class RestHelper {
    private static final List<Object> providerList=new ArrayList<Object>();
    private static final List<Interceptor<? extends Message>> inInterceptorList=new ArrayList<Interceptor<? extends Message>>();
    private static final List<Interceptor<? extends Message>> outInterceptorList=new ArrayList<Interceptor<? extends Message>>();
    static {
        //首先我们应该添加JSON Provider
        JacksonJsonProvider jsonProvider=new JacksonJsonProvider();
        providerList.add(jsonProvider);
        //添加Logging Interceptor
        if (RestConfig.isLog()){
            LoggingInInterceptor loggingInInterceptor=new LoggingInInterceptor();
            inInterceptorList.add(loggingInInterceptor);
            LoggingOutInterceptor loggingOutInterceptor=new LoggingOutInterceptor();
            outInterceptorList.add(loggingOutInterceptor);
        }
        //添加JSONP Interceptor
        if (RestConfig.isJsonp()){
            JsonpInInterceptor jsonpInInterceptor=new JsonpInInterceptor();
            jsonpInInterceptor.setCallbackParam(RestConfig.getJsonpFunction());
            inInterceptorList.add(jsonpInInterceptor);
            JsonpPreStreamInterceptor jsonpPreStreamInterceptor=new JsonpPreStreamInterceptor();
            outInterceptorList.add(jsonpPreStreamInterceptor);
            JsonpPostStreamInterceptor jsonpPostStreamInterceptor=new JsonpPostStreamInterceptor();
            outInterceptorList.add(jsonpPostStreamInterceptor);
        }
        //支持Cors
        if (RestConfig.isCors()){
            CrossOriginResourceSharingFilter corsProvider=new CrossOriginResourceSharingFilter();
            corsProvider.setAllowOrigins(RestConfig.getCorsOriginList());
            providerList.add(corsProvider);
        }
    }
    //发布rest
    public static void publishServie(String wsdl,Class<?> resourceClass){
        JAXRSServerFactoryBean factory=new JAXRSServerFactoryBean();
        factory.setAddress(wsdl);
        factory.setResourceClasses(resourceClass);
        factory.setResourceProvider(resourceClass,new SingletonResourceProvider(BeanHelper.getBean(resourceClass)));
        factory.setProviders(providerList);
        factory.setInInterceptors(inInterceptorList);
        factory.setOutInterceptors(outInterceptorList);
        factory.create();
    }
    public static <T> T createClient(String wsdl,Class<? extends T> resourceClass){
        return JAXRSClientFactory.create(wsdl,resourceClass,providerList);
    }



}
