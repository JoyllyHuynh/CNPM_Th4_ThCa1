package controller.Admin;

import DAO.UserDao;
import model.User;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/users")
public class UsersServlet extends AdminBaseServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if(!checkAdmin(request, response)){
            return;
        }

        UserDao userDao = new UserDao();

        List<User> users = userDao.getAllUsers();

        request.setAttribute("users", users);

        request.getRequestDispatcher("/Admin/users.jsp")
                .forward(request, response);
    }
}