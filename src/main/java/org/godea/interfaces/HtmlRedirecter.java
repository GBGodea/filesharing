package org.godea.interfaces;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface HtmlRedirecter {
    void redirect(String pagePath, HttpServletResponse resp) throws IOException;
}
