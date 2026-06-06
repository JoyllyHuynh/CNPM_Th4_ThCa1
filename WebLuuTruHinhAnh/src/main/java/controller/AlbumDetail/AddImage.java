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
    AlbumsService albumsService = new AlbumsService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (InputStream is = request.getInputStream();
             JsonReader jsonReader = Json.createReader(is)) {

            JsonObject json = jsonReader.readObject();

            // ===== SAFE READ albumId =====
            if (!json.containsKey("albumId") || json.isNull("albumId")) {
                throw new IllegalArgumentException("albumId is missing");
            }
            int albumId = json.getInt("albumId");

            // [Bước 10.1.6] Xác thực user bằng Session (SR-27)
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

            // ===== READ photoIds =====
            JsonArray photoIdsJson = json.getJsonArray("photoIds");
            List<Integer> ids = new ArrayList<>();

            if (photoIdsJson != null) {
                for (int i = 0; i < photoIdsJson.size(); i++) {
                    ids.add(photoIdsJson.getInt(i));
                }
            }

            // Gọi service xử lý (Chứa các bước 10.1.6 -> 10.1.8 và luồng 10.2, 10.3)
            String message = albumsService.addPhotosToAlbum(uid, albumId, ids);

            boolean success = message.contains("thành công");

            // [Bước 10.1.9, 10.2.2, 10.3.2] System: Trả kết quả và thông báo tương ứng
            JsonObject result = Json.createObjectBuilder()
                    .add("success", success)
                    .add("message", message)
                    .build();

            response.getWriter().write(result.toString());

        } catch (Exception e) {
            // [Bước 10.4] System: Lỗi hệ thống -> Báo lỗi
            e.printStackTrace();

            JsonObject error = Json.createObjectBuilder()
                    .add("success", false)
                    .add("message", "Thêm ảnh thất bại, vui lòng thử lại.")
                    .build();

            response.getWriter().write(error.toString());
        }
    }
}