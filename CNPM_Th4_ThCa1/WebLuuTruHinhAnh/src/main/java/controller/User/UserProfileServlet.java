package controller.User;

import controller.service.ImageService;
import controller.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Image;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet("/UserProfile")
public class UserProfileServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final ImageService imageService = new ImageService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if(idParam == null || idParam.isEmpty()){
            response.sendRedirect(request.getContextPath() + "/Photos");
            return;
        }

        try{

            int userId = Integer.parseInt(idParam);

            User profileUser =
                    userService.getUserById(userId);

            if(profileUser == null){
                response.sendRedirect(request.getContextPath() + "/Photos");
                return;
            }

            List<Image> images =
                    imageService.getImagesByUserId(userId);

            request.setAttribute("profileUser", profileUser);
            request.setAttribute("images", images);

            request.getRequestDispatcher("/user/userProfile.jsp")
                    .forward(request,response);

        }catch (Exception e){
            response.sendRedirect(request.getContextPath() + "/Photos");
        }
    }
}