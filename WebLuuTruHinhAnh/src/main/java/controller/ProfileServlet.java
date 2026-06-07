package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;

import java.io.IOException;

@WebServlet(name = "ProfileServlet", value = "/Profile")
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // [20.1.2] GET /Profile
        // [20.1.3] Kiểm tra session (getSession(false))
        HttpSession session = request.getSession(false);
        // [20.1.4] Trả về đối tượng User (đã lưu khi đăng nhập)
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        // [Luồng 20.2] Session không hợp lệ / chưa đăng nhập
        if (user == null) {
            // [20.2.1] Phát hiện session không hợp lệ
            // [20.2.2] Redirect /login.jsp
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // [20.1.5] setAttribute(activeTopNav=profile)
        request.setAttribute("activeTopNav", "profile");
        // [20.1.5] forward(profile.jsp)
        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }
}
