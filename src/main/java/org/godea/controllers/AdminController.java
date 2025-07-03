package org.godea.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.godea.di.Autowired;
import org.godea.di.Controller;
import org.godea.di.Route;
import org.godea.di.Secured;
import org.godea.services.AdminService;

import java.io.IOException;

@Controller(path = "/api")
public class AdminController {
    @Autowired
    AdminService adminService;

    @Route(path = "/admin", method = "GET")
    @Secured(role = "admin")
    public void getAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        adminService.handleGet(req, resp);
    }

    @Route(path = "/admin/files", method = "GET")
    @Secured(role = "admin")
    public void getFiles(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        adminService.handleGetFiles(req, resp);
    }
}
