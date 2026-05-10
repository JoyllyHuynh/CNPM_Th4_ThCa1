package controller.Image;

import controller.service.ImageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/RenameImage")
public class RenameImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final ImageService imageService = new ImageService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("id");
        String newName = request.getParameter("newName");

        // 1. Kiểm tra rỗng trước
        if (idStr == null || newName == null || newName.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Loại bỏ khoảng trắng thừa
        newName = newName.trim();

        // 2. Kiểm tra và tự động thêm đuôi .png nếu chưa có
        if (!newName.toLowerCase().endsWith(".png")) {
            newName += ".png";
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 3. Gọi hàm renameImage dưới Service
        boolean isSuccess = imageService.renameImage(id, newName);

        if (isSuccess) {
            // Trả về tên mới (đã bao gồm đuôi .png) để JS cập nhật giao diện
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(newName);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}