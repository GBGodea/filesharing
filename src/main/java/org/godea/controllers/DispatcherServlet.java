package org.godea.controllers;

import io.jsonwebtoken.Claims;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/*")
@MultipartConfig
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
        String servletPath = req.getServletPath();
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

        String key = httpMethod + ":" + servletPath + path;

        Handler handler = routes.get(key);
        if(handler == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return ;
        }

        Method method = handler.method;
        if(method.isAnnotationPresent(Secured.class)) {
            Secured sec = method.getAnnotation(Secured.class);
            Claims claims = (Claims) req.getAttribute("claims");

            if(claims == null) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Jwt token is missing or invalid");
                return;
            }

            String userRole = claims.get("role", String.class);
            if(!sec.role().equals(userRole)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                return ;
            }
        }

        try {
            method.invoke(handler.clazz, req, resp);
        } catch(Exception e) {
            e.printStackTrace();
            Throwable cause = e.getCause();
            if (cause != null) {
                cause.printStackTrace();
            }
            throw new ServletException("Error in handler: " + (cause != null ? cause : e), cause);
        }
    }

    private static class Handler {
        private final Object clazz;
        private final Method method;

        public Handler(Object clazz, Method method) {
            this.clazz = clazz;
            this.method = method;
        }
    }
}
