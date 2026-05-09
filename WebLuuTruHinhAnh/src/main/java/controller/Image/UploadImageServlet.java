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
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)
public class UploadImageServlet extends HttpServlet {

    private final ImageService imageService = new ImageService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int userId = user.getId();

        String uploadDir = getServletContext().getRealPath("/uploads");
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        String description = request.getParameter("description");

        for (Part part : request.getParts()) {
            String fieldName = part.getName();
            if (!fieldName.equals("photos"))
                continue;

            String originalFileName = getFileName(part);
            if (originalFileName == null || originalFileName.isEmpty())
                continue;

            String lowerName = originalFileName.toLowerCase();
            if (!(lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")
                    || lowerName.endsWith(".png") || lowerName.endsWith(".gif")
                    || lowerName.endsWith(".bmp") || lowerName.endsWith(".webp"))) {
                continue;
            }

            String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;

            File savedFile = new File(uploadFolder, uniqueFileName);
            part.write(savedFile.getAbsolutePath());

            Image image = new Image();
            image.setUserId(userId);
            image.setFileName(originalFileName);
            image.setFilePath(uniqueFileName);
            image.setDescription(description);
            image.setFileSize(part.getSize());
            image.setUploadDate(LocalDate.now());
            image.setDeleted(false);

            imageService.uploadImage(image);
        }

        response.sendRedirect(request.getContextPath() + "/Photos");
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition != null) {
            for (String token : contentDisposition.split(";")) {
                if (token.trim().startsWith("filename")) {
                    String name = token.substring(token.indexOf('=') + 1).trim().replace("\"", "");

                    int idx = name.lastIndexOf(File.separator);
                    if (idx >= 0) {
                        name = name.substring(idx + 1);
                    }

                    idx = name.lastIndexOf('/');
                    if (idx >= 0) {
                        name = name.substring(idx + 1);
                    }
                    return name;
                }
            }
        }
        return null;
    }
}
