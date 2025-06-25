package org.godea.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.godea.di.Autowired;
import org.godea.di.Controller;
import org.godea.di.Route;
import org.godea.services.RegisterService;

import java.io.IOException;

/*
Take all users from DB

String findAllUsers = "SELECT * from users";
                    ResultSet rs = statement.executeQuery(findAllUsers);
                    ResultSetMetaData md = rs.getMetaData();
                    int columnCount = md.getColumnCount();

                    while(rs.next()) {
                        StringBuilder row = new StringBuilder();
                        for(int i = 1; i <= columnCount; i++) {
                            String colName = md.getColumnName(i);
                            String colValue = rs.getString(i);
                            row.append(colName)
                                    .append("=")
                                    .append(colValue)
                                    .append(";     ");
                        }
                        System.out.println(row);
                    }
 */

@Controller(path = "/api")
//@WebServlet("/api/*")
//@ServletSecurity
public class RegisterController {
    @Autowired
    RegisterService registerService;

    @Route(path = "/register", method = "GET")
    public void getResponse(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        registerService.handleGet(req, resp);
    }

    @Route(path = "/register", method = "POST")
    public void postResponse(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        String path = req.getPathInfo();

        if (path == null) {
            path = "/api/register";
        }

        switch (method) {
//            case "GET" -> registerService.handleGet(path, req, resp);
            case "POST" -> registerService.handlePost(req, resp);
        }
    }
}
