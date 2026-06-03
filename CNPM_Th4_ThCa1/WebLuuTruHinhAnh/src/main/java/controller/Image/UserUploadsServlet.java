package controller.Image;

import controller.service.ImageService;
import DAO.UserDao; // Gọi trực tiếp tầng DAO của bạn
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Image;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserUploadsServlet", value = "/UserUploads")
public class UserUploadsServlet extends HttpServlet {

    private final ImageService imageService = new ImageService();
    private final UserDao userDao = new UserDao(); // Khởi tạo trực tiếp UserDao thay cho UserService

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String userIdStr = request.getParameter("userId");
        if (userIdStr == null || userIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/Photos");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdStr);

            // Tìm thông tin uploader:
            // Tạm thời lấy thông tin currentUser nếu trùng ID, hoặc bạn có thể bổ sung hàm getUserById(userId) vào UserDao sau.
            User uploader = (currentUser.getId() == userId) ? currentUser : null;

            // Lấy danh sách ảnh từ JDBI
            List<Image> userImages = imageService.getImagesByUserId(userId);

            request.setAttribute("uploader", uploader);
            request.setAttribute("userId", userId); // Gửi thêm cái này dự phòng ra JSP
            request.setAttribute("userImages", userImages);
            request.setAttribute("activeTopNav", "photos");

            request.getRequestDispatcher("/userProfile.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/Photos");
        }
    }
}