package controller.Admin;

import DAO.ImageDao;
import DAO.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends AdminBaseServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if(!checkAdmin(request,response)){
            return;
        }

        UserDao userDao = new UserDao();
        ImageDao imageDao = new ImageDao();

        int totalUsers = userDao.countUsers();

        int totalImages = imageDao.countImages();

        int deletedImages = imageDao.countDeletedImages();

        request.setAttribute("totalUsers", totalUsers);

        request.setAttribute("totalImages", totalImages);

        request.setAttribute("deletedImages", deletedImages);

        request.getRequestDispatcher("/Admin/dashboard.jsp")
                .forward(request,response);
    }
}