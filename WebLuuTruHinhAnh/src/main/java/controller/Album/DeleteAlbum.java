package controller.Album;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "DeleteAlbum", value = "/DeleteAlbum")
public class DeleteAlbum extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");

        // ============================================================
        // [2.1.3 & 2.1.5] User nhấn Xóa album và xác nhận thao tác
        // ============================================================

        // ============================================================
        // [2.1.2] Kiểm tra phiên đăng nhập (SR-27)
        // ============================================================
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Không tìm thấy thông tin xác thực. Vui lòng đăng nhập lại.\"}"
            );
            return;
        }

        model.User loggedInUser = (model.User) session.getAttribute("user");
        int uid = loggedInUser.getId();

        // ============================================================
        // [2.1.7] Nhận Album ID từ request
        // ============================================================
        String albumid = request.getParameter("albumId");

        // ============================================================
        // [2.3.1] Thiếu Album ID
        // ============================================================
        if (albumid == null || albumid.trim().isEmpty()) {
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Thiếu thông tin Album ID.\"}"
            );
            return;
        }

        int albumId;

        try {

            // ============================================================
            // [2.1.8] Kiểm tra định dạng Album ID
            // ============================================================
            albumId = Integer.parseInt(albumid);

        } catch (NumberFormatException e) {

            // ============================================================
            // [2.3.1] Album ID không hợp lệ
            // ============================================================
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Định dạng ID không hợp lệ.\"}"
            );
            return;
        }

        try {

            // ============================================================
            // [2.1.2, 2.1.6 -> 2.1.9]
            // Service + DAO kiểm tra:
            // - Album tồn tại?
            // - Album thuộc user hiện tại?
            // - Xóa Album
            // ============================================================
            boolean ok = albumsService.deleteAlbum(uid, albumId);

            if (ok) {

                // ============================================================
                // [2.1.10] Xóa thành công
                // ============================================================
                response.getWriter().write(
                        "{\"success\":true,\"message\":\"Xóa album thành công\"}"
                );

            } else {

                // ============================================================
                // [2.4.2] Không có quyền hoặc album không tồn tại
                // ============================================================
                System.err.println(
                        "[WARN] User " + uid +
                                " failed to delete album " + albumId +
                                " (Album không tồn tại hoặc không có quyền)."
                );

                response.getWriter().write(
                        "{\"success\":false,\"message\":\"Album không tồn tại hoặc bạn không có quyền xóa\"}"
                );
            }

        } catch (Exception e) {

            // ============================================================
            // [8. Exceptions]
            // Database lỗi -> rollback transaction
            // ============================================================
            System.err.println(
                    "[ERROR] Database error khi xóa album: "
                            + e.getMessage()
            );

            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Lỗi cơ sở dữ liệu. Vui lòng thử lại sau.\"}"
            );
        }
}