package org.godea.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.godea.di.Autowired;
import org.godea.di.Controller;
import org.godea.di.Route;
import org.godea.di.Secured;
import org.godea.services.FileService;

import java.io.*;

@Controller(path = "/api")
public class FileController {
    @Autowired
    FileService fileService;

    @Route(path = "/file", method = "GET")
    @Secured
    public void getFilePage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        fileService.handleGet(req, resp);
    }

    @Route(path = "/file/download", method = "GET")
    @Secured
    public void downloadFile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        fileService.handleDownload(req, resp);
    }

    @Route(path = "/upload", method = "POST")
    @Secured
    public void uploadFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        fileService.handleUpload(req, resp);
    }
}
