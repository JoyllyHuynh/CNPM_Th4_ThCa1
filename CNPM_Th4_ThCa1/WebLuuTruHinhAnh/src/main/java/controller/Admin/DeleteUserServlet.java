package controller.Admin;

import DAO.UserDao;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/admin/delete-user")
public class DeleteUserServlet extends AdminBaseServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        UserDao userDao = new UserDao();

        userDao.deleteUser(id);

        response.sendRedirect("users");
    }
}