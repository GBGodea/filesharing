package org.godea.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.godea.di.Autowired;
import org.godea.di.Controller;
import org.godea.di.Route;
import org.godea.di.Secured;
import org.godea.services.FilePageService;

import java.io.IOException;

@Controller(path = "/api")
@Secured
public class FilePageController {
    @Autowired
    FilePageService filePageService;

    @Route(path = "/file")
    public void getPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        filePageService.getPage(req, resp);
    }

    public void getUser() {

    }
}
