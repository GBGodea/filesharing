package org.godea.controllers;

import org.godea.di.Controller;
import org.godea.di.Route;

@Controller(path = "/api")
public class AdminController {
    @Route(path = "/admin")
    public void getAdmin() {

    }
}
