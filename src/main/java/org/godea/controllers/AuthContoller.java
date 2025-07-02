package org.godea.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.godea.di.Autowired;
import org.godea.di.Controller;
import org.godea.di.Route;
import org.godea.services.AuthService;

import java.io.IOException;

@Controller(path = "/api")
public class AuthContoller {
    @Autowired
    AuthService authService;

    @Route(path = "/login", method = "GET")
    public void getLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        authService.handleGetLogin(req, resp);
    }

    @Route(path = "/login", method = "POST")
    public void postLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        authService.handlePostLogin(req, resp);
    }
}
