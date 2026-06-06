package controller.Admin;

import controller.service.ImageService;
import controller.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Image;
import model.User;

import java.io.IOException;

@WebServlet(name = "AdminImageDetailServlet", value = "/admin/image-detail")
public class AdminImageDetailServlet extends HttpServlet {

    private final ImageService imageService = new ImageService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/images");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Image image = imageService.getImageById(id);

            if(image == null){
                response.sendRedirect(request.getContextPath() + "/admin/images");
                return;
            }

            User uploader = userService.getUserById(image.getUserId());

            request.setAttribute("image", image);
            request.setAttribute("uploader", uploader);
            request.getRequestDispatcher("/Admin/image-detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/images");
        }
    }
}
