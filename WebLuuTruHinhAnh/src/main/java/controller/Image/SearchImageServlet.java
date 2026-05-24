package controller.Image;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Image;
import controller.service.ImageService;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchImageServlet", value = "/search")
public class SearchImageServlet extends HttpServlet {

    private final ImageService imageService = new ImageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        Integer userId = user.getId();

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String keyword = request.getParameter("keyword");

        List<Image> images;

        if (keyword != null && !keyword.trim().isEmpty()) {
            images = imageService.searchByKW(userId, keyword.trim());
            request.setAttribute("searchKeyword", keyword.trim());
        } else {
            images = List.of();
        }

        request.setAttribute("images", images);
        request.setAttribute("isSearchResult", true);
        request.setAttribute("activeTopNav", "photos");

        request.getRequestDispatcher("image.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}