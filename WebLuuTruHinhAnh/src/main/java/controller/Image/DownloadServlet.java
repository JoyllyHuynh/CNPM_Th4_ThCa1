package controller.Image;

import DAO.ImageDao;
import controller.service.ImageService;
import model.Image;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "DownloadServlet", value = "/DownloadServlet")
public class DownloadServlet extends HttpServlet {

    private final ImageService imageService = new ImageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");

        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);

                // LUỒNG: Controller -> Service -> DAO
                Image img = imageService.getImageById(id);

                if (img != null) {
                    String uploadDir = getServletContext().getRealPath("/uploads");

                    // Cách nối đường dẫn an toàn cho cả Windows và Linux
                    File file = new File(uploadDir, img.getFilePath());

                    if (file.exists()) {
                        String encodedFileName = URLEncoder.encode(img.getFileName(), StandardCharsets.UTF_8.toString())
                                .replaceAll("\\+", "%20");

                        response.setContentType("application/octet-stream");
                        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);
                        response.setContentLength((int) file.length());

                        try (FileInputStream in = new FileInputStream(file);
                             OutputStream out = response.getOutputStream()) {
                            byte[] buffer = new byte[8192];
                            int length;
                            while ((length = in.read(buffer)) > 0) {
                                out.write(buffer, 0, length);
                            }
                        }
                    } else {
                        response.sendError(404, "File không tồn tại trên server!");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}