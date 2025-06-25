package org.godea.services;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.godea.di.Autowired;
import org.godea.di.Service;
import org.godea.interfaces.HtmlRenderer;
import org.godea.interfaces.JsonResponser;
import org.godea.models.User;
import org.godea.models.dto.EmailDTO;
import org.godea.repositories.UserRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@Service
public class MainPageService implements HtmlRenderer, JsonResponser {
    @Autowired
    UserRepository userRepository;
    private Gson gson = new Gson();

    public void handleGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getMainPage(req, resp);
    }

    public void handlePost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        EmailDTO email = gson.fromJson(reader, EmailDTO.class);
        Optional<User> user = userRepository.findByEmail(email.getEmail());

        if(user.isEmpty()) {
            String message = "User not found";
            generateResponse("application/json", "UTF-8", resp, message);
        } else {
            generateResponse("application/json", "UTF-8", resp, user.get());
        }
    }

    public void getMainPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderHtml(req, resp, "./main.html");
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
}
