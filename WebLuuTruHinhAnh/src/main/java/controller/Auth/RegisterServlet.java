package controller.Auth;

import controller.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {

    private final AuthService userService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");

        if (email == null || password == null || fullName == null ||
                email.isBlank() || password.isBlank() || fullName.isBlank()) {

            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        boolean success = userService.register(email.trim(), password, fullName.trim());

        if (success) {
            request.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Email đã tồn tại!");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}