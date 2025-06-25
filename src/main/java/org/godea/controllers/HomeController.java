package org.godea.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.godea.di.Controller;
import org.godea.di.Route;

import java.io.IOException;

@Controller
public class HomeController {
    @Route(path = "/", method = "GET")
    public void index(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.getRequestDispatcher("/register.html").forward(req, resp);
    }
}
