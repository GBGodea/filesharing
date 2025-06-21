package org.godea.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.godea.di.Autowired;
import org.godea.di.InjectedServlet;
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

@WebServlet("/api/*")
@ServletSecurity
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
