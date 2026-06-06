package controller.Album;

import controller.service.AlbumsService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "CreateAlbum", value = "/CreateAlbum")
public class CreateAlbum extends HttpServlet {
    private final AlbumsService albumsService = new AlbumsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Luồng 7.5: Cancel action xử lý ở client, không cần xử lý ở đây
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");

        // ============================================================
        // [7.4] Kiểm tra xác thực phiên đăng nhập
        // ============================================================
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Phiên làm việc hết hạn. Vui lòng đăng nhập lại.\"}"
            );
            return;
        }

        model.User loggedInUser = (model.User) session.getAttribute("user");
        int uid = loggedInUser.getId();

        // ============================================================
        // [7.1.1 -> 7.1.4] Nhận dữ liệu từ giao diện
        // ============================================================
        String albumName = request.getParameter("albumName");

        // ============================================================
        // [7.1.5] Validate & Sanitize dữ liệu đầu vào
        // ============================================================
        if (albumName != null) {
            albumName = albumName.trim();
            albumName = albumName.replaceAll("<[^>]*>", "");
        }

        if (albumName == null || albumName.isEmpty()) {
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Tên album không được để trống.\"}"
            );
            return;
        }

        if (albumName.length() > 100) {
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Tên album không được vượt quá 100 ký tự.\"}"
            );
            return;
        }

        try {

            // ============================================================
            // [7.2.1] Kiểm tra album trùng tên
            // ============================================================
            if (albumsService.isAlbumNameExist(uid, albumName)) {
                response.getWriter().write(
                        "{\"success\":false,\"message\":\"Tên album đã tồn tại. Vui lòng chọn tên khác.\"}"
                );
                return;
            }

            // ============================================================
            // [7.1.6 -> 7.1.8] Tạo album
            // ============================================================
            boolean success = albumsService.createAlbum(uid, albumName);

            if (success) {

                // ============================================================
                // [7.1.9 -> 7.1.10] Thông báo tạo thành công
                // ============================================================
                response.getWriter().write(
                        "{\"success\":true,\"message\":\"Tạo album thành công!\"}"
                );

            } else {

                // ============================================================
                // [7.2.2] Tạo album thất bại
                // ============================================================
                response.getWriter().write(
                        "{\"success\":false,\"message\":\"Tạo album thất bại.\"}"
                );
            }

        } catch (Exception e) {

            // ============================================================
            // [8. Exceptions] Lỗi hệ thống / Database
            // ============================================================
            System.err.println(
                    "[ERROR - SR-32] Database error khi tạo album: "
                            + e.getMessage()
            );

            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Lỗi cơ sở dữ liệu. Vui lòng thử lại sau.\"}"
            );
        }
    }
}