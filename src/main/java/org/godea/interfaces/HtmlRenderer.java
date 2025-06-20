package org.godea.interfaces;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface HtmlRenderer {
    void renderHtml(HttpServletRequest req,
                    HttpServletResponse resp,
                    String path) throws ServletException, IOException;
}
