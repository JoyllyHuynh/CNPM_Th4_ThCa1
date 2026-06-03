package controller.Album;

import controller.service.AlbumsService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "DeleteAlbum", value = "/DeleteAlbum")
public class DeleteAlbum extends HttpServlet {
    AlbumsService albumsService = new AlbumsService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");

        // [Bước 2.1.3 & 2.1.5] User: Nhấn nút "Xóa album" và Xác nhận thao tác xóa (từ UI, gửi request POST)
        // Xác thực người dùng (SR-27) bằng Session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.getWriter().write("{\"success\":false,\"message\":\"Không tìm thấy thông tin xác thực. Vui lòng đăng nhập lại.\"}");
            return;
        }
        model.User loggedInUser = (model.User) session.getAttribute("user");
        int uid = loggedInUser.getId();

        String albumid = request.getParameter("albumId");

        if (albumid == null || albumid.trim().isEmpty()) {
            response.getWriter().write("{\"success\":false,\"message\":\"Thiếu thông tin Album ID.\"}");
            return;
        }

        int albumId;
        try {
            albumId = Integer.parseInt(albumid);
        } catch (NumberFormatException e) {
            response.getWriter().write("{\"success\":false,\"message\":\"Định dạng ID không hợp lệ.\"}");
            return;
        }

        try {
            // Gọi service xử lý (Bao gồm các bước 2.1.2, 2.1.6 -> 2.1.9 trong DB Transaction)
            boolean ok = albumsService.deleteAlbum(uid, albumId);
            
            if (ok) {
                // [Bước 2.1.10] System: Cập nhật giao diện danh sách album (Trả về JSON thành công để UI xử lý)
                response.getWriter().write("{\"success\":true,\"message\":\"Xóa album thành công\"}");
            } else {
                // [Bước 2.4.3] System: Ghi log hành vi (SR-32)
                System.err.println("[WARN] User " + uid + " failed to delete album " + albumId + " (Album không tồn tại hoặc không có quyền).");
                // [Bước 2.3.2 & 2.4.2] System: Hiển thị thông báo: "Album không tồn tại" hoặc Từ chối thao tác
                response.getWriter().write("{\"success\":false,\"message\":\"Album không tồn tại hoặc bạn không có quyền xóa\"}");
                // [Bước 2.3.3] System: Dừng xử lý
            }
        } catch (Exception e) {
            // [8. Exceptions] System: Lỗi cơ sở dữ liệu -> Hệ thống rollback
            System.err.println("[ERROR] Database error khi xóa album: " + e.getMessage());
            response.getWriter().write("{\"success\":false,\"message\":\"Lỗi cơ sở dữ liệu. Vui lòng thử lại sau.\"}");
        }
    }
}