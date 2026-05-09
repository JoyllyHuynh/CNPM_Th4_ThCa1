package controller.Admin;

import DAO.ImageDao;
import model.Image;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/images")
public class ImagesServlet extends AdminBaseServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        ImageDao imageDao = new ImageDao();

        List<Image> images = imageDao.getAllImages();

        request.setAttribute("images", images);

        request.getRequestDispatcher("/Admin/images.jsp")
                .forward(request, response);
    }
}