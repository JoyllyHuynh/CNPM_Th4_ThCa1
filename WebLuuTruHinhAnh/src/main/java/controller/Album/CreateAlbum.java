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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");

        // [Bước 7.1.1 -> 7.1.4] User: Chọn tạo, nhập form và nhấn Lưu (gửi POST)
        String albumName = request.getParameter("albumName");

        // [Luồng 7.4] System: Kiểm tra token + quyền truy cập (Dùng Session)
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.getWriter().write("{\"success\":false,\"message\":\"Không tìm thấy thông tin xác thực. Vui lòng đăng nhập lại.\"}");
            return;
        }
        model.User loggedInUser = (model.User) session.getAttribute("user");
        int uid = loggedInUser.getId();

        // [Bước 7.1.5] System: Validate input (SR-07: format, length) & Sanitize input
        if (albumName != null) {
            albumName = albumName.trim(); // Sanitize: Loại bỏ khoảng trắng 2 đầu
        }
        
        if (albumName == null || albumName.isEmpty()) {
            // [Bước 7.3.1 & 7.3.2] System: Lỗi rỗng -> Báo lỗi
            response.getWriter().write("{\"success\":false,\"message\":\"Tên album không được để trống.\"}");
            return;
        }
        
        if (albumName.length() > 100) {
            // [Bước 7.3.1 & 7.3.2] System: Lỗi vượt quá 100 ký tự -> Báo lỗi
            response.getWriter().write("{\"success\":false,\"message\":\"Tên album không được vượt quá 100 ký tự.\"}");
            return;
        }

        try {
            // [Bước 7.1.6 & 7.2.1] System: Kiểm tra album name không trùng theo user (SR-24)
            if (albumsService.isAlbumNameExist(uid, albumName)) {
                // [Bước 7.2.2] System: Phát hiện trùng tên -> Hiển thị lỗi
                response.getWriter().write("{\"success\":false,\"message\":\"Tên album đã tồn tại. Vui lòng chọn tên khác.\"}");
                return;
            }

            // [Bước 7.1.7 & 7.1.8] System: Tạo album record trong database (và khởi tạo quan hệ qua ID)
            boolean ok = albumsService.createAlbum(uid, albumName);
            
            if (ok) {
                // [Bước 7.1.9 & 7.1.10] System: Trả kết quả thành công và UI cập nhật danh sách
                response.getWriter().write("{\"success\":true,\"message\":\"Tạo album thành công\"}");
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"Tạo album thất bại do lỗi hệ thống.\"}");
            }
        } catch (Exception e) {
            // [Bước 8. Exceptions] System: Database error -> log SR-32
            System.err.println("[ERROR - SR-32] Database error khi tạo album: " + e.getMessage());
            response.getWriter().write("{\"success\":false,\"message\":\"Lỗi cơ sở dữ liệu. Vui lòng thử lại sau.\"}");
        }
    }
}