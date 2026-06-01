package controller.Album;

import controller.service.AlbumsService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "DeleteAlbum", value = "/DeleteAlbum")
public class DeleteAlbum extends HttpServlet {
    private final AlbumsService albumsService = new AlbumsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");

        try {
            // 1. BẢO MẬT: Lấy userId từ Session thay vì lấy từ parameter nguy hiểm
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"success\":false,\"message\":\"Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.\"}");
                return;
            }
            int uid = (int) session.getAttribute("userId");

            // 2. VALIDATION: Kiểm tra tham số đầu vào
            String albumidParam = request.getParameter("albumId");
            if (albumidParam == null || albumidParam.trim().isEmpty()) {
                response.getWriter().write("{\"success\":false,\"message\":\"ID album không hợp lệ.\"}");
                return;
            }
            int albumId = Integer.parseInt(albumidParam);

            // 3. XỬ LÝ NGHIỆP VỤ
            boolean ok = albumsService.deleteAlbum(uid, albumId);

            if (ok) {
                // Sửa lỗi copy-paste chữ "Create" cũ
                response.getWriter().write("{\"success\":true,\"message\":\"Xóa album thành công!\"}");
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"Album không tồn tại hoặc bạn không có quyền xóa.\"}");
            }

        } catch (NumberFormatException e) {
            // Bắt lỗi ép kiểu dữ liệu đầu vào (ví dụ định dạng id là chữ thay vì số)
            response.getWriter().write("{\"success\":false,\"message\":\"Định dạng ID không hợp lệ.\"}");
        } catch (Exception e) {
            // Tránh sập trắng trang hệ thống (lỗi 500)
            response.getWriter().write("{\"success\":false,\"message\":\"Lỗi hệ thống. Vui lòng thử lại sau.\"}");
        }
    }
}