package org.godea.services;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.godea.adapter.OffsetDateTimeAdapter;
import org.godea.di.Autowired;
import org.godea.di.Service;
import org.godea.interfaces.HtmlRenderer;
import org.godea.interfaces.JsonResponser;
import org.godea.models.dto.FileDTO;
import org.godea.models.dto.PageDTO;
import org.godea.repositories.FileRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class AdminService implements HtmlRenderer, JsonResponser {
    @Autowired
    FileRepository fileRepository;

    public void handleGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderHtml(req, resp, "/admin.html");
    }

    public void handleGetFiles(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int limit = Integer.parseInt(req.getParameter("limit"));
        int offset = Integer.parseInt(req.getParameter("offset"));

        PageDTO<FileDTO> fileDTO = fileRepository.viewAllFiles(limit, offset);
        try {
            generateResponse("application/json", "UTF-8", resp, fileDTO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Can't to load list to json");
        }
    }

    @Override
    public void renderHtml(HttpServletRequest req, HttpServletResponse resp, String path) throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, resp);
    }

    @Override
    public void generateResponse(String contentType, String characterEncoding, HttpServletResponse resp, Object element) throws IOException {
        Gson gson = new Gson().newBuilder()
                .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
                .create();
        String json = gson.toJson(element);
        resp.setContentType(contentType);
        resp.setCharacterEncoding(characterEncoding);
        resp.getWriter().write(json);
    }
}