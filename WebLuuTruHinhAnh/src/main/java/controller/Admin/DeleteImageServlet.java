package controller.Admin;

import DAO.ImageDao;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/admin/delete-image")
public class DeleteImageServlet extends AdminBaseServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        ImageDao imageDao = new ImageDao();

        imageDao.deleteImage(id);

        response.sendRedirect("images");
    }
}