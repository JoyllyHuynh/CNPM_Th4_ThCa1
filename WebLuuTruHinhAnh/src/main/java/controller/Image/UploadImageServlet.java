package controller.Image;

import controller.service.ImageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Image;
import model.User;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@WebServlet(name = "UploadImageServlet", value = "/UploadImage")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,    // 1 MB
        maxFileSize      = 1024 * 1024 * 10, // 10 MB
        maxRequestSize   = 1024 * 1024 * 50  // 50 MB
)
public class UploadImageServlet extends HttpServlet {

    private final ImageService imageService = new ImageService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int userId = user.getId();

        // Tạo thư mục uploads nếu chưa có
        String uploadDir = getServletContext().getRealPath("/uploads");
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        String description = request.getParameter("description");

        for (Part part : request.getParts()) {
            if (!"photos".equals(part.getName())) continue;

            String originalFileName = extractFileName(part);
            if (originalFileName == null || originalFileName.isEmpty()) continue;

            // Kiểm tra định dạng
            String lower = originalFileName.toLowerCase();
            if (!lower.endsWith(".jpg") && !lower.endsWith(".jpeg")
                    && !lower.endsWith(".png") && !lower.endsWith(".gif")
                    && !lower.endsWith(".bmp") && !lower.endsWith(".webp")) {
                continue;
            }

            // Lưu file với tên duy nhất
            String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;
            File savedFile = new File(uploadFolder, uniqueFileName);
            part.write(savedFile.getAbsolutePath());

            // Lưu vào DB
            Image image = new Image();
            image.setUserId(userId);
            image.setFileName(originalFileName);
            image.setFilePath("uploads/" + uniqueFileName);
            image.setDescription(description != null ? description : "");
            image.setFileSize(part.getSize());
            image.setUploadDate(LocalDate.now());
            image.setDeleted(false);

            imageService.uploadImage(image);
        }

        response.sendRedirect(request.getContextPath() + "/Photos");
    }

    private String extractFileName(Part part) {
        String cd = part.getHeader("content-disposition");
        if (cd == null) return null;
        for (String token : cd.split(";")) {
            token = token.trim();
            if (token.startsWith("filename")) {
                String name = token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
                // Xử lý path đầy đủ (một số browser cũ gửi full path)
                int idx = Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\'));
                if (idx >= 0) name = name.substring(idx + 1);
                return name.trim();
            }
        }
        return null;
    }
}
