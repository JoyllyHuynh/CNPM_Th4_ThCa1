package controller.Image;

import controller.service.ImageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Image;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "SortImageServlet", value = "/Photos")
public class SortImageServlet extends HttpServlet {

    private final ImageService imageService = new ImageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String sortBy = request.getParameter("sortBy");

        List<Image> images = imageService.getImagesSorted(userId, sortBy);

        request.setAttribute("images", images);
        request.setAttribute("currentSort", sortBy != null ? sortBy : "newest");
        request.setAttribute("activeTopNav", "photos");

        request.getRequestDispatcher("/image.jsp")
                .forward(request, response);
    }
}