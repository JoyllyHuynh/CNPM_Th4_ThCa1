package controller;

import DAO.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@MultipartConfig
@WebServlet("/UpdateProfile")
public class UpdateProfileServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        User currentUser =
                (User) session.getAttribute("user");

        if(currentUser == null){
            response.sendRedirect(
                    request.getContextPath() + "/login.jsp");
            return;
        }

        String fullName =
                request.getParameter("fullName");

        String email =
                request.getParameter("email");

        String currentPassword =
                request.getParameter("currentPassword");

        String newPassword =
                request.getParameter("newPassword");

        String confirmPassword =
                request.getParameter("confirmPassword");

        Part avatarPart =
                request.getPart("avatar");

        User existingUser = userDao.findByEmail(email);

        if (existingUser != null
                && existingUser.getId() != currentUser.getId()) {

            request.getSession().setAttribute(
                    "error",
                    "Email đã được sử dụng!");

            if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin/profile");
            } else {
                response.sendRedirect(request.getContextPath() + "/Profile");
            }

            return;
        }

        if(newPassword != null && !newPassword.isBlank()){

            if(!currentUser.getPassword().equals(currentPassword)){

                request.getSession().setAttribute(
                        "error",
                        "Mật khẩu hiện tại không đúng");

                if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/admin/profile");
                } else {
                    response.sendRedirect(request.getContextPath() + "/Profile");
                }

                return;
            }
        }

        if(newPassword != null
                && !newPassword.isBlank()) {

            if(!newPassword.equals(confirmPassword)) {

                request.getSession().setAttribute(
                        "error",
                        "Xác nhận mật khẩu không khớp");

                if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/admin/profile");
                } else {
                    response.sendRedirect(request.getContextPath() + "/Profile");
                }

                return;
            }
        }

        if(newPassword != null && !newPassword.isBlank()){

            userDao.updateProfileAndPassword(
                    currentUser.getId(),
                    fullName,
                    email,
                    newPassword
            );

        }else{

            userDao.updateProfile(
                    currentUser.getId(),
                    fullName,
                    email
            );
        }

        if(avatarPart != null &&
                avatarPart.getSize() > 0){

            String fileName =
                    Paths.get(
                                    avatarPart.getSubmittedFileName())
                            .getFileName()
                            .toString();

            String avatarName =
                    "avatar_" +
                            currentUser.getId() +
                            "_" +
                            System.currentTimeMillis() +
                            "_" +
                            fileName;

            String uploadPath =
                    getServletContext()
                            .getRealPath("/uploads/avatar");

            File dir = new File(uploadPath);

            if(!dir.exists()){
                dir.mkdirs();
            }

            avatarPart.write(
                    uploadPath +
                            File.separator +
                            avatarName);

            userDao.updateAvatar(
                    currentUser.getId(),
                    avatarName);

            currentUser.setAvatar(
                    avatarName);
        }

        // cập nhật session
        currentUser.setFullName(fullName);
        currentUser.setEmail(email);

        session.setAttribute("user", currentUser);

        if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/admin/profile");
        } else {
            response.sendRedirect(request.getContextPath() + "/Profile");
        }
    }
}
