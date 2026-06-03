package controller.Admin;

import DAO.UserDao;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/admin/lock-user")
public class LockUserServlet extends AdminBaseServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if(!checkAdmin(request, response)){
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        UserDao userDao = new UserDao();

        userDao.updateStatus(id, "BANNED");

        response.sendRedirect("users");
    }
}
