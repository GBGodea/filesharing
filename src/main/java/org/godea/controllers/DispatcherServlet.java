package org.godea.controllers;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.godea.di.Controller;
import org.godea.di.Injector;
import org.godea.di.Route;
import org.godea.di.Secured;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Map;


/*
Map где хранятся все существующие пути на сервере(путь controller + пути от методов)
Также создать аннотации Post, Get и т.д., которые будут носить только информацию о методе запроса
Сам Dispatcher servlet будет просто проверять есть ли в map такой путь, если да, то идти
к контроллеру, а там и вызывать нужный метод, который помечает данные метод
 */

@WebServlet("/*")
public class DispatcherServlet extends HttpServlet {
    private final Map<String, Handler> routes = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Injector.initialize("org.godea");

        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forPackage("org.godea.controllers"))
                        .filterInputsBy(new FilterBuilder().includePackage("org.godea.controllers"))
        );

        for (Class<?> clazz : reflections.getTypesAnnotatedWith(Controller.class)) {
            Object controllerBean;
            try {
                controllerBean = Injector.getBean(clazz);
                Injector.inject(controllerBean);
            } catch (Exception e) {
                throw new ServletException("Can't to create controller");
            }

            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Route.class)) {
                    Route routeAnnotation = method.getAnnotation(Route.class);
                    String key = routeAnnotation.method() + ":" + clazz.getAnnotation(Controller.class).path() + routeAnnotation.path();
                    routes.put(key, new Handler(controllerBean, method));
                }
            }
        }
    }

    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String httpMethod = req.getMethod();
        String path = req.getPathInfo();

        String real = req.getServletContext().getRealPath(path);
        if (real != null) {
            File f = new File(real);
            if (f.exists() && !f.isDirectory()) {
                req.getServletContext()
                        .getNamedDispatcher("default")
                        .forward(req, resp);
                return;
            }
        }

        String key = httpMethod + ":" + path;

        Handler handler = routes.get(key);
        if(handler == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return ;
        }

        Method method = handler.method;
        if(method.isAnnotationPresent(Secured.class)) {
            String requiredRole = method.getAnnotation(Secured.class).role();
            String userRole = (String) req.getSession().getAttribute("role");

            if(!requiredRole.equals(userRole)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                return ;
            }
        }

        try {
            method.invoke(handler.clazz, req, resp);
        } catch(Exception e) {
            throw new ServerException(e.toString());
        }
    }

    private class Handler {
        private Object clazz;
        private Method method;

        public Handler(Object clazz, Method method) {
            this.clazz = clazz;
            this.method = method;
        }
    }
}
