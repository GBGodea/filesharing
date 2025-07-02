package org.godea.services;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.godea.di.Autowired;
import org.godea.di.Service;
import org.godea.interfaces.HtmlRenderer;
import org.godea.interfaces.JsonResponser;
import org.godea.models.User;
import org.godea.models.dto.UserDTO;
import org.godea.models.dto.UserLoginDTO;
import org.godea.repositories.UserRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@Service
public class AuthService implements HtmlRenderer, JsonResponser {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUtilService jwtUtilService;
    private final Gson gson = new Gson();

    public void handleGetLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderHtml(req, resp, "/login.html");
    }

    public void handlePostLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        BufferedReader reader = req.getReader();
        UserLoginDTO dto = gson.fromJson(reader, UserLoginDTO.class);

        Optional<User> userOpt = userRepository.findByEmail(dto.getEmail());
        if(userOpt.isEmpty() || !userOpt.get().getPassword().equals(dto.getPassword())) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials");
            return ;
        }

        User user = userOpt.get();

        String token = jwtUtilService.generateToken(user);
        var cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 3600);
        resp.addCookie(cookie);

        UserDTO response = new UserDTO(user.getId(), user.getEmail(), user.getRole().getRole().name());
        generateResponse("application/json", "UTF-8", resp, response);
    }

    @Override
    public void renderHtml(HttpServletRequest req, HttpServletResponse resp, String path) throws ServletException, IOException {
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
