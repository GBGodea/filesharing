package org.godea.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.godea.di.Autowired;
import org.godea.di.InjectedServlet;
import org.godea.services.RegisterService;

import java.io.IOException;

@WebServlet("/api/*")
public class RegisterController extends InjectedServlet {
    @Autowired
    RegisterService registerService;

    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        String path = req.getPathInfo();

        if (path == null) {
            path = "/";
        }

        switch (method) {
            case "GET" -> registerService.handleGet(path, req, resp);
        }
    }
}
