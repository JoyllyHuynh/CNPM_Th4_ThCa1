package controller.Image;

import controller.service.ImageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Image;
import model.User;

import java.io.IOException;

@WebServlet(name = "ImageDetailServlet", value = "/ImageDetail")
public class ImageDetailServlet extends HttpServlet {

    private final ImageService imageService = new ImageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/Photos");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Image image = imageService.getImageById(id);

            if (image == null || image.getUserId() != user.getId()) {
                // Không tìm thấy hoặc không phải ảnh của user này
                response.sendRedirect(request.getContextPath() + "/Photos");
                return;
            }

            request.setAttribute("image", image);
            request.setAttribute("activeTopNav", "photos");
            request.getRequestDispatcher("/detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/Photos");
        }
    }
}
