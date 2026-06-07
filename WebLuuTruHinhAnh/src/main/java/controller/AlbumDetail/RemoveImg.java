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
    ImagService imagService = new ImagService();

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

            // Kiểm tra tính hợp lệ dữ liệu đầu vào
            if (!json.containsKey("albumId") || json.isNull("albumId") || !json.containsKey("photoIds")) {
                response.getWriter().write("{\"success\":false,\"message\":\"Dữ liệu yêu cầu không hợp lệ.\"}");
                return;
            }

            // ĐÃ XÓA: Bỏ hoàn toàn đoạn code trùng lặp khai báo 'loggedInUser' và 'uid' tại đây để hết lỗi đỏ

            int albumId = json.getInt("albumId");

            JsonArray photoArray = json.getJsonArray("photoIds");
            List<Integer> photoIds = new ArrayList<>();

            if (photoArray != null) {
                for (int i = 0; i < photoArray.size(); i++) {
                    photoIds.add(photoArray.getInt(i));
                }
            }

            if (photoIds.isEmpty()) {
                response.getWriter().write("{\"success\":false,\"message\":\"Vui lòng chọn ít nhất một ảnh để xóa.\"}");
                return;
            }

            // =================================================================
            // [19.1.6] doPost(request) (albumId, photoIds)
            // Controller (RemoveImg) tiếp nhận yêu cầu từ Giao diện Web
            // =================================================================

            // =================================================================
            // [19.1.7] removePhotosFromAlbum(uid, albumId, photoIds)
            // Controller gọi xuống tầng Service xử lý kiểm tra quyền và nghiệp vụ gỡ ảnh
            // =================================================================
            boolean isRemoved = imagService.removePhotosFromAlbum(uid, albumId, photoIds);

            JsonObject result;
            if (isRemoved) {

                // --- NHÁNH [Là chủ sở hữu album] -> [Tồn tại mapping] ---

                // =============================================================
                // [19.1.12] JSON (success: true, message)
                // Phản hồi chuỗi JSON thành công hoàn toàn về Giao diện Web
                // =============================================================
                result = Json.createObjectBuilder()
                        .add("success", true)
                        .add("message", "Xóa ảnh khỏi album thành công.")
                        .build();
            } else {

                // --- NHÁNH [Không phải chủ sở hữu] HOẶC [Không tồn tại mapping] ---

                // =============================================================
                // [19.4.3] / [19.3.2] JSON (success: false, message)
                // Phản hồi lỗi thao tác thất bại về phía Client
                // =============================================================
                result = Json.createObjectBuilder()
                        .add("success", false)
                        .add("message", "Xóa ảnh thất bại. Bạn không có quyền hoặc liên kết ảnh không tồn tại.")
                        .build();
            }

            response.getWriter().write(result.toString());

        } catch (Exception e) {

            // --- KHỐI [alt] DƯỚI CÙNG: [Mục 8. Exceptions] Lỗi CSDL -> Hệ thống log ---

            // =================================================================
            // Ghi nhận lỗi kết nối hoặc lỗi cú pháp phát sinh từ tầng Cơ sở dữ liệu
            // [Ghi log cảnh báo [ERROR - SR-32]]
            // =================================================================
            System.err.println("[ERROR - SR-32] Lỗi khi thực hiện xóa ảnh khỏi album: " + e.getMessage());

            // =================================================================
            // [19.8.1] JSON Error (Lỗi hệ thống)
            // Trả trạng thái mã lỗi 500 cùng chuỗi thông báo lỗi hệ thống chung cho client
            // =================================================================
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject error = Json.createObjectBuilder()
                    .add("success", false)
                    .add("message", "Xóa ảnh thất bại do lỗi hệ thống. Vui lòng thử lại.")
                    .build();

            response.getWriter().write(error.toString());
        }
    }
}