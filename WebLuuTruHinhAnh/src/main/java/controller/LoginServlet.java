package controller;

import DAO.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        System.out.println("EMAIL: " + email);
        System.out.println("PASSWORD: " + password);

        UserDao userDao = new UserDao();

        User user = userDao.login(email, password);

        if (user == null) {

            System.out.println("LOGIN FAIL");

            request.setAttribute("error", "Sai tài khoản hoặc mật khẩu");

            request.getRequestDispatcher("/login.jsp")
                    .forward(request, response);

            return;
        }

        System.out.println("LOGIN SUCCESS");
        System.out.println("ROLE = " + user.getRole());

        HttpSession session = request.getSession();

        session.setAttribute("user", user);

        if ("ADMIN".equalsIgnoreCase(user.getRole())) {

            response.sendRedirect(
                    request.getContextPath() + "/admin/dashboard"
            );

        } else {

            response.sendRedirect(
                    request.getContextPath() + "/image"
            );
        }
    }
}