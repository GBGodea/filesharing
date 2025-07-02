package org.godea.interfaces;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface JsonResponser {
    void generateResponse(String contentType,
                          String characterEncoding,
                          HttpServletResponse resp,
                          Object element) throws IOException;

    default void generateResponseAdapted(String contentType,
                          String characterEncoding,
                          HttpServletResponse resp,
                          Object element) throws IOException {};
}
