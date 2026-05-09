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

            // ===== READ photoIds =====
            JsonArray photoIdsJson = json.getJsonArray("photoIds");
            List<Integer> ids = new ArrayList<>();

            if (photoIdsJson != null) {
                for (int i = 0; i < photoIdsJson.size(); i++) {
                    ids.add(photoIdsJson.getInt(i));
                }
            }

            boolean ok = albumsService.addPhotosToAlbum(albumId, ids);

            JsonObject result = Json.createObjectBuilder()
                    .add("success", ok)
                    .build();

            response.getWriter().write(result.toString());

        } catch (Exception e) {
            e.printStackTrace();

            JsonObject error = Json.createObjectBuilder()
                    .add("success", false)
                    .add("message", "không thể thêm do bị trùng ảnh")
                    .build();

            response.getWriter().write(error.toString());
        }
    }
}