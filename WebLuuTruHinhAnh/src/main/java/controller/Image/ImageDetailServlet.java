package controller.Image;

import controller.service.ImageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Image;
import model.User;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import controller.service.UserService;

@WebServlet(name = "ImageDetailServlet", value = "/ImageDetail")
public class ImageDetailServlet extends HttpServlet {

    private final ImageService imageService = new ImageService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // =========================
        // 9.1.3 Kiểm tra session
        // =========================

        HttpSession session = request.getSession(false);

        User user =
                (session != null)
                        ? (User) session.getAttribute("user")
                        : null;

        // =========================
        // Luồng 9.2 - Session không hợp lệ
        // =========================

        if (user == null) {

            // 9.2.1 Phát hiện session không hợp lệ

            // 9.2.2 Redirect login.jsp
            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );
            return;
        }


        // =========================
        // 9.1.4 Đọc tham số id từ URL
        // =========================

        String idStr = request.getParameter("id");

        // =========================
        // Luồng 9.3 - id rỗng hoặc không hợp lệ
        // =========================

        if (idStr == null || idStr.isEmpty()) {

            // 9.3.1 Phát hiện id không hợp lệ

            // 9.3.2 Redirect /Photos
            response.sendRedirect(
                    request.getContextPath() + "/Photos"
            );
            return;
        }

        try {

            int id = Integer.parseInt(idStr);

            // =========================
            // 9.1.5 getImageById(id)
            // Controller -> ImageService -> ImageDao
            // =========================

            Image image =
                    imageService.getImageById(id);

            // =========================
            // 9.1.6 Kiểm tra image null
            // và quyền truy cập
            // =========================

            if (image == null) {

                // 9.4.1 Phát hiện ảnh không hợp lệ

                // 9.4.2 Redirect /Photos
                response.sendRedirect(
                        request.getContextPath() + "/Photos"
                );
                return;
            }

            // =========================
            // Chức năng mới:
            // Kiểm tra ảnh PRIVATE
            // Chỉ chủ sở hữu được xem
            // =========================

            if(image == null){
                response.sendRedirect(
                        request.getContextPath() + "/Photos");
                return;
            }

            if ("PRIVATE".equals(image.getVisibility())
                    && image.getUserId() != user.getId()) {

                response.sendRedirect(
                        request.getContextPath() + "/Photos");
                return;
            }

            // ==================================================
            // Chức năng mới 9.1.6.1
            // getUserById(image.userId)
            // Controller -> UserService -> UserDao
            // ==================================================

            User uploader =
                    userService.getUserById(
                            image.getUserId()
                    );

            // ==================================================
            // Chức năng mới 9.1.6.2
            // getImageIdsByUserId(userId)
            // Lấy danh sách ảnh của uploader
            // dùng cho Previous / Next
            // ==================================================

            List<Integer> imageIds =
                    imageService.getImageIdsByUserId(
                            image.getUserId()
                    );

            // Debug
            System.out.println(
                    "Current image = " + image.getId()
            );

            System.out.println(
                    "Owner = " + image.getUserId()
            );

            System.out.println(
                    "Image IDs = " + imageIds
            );

            // =========================
            // 9.1.7 setAttribute(...)
            // =========================

            request.setAttribute(
                    "image",
                    image
            );

            // 9.1.7.1 setAttribute(uploader)

            request.setAttribute(
                    "uploader",
                    uploader
            );

            // 9.1.7.2 setAttribute(imageIds)

            request.setAttribute(
                    "imageIds",
                    imageIds
            );

            request.setAttribute(
                    "activeTopNav",
                    "photos"
            );

            // =========================
            // 9.1.7.3 forward(detail.jsp)
            // =========================

            request.getRequestDispatcher("/detail.jsp")
                    .forward(request, response);

            // =========================
            // 9.1.8 detail.jsp Render
            // =========================
            //
            // Hiển thị:
            // - Ảnh kích thước lớn
            // - Tên file
            // - Người upload
            // - Avatar uploader
            // - Ngày upload
            // - Dung lượng ảnh
            // - Mô tả
            // - Download Count
            // - Nút Download
            // - Nút Previous
            // - Nút Next
            //
            // =========================

        } catch (NumberFormatException e) {

            // Luồng 9.3.1
            // id không phải số hợp lệ

            response.sendRedirect(
                    request.getContextPath() + "/Photos"
            );
        }
    }
}
