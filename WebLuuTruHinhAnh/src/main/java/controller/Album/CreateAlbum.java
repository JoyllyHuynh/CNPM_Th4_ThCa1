package controller.Album;

import controller.service.AlbumsService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "CreateAlbum", value = "/CreateAlbum")
public class CreateAlbum extends HttpServlet {
    AlbumsService albumsService = new AlbumsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");

        // ============================================================
        // [7.4.1] Kiểm tra xác thực phiên đăng nhập
        // ============================================================
        HttpSession session = request.getSession(false);

        // [Luồng 7.4] System: Kiểm tra token + quyền truy cập (Dùng Session)
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {

            // ============================================================
            // [7.4.2] Từ chối yêu cầu do chưa đăng nhập
            // ============================================================
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            // ============================================================
            // [7.4.3] Trả thông báo yêu cầu đăng nhập lại
            // ============================================================
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Phiên làm việc hết hạn. Vui lòng đăng nhập lại.\"}"
            );
            return;
        }
        model.User loggedInUser = (model.User) session.getAttribute("user");
        int uid = loggedInUser.getId();

        // ============================================================
        // [7.1.3 -> 7.1.4]
        // Nhận dữ liệu từ form tạo album sau khi user nhấn "Lưu"
        // ============================================================
        String albumName = request.getParameter("albumName");

        // ============================================================
        // [7.1.5]
        // Validate & Sanitize dữ liệu đầu vào
        // ============================================================
        if (albumName != null) {
            albumName = albumName.trim(); // Sanitize: Loại bỏ khoảng trắng 2 đầu
        }

        // ============================================================
        // [7.3.1 -> 7.3.2]
        // Tên album rỗng
        // ============================================================
        if (albumName == null || albumName.isEmpty()) {
            // [Bước 7.3.1 & 7.3.2] System: Lỗi rỗng -> Báo lỗi
            response.getWriter().write("{\"success\":false,\"message\":\"Tên album không được để trống.\"}");
            return;
        }

        // ============================================================
        // [7.3.1 -> 7.3.2]
        // Tên album vượt quá giới hạn
        // ============================================================
        if (albumName.length() > 100) {
            // [Bước 7.3.1 & 7.3.2] System: Lỗi vượt quá 100 ký tự -> Báo lỗi
            response.getWriter().write("{\"success\":false,\"message\":\"Tên album không được vượt quá 100 ký tự.\"}");
            return;
        }

        try {

            // ============================================================
            // [7.1.6]
            // Kiểm tra album trùng tên trong phạm vi user
            // ============================================================
            if (albumsService.isAlbumNameExist(uid, albumName)) {

                // ============================================================
                // [7.2.1 -> 7.2.2]
                // Phát hiện album đã tồn tại
                // ============================================================
                response.getWriter().write(
                        "{\"success\":false,\"message\":\"Tên album đã tồn tại. Vui lòng chọn tên khác.\"}"
                );
                return;
            }

            // ============================================================
            // [7.1.7 -> 7.1.8]
            // Thực hiện tạo album
            // ============================================================
            boolean success = albumsService.createAlbum(uid, albumName);

            if (success) {

                // ============================================================
                // [7.1.9]
                // Trả kết quả tạo album thành công
                // ============================================================
                response.getWriter().write(
                        "{\"success\":true,\"message\":\"Tạo album thành công!\"}"
                );

                // ============================================================
                // [7.1.10]
                // Frontend nhận kết quả và cập nhật danh sách album
                // ============================================================

            } else {

                // ============================================================
                // [7.2.2]
                // Tạo album thất bại
                // ============================================================
                response.getWriter().write(
                        "{\"success\":false,\"message\":\"Tạo album thất bại.\"}"
                );
            }
        } catch (Exception e) {

            // ============================================================
            // [8.1]
            // Ghi log lỗi hệ thống / CSDL (SR-32)
            // ============================================================
            System.err.println(
                    "[ERROR - SR-32] Database error khi tạo album: "
                            + e.getMessage()
            );

            // ============================================================
            // [8.2]
            // Trả thông báo lỗi hệ thống
            // ============================================================
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Lỗi cơ sở dữ liệu. Vui lòng thử lại sau.\"}"
            );
        }
    }
}