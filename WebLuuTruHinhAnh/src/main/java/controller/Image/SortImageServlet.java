package controller.Image;

import controller.service.ImageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Image;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SortImageServlet", value = "/Photos")
public class SortImageServlet extends HttpServlet {

    private final ImageService imageService = new ImageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // =========================
        // 1.1. Kiểm tra quyền truy cập (Session Validation)
        // =========================

        // 1.1.1 Lấy Session hiện tại
        HttpSession session = request.getSession(false);

        // 1.1.2 Lấy đối tượng User từ Session
        User user = (session != null)
                ? (User) session.getAttribute("user")
                : null;



        // 1.1.3 Kiểm tra User hợp lệ
        // Exception Flow 8.1:
        // Nếu user == null
        // -> Session hết hạn/chưa đăng nhập
        // -> Redirect về login.jsp
        // -> Kết thúc Use Case
        if (user == null) {

            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }



        // 1.1.3 Trích xuất userId từ User
        int userId = user.getId();



        // =========================
        // 1.2. Tiếp nhận tham số sắp xếp
        // =========================

        // 1.2.1 Lấy Request Parameter "sortBy"
        String sortBy = request.getParameter("sortBy");



        // =========================
        // 1.3. Xử lý nghiệp vụ và truy vấn dữ liệu
        // =========================

        // 1.3.1 Controller gọi tầng Service xử lý sắp xếp

        // 1.3.2 Service kiểm tra sortBy:
        // nếu null/rỗng -> mặc định "newest"

        // 1.3.3 Service/DAO truy vấn DB:
        // lấy danh sách ảnh theo userId
        // và sắp xếp theo sortBy

        // 1.3.4 Nhận kết quả List<Image> images
        List<Image> images = imageService.getImagesSorted(userId, sortBy);



        // =========================
        // 1.4. Thiết lập dữ liệu hiển thị
        // =========================

        // 1.4.1 Đính kèm danh sách ảnh vào Request
        request.setAttribute("images", images);



        // 1.4.2 Lưu lại tiêu chí sắp xếp hiện tại
        // Alternative Flow 7.1:
        // Nếu sortBy == null
        // -> Gán mặc định "newest"
        request.setAttribute(
                "currentSort",
                sortBy != null ? sortBy : "newest"
        );



        // 1.4.3 Thiết lập menu active
        request.setAttribute("activeTopNav", "photos");



        // =========================
        // 1.5. Trả về giao diện
        // =========================

        // 1.5.1 Forward dữ liệu sang image.jsp
        request.getRequestDispatcher("/image.jsp")
                .forward(request, response);

        // 1.5.2 image.jsp render danh sách ảnh đã sắp xếp
    }
}