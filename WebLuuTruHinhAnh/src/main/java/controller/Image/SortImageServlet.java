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
        // 1.1.1 - 1.1.3 Kiểm tra quyền truy cập (Session Validation)
        // =========================

        // 1.1.2. Lớp điều khiển (PhotosServlet/SortImageServlet) tiếp nhận request và kiểm tra Session hiện tại.
        HttpSession session = request.getSession(false);

        // 1.1.3. Hệ thống lấy đối tượng User từ thuộc tính "user" trong Session.
        User user = (session != null)
                ? (User) session.getAttribute("user")
                : null;



        // 1.1.4. Hệ thống xác nhận đối tượng User tồn tại (khác null) và trích xuất thông tin định danh userId = user.getId().
        // Exception Flow 1.3:
        // 1.3.1. Tại bước 1.1.4, hệ thống kiểm tra thấy đối tượng User thu được từ Session bằng null.
        // 1.3.2. Hệ thống hủy bỏ tiến trình xử lý sắp xếp ảnh.
        // 1.3.3. Hệ thống thực hiện chuyển hướng người dùng về trang đăng nhập. Kết thúc luồng.
        if (user == null) {

            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }



        // Tiếp tục 1.1.4: trích xuất thông tin định danh userId = user.getId()
        int userId = user.getId();



        // =========================
        // 1.1.4 Tiếp nhận tham số sắp xếp
        // =========================

        // 1.1.5. Hệ thống tiếp nhận tham số yêu cầu từ URL/Request Parameter có tên là "sortBy".
        String sortBy = request.getParameter("sortBy");



        // =========================
        // 1.1.5 - 1.1.8 Xử lý nghiệp vụ và truy vấn dữ liệu
        // =========================

        // 1.1.6. Lớp điều khiển gọi tầng nghiệp vụ (ImageService) để xử lý logic.
        // 1.1.7. ImageService kiểm tra tham số sortBy (gán "newest" nếu null).
        // 1.1.8. ImageService kết nối xuống tầng dữ liệu truy vấn Database sắp xếp theo tiêu chí.
        // 1.1.9. Hệ thống trả về một danh sách thực thể hình ảnh List<Image> images.
        List<Image> images = imageService.getImagesSorted(userId, sortBy);



        // =========================
        // 1.1.9 - 1.1.11 Thiết lập dữ liệu hiển thị
        // =========================

        // 1.1.10. Hệ thống đính kèm danh sách ảnh vào Request.
        request.setAttribute("images", images);



        // 1.1.11. Hệ thống lưu lại tiêu chí vừa chọn vào Request để giữ trạng thái hiển thị trên giao diện.
        // Alternative Flow 1.2:
        // 1.2.2. Hệ thống kiểm tra thấy sortBy == null.
        // 1.2.3. Hệ thống sẽ tự động gán giá trị "newest" (Sắp xếp theo ảnh mới nhất) làm tiêu chí mặc định.
        request.setAttribute(
                "currentSort",
                sortBy != null ? sortBy : "newest"
        );



        // 1.1.12. Hệ thống thiết lập thuộc tính điều hướng thanh menu (chọn tab "photos").
        request.setAttribute("activeTopNav", "photos");



        // =========================
        // 1.1.12 - 1.1.13 Trả về giao diện
        // =========================

        // 1.1.13. Hệ thống thực hiện chuyển tiếp (Forward) toàn bộ dữ liệu sang trang hiển thị.
        request.getRequestDispatcher("/image.jsp")
                .forward(request, response);

        // Servlet hoàn thành nhánh xử lý Backend tại đây.
    }
}