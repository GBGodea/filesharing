package org.godea.di;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

public abstract class InjectedServlet extends HttpServlet {
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Injector.inject(this);
    }
}
