package controller.Album;

import controller.service.AlbumsService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Album; // Đảm bảo đã import model Album

import java.io.IOException;

@WebServlet(name = "CreateAlbum", value = "/CreateAlbum")
public class CreateAlbum extends HttpServlet {
    private final AlbumsService albumsService = new AlbumsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Luồng 7.5: Cancel action xử lý ở client, không cần xử lý ở đây
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");

        // [NÂNG CẤP BẢO MẬT]: Đẩy luồng [7.4] lên đầu tiên để chặn truy cập trái phép sớm nhất
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) { // Sử dụng "userId" đồng bộ với DeleteAlbum
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\":false,\"message\":\"Phiên làm việc hết hạn. Vui lòng đăng nhập lại.\"}");
            return;
        }
        int uid = (int) session.getAttribute("userId");

        // [Bước 7.1.1 -> 7.1.4]: Đọc tham số sau khi đã qua vòng bảo mật
        String albumName = request.getParameter("albumName");

        // [Bước 7.1.5 - NÂNG CẤP]: Validate & Sanitize input triệt để chống XSS
        if (albumName != null) {
            albumName = albumName.trim();
            // Sanitize: Loại bỏ các thẻ HTML để tránh Stored XSS
            albumName = albumName.replaceAll("<[^>]*>", "");
        }

        if (albumName == null || albumName.isEmpty()) {
            response.getWriter().write("{\"success\":false,\"message\":\"Tên album không được để trống.\"}");
            return;
        }

        if (albumName.length() > 100) {
            response.getWriter().write("{\"success\":false,\"message\":\"Tên album không được vượt quá 100 ký tự.\"}");
            return;
        }

        try {
            // [Bước 7.1.6 & 7.2.1]: Kiểm tra trùng tên theo User (SR-24)
            if (albumsService.isAlbumNameExist(uid, albumName)) {
                response.getWriter().write("{\"success\":false,\"message\":\"Tên album đã tồn tại. Vui lòng chọn tên khác.\"}");
                return;
            }

            // [Bước 7.1.7 & 7.1.8 - TỐI ƯU]: Gọi tạo album và nhận về dữ liệu chi tiết thay vì boolean
            // Giả sử hàm này xử lý Transaction lồng nhau ở DAO: tạo album -> lấy ID vừa tạo -> trả về đối tượng Album
            Album newAlbum = albumsService.createAlbumAndReturn(uid, albumName);

            if (newAlbum != null) {
                // [Bước 7.1.9 & 7.1.10 - TỐI ƯU UX]: Trả thêm dữ liệu album để Frontend render động không cần reload trang
                // Để đơn giản, bạn có thể dùng thư viện Gson/Jackson hoặc tự viết chuỗi JSON:
                response.getWriter().write("{\"success\":true,\"message\":\"Tạo album thành công!\",\"albumId\":" + newAlbum.getId() + "}");
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"Tạo album thất bại do lỗi hệ thống.\"}");
            }
        } catch (Exception e) {
            // [Bước 8. Exceptions]: Ghi log lỗi hệ thống (SR-32)
            System.err.println("[ERROR - SR-32] Database error khi tạo album: " + e.getMessage());
            response.getWriter().write("{\"success\":false,\"message\":\"Lỗi cơ sở dữ liệu. Vui lòng thử lại sau.\"}");
        }
    }
}