package org.godea.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.godea.di.Service;
import org.godea.interfaces.HtmlRenderer;

import java.io.IOException;

@Service
public class RegisterService implements HtmlRenderer {
    public void handleGet(String path, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (path) {
            case "/users" -> getUsers(req, resp);
            case "/admin" -> getAdmins(req, resp);
        }
    }

    private void getUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderHtml(req, resp, "/users/getuser.html");
    }

    private void getAdmins(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderHtml(req, resp, "/users/getadmins.html");
    }

    @Override
    public void renderHtml(HttpServletRequest req,
                           HttpServletResponse resp,
                           String path) throws ServletException, IOException {
        if(!path.startsWith("/")) {
            path = "/" + path;
        }
        req.getRequestDispatcher(path).forward(req, resp);
    }
}
