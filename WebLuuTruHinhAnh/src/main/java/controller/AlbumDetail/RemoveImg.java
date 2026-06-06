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

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // ===== READ JSON BODY =====
            InputStream is = request.getInputStream();
            JsonReader jsonReader = Json.createReader(is);
            JsonObject json = jsonReader.readObject();

            // [Bước 19.1.2] Xác thực user bằng Session (SR-27)
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                JsonObject error = Json.createObjectBuilder()
                        .add("success", false)
                        .add("message", "Không tìm thấy thông tin xác thực. Vui lòng đăng nhập lại.")
                        .build();
                response.getWriter().write(error.toString());
                return;
            }
            model.User loggedInUser = (model.User) session.getAttribute("user");
            int uid = loggedInUser.getId();

            if (!json.containsKey("albumId") || json.isNull("albumId")) {
                throw new IllegalArgumentException("albumId is missing");
            }
            int albumId = json.getInt("albumId");

            JsonArray photoArray = json.getJsonArray("photoIds");
            List<Integer> photoIds = new ArrayList<>();

            if (photoArray != null) {
                for (int i = 0; i < photoArray.size(); i++) {
                    photoIds.add(photoArray.getInt(i));
                }
            }

            // Gọi service xử lý (Bao gồm các kiểm tra 19.1.2, 19.1.8 và xóa 19.1.9)
            String message = imagService.removePhotosFromAlbum(uid, albumId, photoIds);

            boolean success = message.contains("thành công");

            // [Bước 19.1.10, 19.3.2, 19.4.2] System: Trả kết quả JSON tương ứng để UI xử lý
            JsonObject result = Json.createObjectBuilder()
                    .add("success", success)
                    .add("message", message)
                    .build();

            response.getWriter().write(result.toString());

        } catch (Exception e) {
            // [Bước 8. Exceptions] System: Lỗi hệ thống -> Báo lỗi
            e.printStackTrace();

            JsonObject error = Json.createObjectBuilder()
                    .add("success", false)
                    .add("message", "Xóa ảnh thất bại do lỗi hệ thống. Vui lòng thử lại.")
                    .build();

            response.getWriter().write(error.toString());
        }
    }
}