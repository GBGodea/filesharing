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
import org.godea.models.dto.UserDTO;
import org.godea.repositories.UserRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@Service
public class RegisterService implements HtmlRenderer, JsonResponser {
    @Autowired
    UserRepository userRepository;
    private Gson gson = new Gson();

    public void handleGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            getRegisterPage(req, resp);
    }

    public void handlePost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        User user = gson.fromJson(reader, User.class);
        userRepository.save(user);
        generateResponse("application/json",
                "UTF-8",
                resp,
                new UserDTO(user.getId(), user.getEmail()));
    }

    private void getRegisterPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderHtml(req, resp, "./register.html");
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
    public void generateResponse(String contentType,
                                 String characterEncoding,
                                 HttpServletResponse resp,
                                 Object element) throws IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType(contentType);
        resp.setCharacterEncoding(characterEncoding);
        writer.write(gson.toJson(element));
    }
}
