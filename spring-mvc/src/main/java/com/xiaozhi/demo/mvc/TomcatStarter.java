package com.xiaozhi.demo.mvc;

import com.xiaozhi.demo.annotation.Autowired;
import com.xiaozhi.demo.annotation.Component;
import com.xiaozhi.demo.annotation.PostConstruct;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

/**
 *
 * @author DD
 */
@Component
public class TomcatStarter {

    @Autowired
    private DispatchServlet dispatchServlet;

    @PostConstruct
    public void init() throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8001);
        tomcat.getConnector();

        // 设置 webapp 目录
        Context webContext = tomcat.addWebapp("", new File(".").getAbsolutePath());

        tomcat.addServlet(webContext, "dispatchServlet", dispatchServlet);
        webContext.addServletMappingDecoded("/*", "dispatchServlet");

        tomcat.start();
        System.out.println("Tomcat 启动成功");
    }
}
