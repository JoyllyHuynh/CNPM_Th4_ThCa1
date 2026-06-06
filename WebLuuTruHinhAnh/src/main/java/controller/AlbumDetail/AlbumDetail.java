package controller.AlbumDetail;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Album;
import model.Imagee;
import controller.service.AlbumsService;
import controller.service.ImagService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "Album_detail", value = "/Album_detail")
public class AlbumDetail extends HttpServlet {
    private final ImagService imagService = new ImagService();
    private final AlbumsService albumsService = new AlbumsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy session hiện tại, không tự động tạo mới nếu không tồn tại
        HttpSession session = request.getSession(false);

        // 1. Kiểm tra xác thực phiên đăng nhập đồng bộ bằng key "user"
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Lấy đối tượng User từ Session và trích xuất Id thực tế của họ
        model.User loggedInUser = (model.User) session.getAttribute("user");
        int uid = loggedInUser.getId();

        try {
            String albumIdParam = request.getParameter("aid");
            if (albumIdParam == null || albumIdParam.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu ID Album.");
                return;
            }
            int aid = Integer.parseInt(albumIdParam);

            // 2. BẢO MẬT: Lấy thông tin Album dựa trên aid và uid của User đang đăng nhập
            Album album = albumsService.getAlbumByOwner(aid, uid);
            if (album == null) {
                // Nếu Album không tồn tại hoặc không thuộc quyền sở hữu -> Trả về lỗi 403 Forbidden chặn đứng hack URL
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập hoặc album không tồn tại.");
                return;
            }

            // 3. Lấy dữ liệu danh sách ảnh hiển thị trong thư viện và kho ảnh cá nhân
            List<Imagee> imageList = imagService.getListImage(uid, aid);
            List<Imagee> imageListOfUser = imagService.getListImageOfUser(uid);

            request.setAttribute("album", album);
            request.setAttribute("imageList", imageList);
            request.setAttribute("imageListOfUser", imageListOfUser);
            request.setAttribute("userId", uid);
            request.setAttribute("activeTopNav", "albums");

            request.getRequestDispatcher("user/album-detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Định dạng ID không hợp lệ.");
        }
    }
}