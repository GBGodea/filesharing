package org.godea.di;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServlet;

@WebListener
public class InjectorListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        Injector.initialize("org.godea");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }
}
