package controller.Image;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Image;
import controller.service.ImageService;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchImageServlet", value = "/search")
public class SearchImageServlet extends HttpServlet {

    private final ImageService imageService = new ImageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // =========================
        // 3.1.1 - 3.1.3 Kiểm tra quyền truy cập và thông tin người dùng
        // =========================

        // 3.1.2. Lớp điều khiển (SearchImageServlet) tiếp nhận request và lấy Session hiện tại của người dùng.
        HttpSession session = request.getSession();

        // 3.1.3. Hệ thống trích xuất đối tượng User từ thuộc tính "user" trong Session.
        User user = (User) session.getAttribute("user");

        // 3.1.4. Hệ thống lấy thông tin định danh userId từ đối tượng User và xác nhận mã định danh hợp lệ.
        // Exception Flow 3.4:
        // 3.4.1. Tại bước 3.1.4, hệ thống phát hiện đối tượng User bị null.
        // 3.4.2. Hệ thống hủy bỏ toàn bộ tiến trình xử lý tìm kiếm.
        // 3.4.3. Hệ thống thực hiện chuyển hướng về trang đăng nhập. Kết thúc luồng.
        if (user == null ) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        // Tiếp tục 3.1.4: trích xuất userId.
        Integer userId = user.getId();



        // =========================
        // 3.1.4 - 3.1.5 Tiếp nhận và chuẩn hóa từ khóa tìm kiếm
        // =========================

        // 3.1.5. Hệ thống trích xuất chuỗi ký tự từ tham số "keyword" trong HTTP Request.
        String keyword = request.getParameter("keyword");



        // Danh sách kết quả ảnh
        List<Image> images;



        // 3.1.6. Hệ thống kiểm tra tính hợp lệ của từ khóa: khác null và không rỗng sau trim().
        if (keyword != null && !keyword.trim().isEmpty()) {



            // =========================
            // 3.1.6 - 3.1.9 Xử lý nghiệp vụ truy vấn dữ liệu (Business Logic)
            // =========================

            // 3.1.7. Lớp điều khiển gọi phương thức searchByKW của tầng nghiệp vụ ImageService.
            // 3.1.8. Tầng ImageService tiếp nhận userId và keyword đã được chuẩn hóa.
            // 3.1.9. Tầng nghiệp vụ gọi ImageDao truy vấn DB lọc ảnh theo userId và keyword.
            // 3.1.10. Hệ thống gán danh sách kết quả trả về từ DB vào List<Image> images.
            images = imageService.searchByKW(userId, keyword.trim());



            // =========================
            // 3.1.10 Thiết lập dữ liệu hiển thị
            // =========================

            // 3.1.11. Đính kèm keyword đã chuẩn hóa để hiển thị lại trên ô tìm kiếm.
            request.setAttribute("searchKeyword", keyword.trim());

        } else {

            // Alternative Flow 3.2:
            // 3.2.1. Tại bước 3.1.6, keyword rỗng hoặc null.
            // 3.2.4. Hệ thống tự động khởi tạo và gán một danh sách rỗng.
            images = List.of();
        }



        // =========================
        // 3.1.11 - 3.1.13 Thiết lập dữ liệu và trạng thái hiển thị
        // =========================

        // 3.1.12. Hệ thống đính kèm danh sách ảnh tìm được vào Request.
        request.setAttribute("images", images);

        // 3.1.13. Hệ thống thiết lập cờ đánh dấu trạng thái hiển thị là trang kết quả tìm kiếm.
        request.setAttribute("isSearchResult", true);

        // 3.1.14. Hệ thống cấu hình thuộc tính menu để làm sáng mục "Photos" trên thanh điều hướng.
        request.setAttribute("activeTopNav", "photos");



        // =========================
        // 3.1.14 - 3.1.15 Chuyển tiếp giao diện kết quả
        // =========================

        // 3.1.15. Hệ thống sử dụng RequestDispatcher để chuyển tiếp (forward) sang image.jsp.
        request.getRequestDispatcher("image.jsp")
                .forward(request, response);

        // Servlet hoàn thành nhánh xử lý Backend tại đây.
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}