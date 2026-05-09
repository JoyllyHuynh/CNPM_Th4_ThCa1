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
            // ===== READ JSON BODY (KHÔNG BufferedReader) =====
            InputStream is = request.getInputStream();
            JsonReader jsonReader = Json.createReader(is);
            JsonObject json = jsonReader.readObject();

            int albumId = json.getInt("albumId");

            JsonArray photoArray = json.getJsonArray("photoIds");
            List<Integer> photoIds = new ArrayList<>();

            for (int i = 0; i < photoArray.size(); i++) {
                photoIds.add(photoArray.getInt(i));
            }


            boolean success = imagService.removePhotosFromAlbum(albumId, photoIds);

            JsonObject result = Json.createObjectBuilder()
                    .add("success", success)
                    .build();

            response.getWriter().write(result.toString());

        } catch (Exception e) {
            e.printStackTrace();

            JsonObject error = Json.createObjectBuilder()
                    .add("success", false)
                    .add("message", e.getMessage())
                    .build();

            response.getWriter().write(error.toString());
        }
    }
}