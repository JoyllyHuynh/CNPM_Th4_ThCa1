package controller.Admin;

import jakarta.servlet.http.*;
import model.User;

import java.io.IOException;

public class AdminBaseServlet extends HttpServlet {

    protected boolean checkAdmin(HttpServletRequest request,
                                 HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("user");

        if(user == null){

            response.sendRedirect("../login.jsp");

            return false;
        }

        if(!"ADMIN".equalsIgnoreCase(user.getRole())){

            response.sendRedirect("../home");

            return false;
        }

        return true;
    }
}