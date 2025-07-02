package org.godea.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.godea.adapter.LocalDateTimeTypeAdapter;
import org.godea.di.Autowired;
import org.godea.di.Service;
import org.godea.interfaces.HtmlRenderer;
import org.godea.interfaces.JsonResponser;
import org.godea.models.FileRecord;
import org.godea.models.User;
import org.godea.models.dto.EmailDTO;
import org.godea.repositories.FileRepository;
import org.godea.repositories.UserRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService implements HtmlRenderer, JsonResponser {
    @Autowired
    UserRepository userRepository;
    @Autowired
    FileRepository fileRepository;
    private final Gson gson = new Gson();

    public void handleGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getMainPage(req, resp);
    }

    public void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        EmailDTO email = gson.fromJson(reader, EmailDTO.class);
        Optional<User> user = userRepository.findByEmail(email.getEmail());

        if (user.isEmpty()) {
            String message = "User not found";
            generateResponse("application/json", "UTF-8", resp, message);
        } else {
            generateResponse("application/json", "UTF-8", resp, user.get());
        }
    }

    public void getMainPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderHtml(req, resp, "./file.html");
    }

    public void handleDownload(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");

        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing file id");
            return;
        }
        UUID id;
        try {
            id = UUID.fromString(idParam);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid UUID format");
            return;
        }

        FileRecord rec;
        try {
            rec = handleGetById(id);
        } catch (RuntimeException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            return;
        }

        String uploadsDir = "D:\\Doczilla\\SwapFiles\\src\\main\\java\\org\\godea\\uploads";
        Path filePath = Paths.get(uploadsDir, rec.getStoredName());
        if (!Files.exists(filePath)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found on server");
            return;
        }

        if (LocalDateTime.now().isAfter(rec.getExpirationDate())) {
            fileRepository.updateIsExpired(true, rec.getId());
            rec.setExpired(true);
        }

        if (rec.getIsExpired()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Link is expired");
            return;
        }

        String contentType = rec.getContentType() != null
                ? rec.getContentType()
                : "application/octet-stream";
        resp.setContentType(contentType);
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + rec.getOriginalName() + "\"");

        try (
                InputStream in = Files.newInputStream(filePath);
                OutputStream out = resp.getOutputStream()
        ) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    public void handleUpload(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part filePart = req.getPart("file_name");
        String originalName = filePart.getSubmittedFileName();
        String ext = originalName.substring(originalName.lastIndexOf('.'));
        String uploadDir = "D:\\Doczilla\\SwapFiles\\src\\main\\java\\org\\godea\\uploads";

        File uploads = new File(uploadDir);
        if (!uploads.exists()) uploads.mkdirs();

        String storedName = UUID.randomUUID() + ext;
        File target = new File(uploads, storedName);
        try (
                InputStream in = filePart.getInputStream();
                OutputStream out = new FileOutputStream(target)
        ) {
            in.transferTo(out);
        }

        String expParam = req.getParameter("expiration_date");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime expirationDate = LocalDateTime.parse(expParam, formatter);

        FileRecord rec = saveMetadata(originalName, storedName, filePart.getContentType(), filePart.getSize(), expirationDate);
        generateResponseAdapted("application/json", "UTF-8", resp, rec);
    }

    public FileRecord saveMetadata(String originalName, String storedName, String contentType, long size, LocalDateTime expirationDate) {
        FileRecord record = new FileRecord(originalName, storedName, contentType, size, expirationDate);
        return fileRepository.save(record);
    }

    public FileRecord handleGetById(UUID id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found: " + id));
    }

    @Override
    public void renderHtml(HttpServletRequest req,
                           HttpServletResponse resp,
                           String path) throws ServletException, IOException {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        req.getRequestDispatcher(path).forward(req, resp);
    }

    @Override
    public void generateResponse(String contentType, String characterEncoding, HttpServletResponse resp, Object element) throws IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType(contentType);
        resp.setCharacterEncoding(characterEncoding);
        writer.write(gson.toJson(element));
    }

    @Override
    public void generateResponseAdapted(String contentType, String characterEncoding, HttpServletResponse resp, Object element) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
        String json = gson.toJson(element);
        resp.setContentType(contentType);
        resp.setCharacterEncoding(characterEncoding);
        resp.getWriter().write(json);
    }
}
