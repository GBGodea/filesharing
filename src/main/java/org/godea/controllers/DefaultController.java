package org.godea.controllers;

import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.godea.di.Autowired;
import org.godea.di.Controller;
import org.godea.di.Route;
import org.godea.services.DefaultService;

import java.io.IOException;

@Controller
public class DefaultController {
    @Autowired
    DefaultService defaultService;

    @Route(path = "/", method = "GET")
    public void index(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Claims claims = (Claims) req.getAttribute("claims");

        if(claims != null) {
            defaultService.getPage(req, resp, "/file.html");
        } else {
            defaultService.getPage(req, resp, "/register.html");
        }
    }
}
