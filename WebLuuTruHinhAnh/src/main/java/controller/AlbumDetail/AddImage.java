package controller.AlbumDetail;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import controller.service.AlbumsService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AddImage", value = "/add-photos")
public class AddImage extends HttpServlet {
    private final AlbumsService albumsService = new AlbumsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // [MỤC BẢO MẬT]: Đẩy xác thực phiên làm việc lên đầu trang theo đúng luồng Sequence
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\":false,\"message\":\"Không tìm thấy thông tin xác thực. Vui lòng đăng nhập lại.\"}");
            return;
        }
        model.User loggedInUser = (model.User) session.getAttribute("user");
        int uid = loggedInUser.getId();

        try (InputStream is = request.getInputStream();
             JsonReader jsonReader = Json.createReader(is)) {

            JsonObject json = jsonReader.readObject();

            // Validate sự tồn tại của trường dữ liệu đầu vào
            if (!json.containsKey("albumId") || json.isNull("albumId") || !json.containsKey("photoIds")) {
                response.getWriter().write("{\"success\":false,\"message\":\"Dữ liệu yêu cầu không hợp lệ.\"}");
                return;
            }

            int albumId = json.getInt("albumId");
            JsonArray photoIdsJson = json.getJsonArray("photoIds");
            List<Integer> ids = new ArrayList<>();

            if (photoIdsJson != null) {
                for (int i = 0; i < photoIdsJson.size(); i++) {
                    ids.add(photoIdsJson.getInt(i));
                }
            }

            // [Luồng 10.3]: Đánh chặn ngay tại Controller nếu mảng ID rỗng, không gửi xuống DB
            if (ids.isEmpty()) {
                response.getWriter().write("{\"success\":false,\"message\":\"Vui lòng chọn ít nhất một ảnh.\"}");
                return;
            }

            // Gọi xuống Service xử lý và nhận về kết quả dạng Trạng thái/Thông điệp rõ ràng
            // Tách cấu trúc logic: Service sẽ lo việc bọc DB Transaction (Luồng 10.4)
            boolean success = albumsService.addPhotosToAlbum(uid, albumId, ids);

            if (success) {
                response.getWriter().write("{\"success\":true,\"message\":\"Thêm ảnh vào album thành công!\"}");
            } else {
                response.getWriter().write("{\"success\":false,\"message\":\"Thêm ảnh thất bại. Bạn không có quyền sở hữu album hoặc ảnh này.\"}");
            }

        } catch (Exception e) {
            // [Luồng 10.4.1 -> 10.4.3]: Bắt lỗi Exception hệ thống
            System.err.println("[ERROR - SYSTEM] Lỗi trong quá trình thêm ảnh vào album: " + e.getMessage());
            response.getWriter().write("{\"success\":false,\"message\":\"Thêm ảnh thất bại, hệ thống gặp sự cố.\"}");
        }
    }
}