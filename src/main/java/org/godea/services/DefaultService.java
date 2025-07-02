package org.godea.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.godea.di.Service;
import org.godea.interfaces.HtmlRenderer;

import java.io.IOException;

@Service
public class DefaultService implements HtmlRenderer {
    public void getPage(HttpServletRequest req, HttpServletResponse resp, String path) throws ServletException, IOException {
        renderHtml(req, resp, path);
    }

    @Override
    public void renderHtml(HttpServletRequest req, HttpServletResponse resp, String path) throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, resp);
    }
}
