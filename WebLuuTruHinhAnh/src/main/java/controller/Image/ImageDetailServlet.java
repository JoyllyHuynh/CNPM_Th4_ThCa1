package controller.Image;

import controller.service.ImageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Image;
import model.User;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import controller.service.UserService;

@WebServlet(name = "ImageDetailServlet", value = "/ImageDetail")
public class ImageDetailServlet extends HttpServlet {

    private final ImageService imageService = new ImageService();
    private final UserService userService = new UserService();

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

            if ("PRIVATE".equals(image.getVisibility())
                    && image.getUserId() != user.getId()) {

                response.sendRedirect(
                        request.getContextPath() + "/Photos"
                );
                return;
            }

            if(image == null){
                response.sendRedirect(request.getContextPath() + "/Photos");
                return;
            }

            User uploader = userService.getUserById(image.getUserId());

            List<Integer> imageIds =
                    imageService.getImageIdsByUserId(image.getUserId());

            System.out.println("Current image = " + image.getId());
            System.out.println("Owner = " + image.getUserId());
            System.out.println("Image IDs = " + imageIds);

            request.setAttribute("image", image);
            request.setAttribute("uploader", uploader);
            request.setAttribute("imageIds", imageIds);
            request.setAttribute("activeTopNav", "photos");
            request.getRequestDispatcher("/detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/Photos");
        }
    }
}
