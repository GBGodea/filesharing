package org.godea.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.godea.di.Controller;
import org.godea.di.Route;

@Controller(path = "/api")
public class AuthenticationController {
    @Route(path = "/auth")
    public void authentication(HttpServletRequest req, HttpServletResponse resp) {

    }
}
