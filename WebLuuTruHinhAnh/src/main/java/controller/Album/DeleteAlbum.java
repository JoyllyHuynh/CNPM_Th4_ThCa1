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

        // ============================================================
        // [2.1.3 & 2.1.5 trên UI] User nhấn Xóa album và xác nhận thao tác
        // ============================================================

        // ============================================================
        // Kiểm tra phiên đăng nhập (SR-27)
        // ============================================================
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.getWriter().write("{\"success\":false,\"message\":\"Không tìm thấy thông tin xác thực. Vui lòng đăng nhập lại.\"}");
            return;
        }
        model.User loggedInUser = (model.User) session.getAttribute("user");
        int uid = loggedInUser.getId();

        // ============================================================
        // [2.1.4] doPost(request)
        // Controller tiếp nhận request từ Giao diện Web gửi về
        // ============================================================
        String albumid = request.getParameter("albumId");

        if (albumid == null || albumid.trim().isEmpty()) {
            response.getWriter().write("{\"success\":false,\"message\":\"Thiếu thông tin Album ID.\"}");
            return;
        }

        int albumId;
        try {
            albumId = Integer.parseInt(albumid);
        } catch (NumberFormatException e) {
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Định dạng ID không hợp lệ.\"}"
            );
            return;
        }

        try {
            // ============================================================
            // [2.1.5] deleteAlbum(uid, albumId)
            // Controller gọi xuống tầng Service để xử lý nghiệp vụ xóa
            // ============================================================
            boolean ok = albumsService.deleteAlbum(uid, albumId);
            
            if (ok) {
                // --- NHÁNH [rows > 0] (Xóa thành công) ---

                // ============================================================
                // [2.1.9] JSON (success: true)
                // Controller trả về chuỗi JSON thông báo thành công cho UI
                // ============================================================
                response.getWriter().write(
                        "{\"success\":true,\"message\":\"Xóa album thành công\"}"
                );
            } else {
                // --- NHÁNH [rows = 0] (Album không tồn tại hoặc không có quyền) ---

                System.err.println(
                        "[WARN] User " + uid +
                                " failed to delete album " + albumId +
                                " (Album không tồn tại hoặc không có quyền)."
                );

                // ============================================================
                // [2.1.9] JSON (success: false)
                // Controller trả về chuỗi JSON thông báo thất bại cho UI
                // ============================================================
                response.getWriter().write(
                        "{\"success\":false,\"message\":\"Album không tồn tại hoặc bạn không có quyền xóa\"}"
                );
            }
        } catch (Exception e) {
            // --- KHỐI [alt] DƯỚI CÙNG: [Mục 8. Exceptions] Lỗi CSDL -> Hệ thống log ---

            // ============================================================
            // Ghi log cảnh báo hệ thống ra console/file log
            // [Ghi log cảnh báo [ERROR - SR-32]]
            // ============================================================
            System.err.println(
                    "[ERROR - SR-32] Lỗi hệ thống/CSDL khi xóa album: "
                            + e.getMessage()
            );

            // ============================================================
            // [2.8.1] JSON Error (Lỗi hệ thống)
            // Controller trả về chuỗi JSON thông báo lỗi hệ thống cho giao diện
            // ============================================================
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Lỗi cơ sở dữ liệu. Vui lòng thử lại sau.\"}"
            );
        }
    }
}