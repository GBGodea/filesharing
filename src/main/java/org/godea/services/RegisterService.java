package org.godea.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.godea.di.Autowired;
import org.godea.di.Service;
import org.godea.interfaces.HtmlRenderer;
import org.godea.models.User;
import org.godea.repositories.UserRepository;

import java.io.IOException;
import java.util.UUID;

@Service
public class RegisterService implements HtmlRenderer {
    @Autowired
    UserRepository userRepository;

    public void handleGet(String path, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (path) {
            case "/register":
                getRegisterPage(req, resp);

                User user = new User();
                user.setId(UUID.randomUUID());
                user.setEmail("111@mail.ru");
                user.setPassword("passowrd");

//                UserRepository repository = new UserRepository();
                userRepository.save(user);
                System.out.println("USER CREATED");

//            case "/admin" -> getAdmins(req, resp);
        }
    }

    private void getRegisterPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderHtml(req, resp, "./register.html");
    }



//    private void getUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        renderHtml(req, resp, "/users/getuser.html");
//    }
//
//    private void getAdmins(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        renderHtml(req, resp, "/users/getadmins.html");
//    }

    @Override
    public void renderHtml(HttpServletRequest req,
                           HttpServletResponse resp,
                           String path) throws ServletException, IOException {
        if(!path.startsWith("/")) {
            path = "/" + path;
        }
        req.getRequestDispatcher(path).forward(req, resp);
    }
}
