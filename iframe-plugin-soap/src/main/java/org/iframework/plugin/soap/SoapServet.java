package org.iframework.plugin.soap;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.framework.helper.BeanHelper;
import org.framework.helper.ClassHelper;
import org.framework.util.CollectionUtil;
import org.framework.util.StringUtil;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import java.util.Set;

/**
 * Created by lizhaoz on 2016/4/27.
 */
@WebServlet(urlPatterns = SoapConstant.SERVLET_URL,loadOnStartup = 0)
public class SoapServet extends CXFNonSpringServlet {
    @Override
    protected void loadBus(ServletConfig sc) {
        super.loadBus(sc);
        Bus bus=getBus();
        BusFactory.setDefaultBus(bus);
        //发布Soap
        publishSoapService();
    }

    private void publishSoapService() {
        //遍历所有标注SOAP注解的类
        Set<Class<?>> soapClassSet= ClassHelper.getClassSetByAnnotation(Soap.class);
        if(CollectionUtil.isNotEmpty(soapClassSet)){
            for (Class<?> soapClass:soapClassSet){
                //获取Soap地址
                String address=getAddress(soapClass);
                //获取SOAP类的接口
                Class<?> soapInterfaceClass=getSoapInterfaceClass(soapClass);
                //获取SOAP类的实例
                Object soapInstance= BeanHelper.getBean(soapClass);
                //发布Soap
                SoapHelper.publishService(address,soapInterfaceClass,soapInstance);
            }
        }
    }

    private String getAddress(Class<?> soapClass) {
        String address;
        //如果不为空就位用户设定，如果为空就获取当前值
        String soapValue=soapClass.getAnnotation(Soap.class).value();
        if (StringUtil.isNotEmpty(soapValue)){
            address=soapValue;
        }else {
            address=getSoapInterfaceClass(soapClass).getSimpleName();
        }
        return address;
    }

    private Class<?> getSoapInterfaceClass(Class<?> soapClass) {
        //获取SOAP实现类的第一个接口作为SOAP服务接口
        return soapClass.getInterfaces()[0];
    }
}
