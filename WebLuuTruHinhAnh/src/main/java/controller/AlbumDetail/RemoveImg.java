package controller.AlbumDetail;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import controller.service.ImagService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "RemoveImg", value = "/RemoveImg")
public class RemoveImg extends HttpServlet {
    private final ImagService imagService = new ImagService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // [MỤC BẢO MẬT]: Đẩy xác thực phiên làm việc lên đầu tiên theo đúng luồng hệ thống
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            JsonObject error = Json.createObjectBuilder()
                    .add("success", false)
                    .add("message", "Không tìm thấy thông tin xác thực. Vui lòng đăng nhập lại.")
                    .build();
            response.getWriter().write(error.toString());
            return;
        }
        model.User loggedInUser = (model.User) session.getAttribute("user");
        int uid = loggedInUser.getId();

        try (InputStream is = request.getInputStream();
             JsonReader jsonReader = Json.createReader(is)) {

            JsonObject json = jsonReader.readObject();

            // Kiểm tra sự tồn tại của các cấu trúc dữ liệu bắt buộc
            if (!json.containsKey("albumId") || json.isNull("albumId") || !json.containsKey("photoIds")) {
                response.getWriter().write("{\"success\":false,\"message\":\"Dữ liệu yêu cầu không hợp lệ.\"}");
                return;
            }

            int albumId = json.getInt("albumId");
            JsonArray photoArray = json.getJsonArray("photoIds");
            List<Integer> photoIds = new ArrayList<>();

            if (photoArray != null) {
                for (int i = 0; i < photoArray.size(); i++) {
                    photoIds.add(photoArray.getInt(i));
                }
            }

            // [Luồng 10.3 / 19.3]: Đánh chặn ngay tại Controller nếu mảng đầu vào trống rỗng
            if (photoIds.isEmpty()) {
                response.getWriter().write("{\"success\":false,\"message\":\"Vui lòng chọn ít nhất một ảnh để xóa.\"}");
                return;
            }

            // Gọi xuống tầng Service để thực thi nghiệp vụ (Tránh việc kiểm tra chuỗi String)
            boolean isRemoved = imagService.removePhotosFromAlbum(uid, albumId, photoIds);

            JsonObject result;
            if (isRemoved) {
                // [Bước 19.1.9 & 19.1.10]: Trả kết quả thành công về để FE cập nhật lại giao diện hiển thị
                result = Json.createObjectBuilder()
                        .add("success", true)
                        .add("message", "Xóa ảnh khỏi album thành công.")
                        .build();
            } else {
                // [Luồng 19.3 & 19.4]: Từ chối thao tác do không tìm thấy bản ghi hoặc sai quyền sở hữu
                result = Json.createObjectBuilder()
                        .add("success", false)
                        .add("message", "Xóa ảnh thất bại. Bạn không có quyền sở hữu album hoặc liên kết ảnh không tồn tại.")
                        .build();
            }

            response.getWriter().write(result.toString());

        } catch (Exception e) {
            // [Bước 8. Exceptions / Luồng 10.4]: Xử lý ngoại lệ, ghi lại vết lỗi hệ thống
            System.err.println("[ERROR - SYSTEM] Lỗi khi thực hiện xóa ảnh khỏi album: " + e.getMessage());
            JsonObject error = Json.createObjectBuilder()
                    .add("success", false)
                    .add("message", "Xóa ảnh thất bại do lỗi hệ thống. Vui lòng thử lại.")
                    .build();
            response.getWriter().write(error.toString());
        }
    }
}