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
        // Không sử dụng trong Use Case này
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

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

            // =================================================================
            // [10.1.6] doPost(request)
            // Controller (AddImage) tiếp nhận yêu cầu từ Giao diện Web
            // =================================================================

            // =================================================================
            // [10.1.7] Kiểm tra danh sách ảnh (ids) rỗng
            // Điều hướng xử lý theo khối alt (Danh sách ảnh rỗng vs Danh sách ảnh hợp lệ)
            // =================================================================
            if (ids.isEmpty()) {

                // --- NHÁNH [Danh sách ảnh rỗng] ---

                // =============================================================
                // [10.3.1] JSON (success: false, message: "Vui lòng chọn ít nhất một ảnh.")
                // Trả về thông điệp lỗi dạng JSON ngay lập tức cho Giao diện Web
                // =============================================================
                response.getWriter().write("{\"success\":false,\"message\":\"Vui lòng chọn ít nhất một ảnh.\"}");
                return;

            } else {

                // --- NHÁNH [Danh sách ảnh hợp lệ] ---

                // =============================================================
                // [10.1.8] addPhotosToAlbum(uid, albumId, ids)
                // Controller gọi sang lớp Service thực hiện nghiệp vụ lưu trữ dữ liệu
                // =============================================================
                boolean success = albumsService.addPhotosToAlbum(uid, albumId, ids);

                if (success) {
                    // =========================================================
                    // [10.1.12] JSON (success: true, message)
                    // Trả về kết quả JSON báo thêm ảnh thành công khi success = true
                    // =========================================================
                    response.getWriter().write("{\"success\":true,\"message\":\"Thêm ảnh vào album thành công!\"}");
                } else {
                    response.getWriter().write("{\"success\":false,\"message\":\"Thêm ảnh thất bại. Bạn không có quyền sở hữu album hoặc ảnh này.\"}");
                }
            }

        } catch (Exception e) {

            // --- KHỐI [alt] DƯỚI CÙNG: [Mục 8. Exceptions] Lỗi CSDL -> Hệ thống log ---

            // =================================================================
            // Ghi log lỗi hệ thống hoặc lỗi kết nối Cơ sở dữ liệu
            // [Ghi log cảnh báo [ERROR - SR-32]]
            // =================================================================
            System.err.println("[ERROR - SR-32] Lỗi trong quá trình thêm ảnh vào album: " + e.getMessage());

            // =================================================================
            // [10.8.1] JSON Error (Lỗi hệ thống)
            // Trả về mã lỗi hệ thống và JSON cảnh báo cho phía Client
            // =================================================================
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\":false,\"message\":\"Thêm ảnh thất bại, hệ thống gặp sự cố.\"}");
        }
    }
}