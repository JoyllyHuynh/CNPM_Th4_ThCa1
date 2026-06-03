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

        // 3.1.1 Lấy Session hiện tại
        HttpSession session = request.getSession();

        // 3.1.2 Lấy đối tượng User từ Session
        User user = (User) session.getAttribute("user");

        // 3.1.3 Lấy userId và kiểm tra hợp lệ
        // Exception Flow 3.4
        // 3.4.1 – Session/User không hợp lệ
        // Nếu user == null hoặc userId == null
        // -> Hủy tiến trình tìm kiếm
        // -> Redirect về login.jsp
        if (user == null ) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        Integer userId = user.getId();



        // =========================
        // 3.1.4 - 3.1.5 Tiếp nhận và chuẩn hóa từ khóa tìm kiếm
        // =========================

        // 3.1.4 Lấy Request Parameter "keyword"
        String keyword = request.getParameter("keyword");



        // Danh sách kết quả ảnh
        List<Image> images;



        // 3.1.5 Kiểm tra keyword:
        // khác null và không rỗng sau trim()
        if (keyword != null && !keyword.trim().isEmpty()) {



            // =========================
            // 3.1.6 - 3.1.9 Xử lý nghiệp vụ truy vấn dữ liệu (Business Logic)
            // =========================

            // 3.1.6 Lớp điều khiển gọi phương thức searchByKW của tầng nghiệp vụ ImageService
            // (Truyền vào userId và keyword đã được chuẩn hóa theo Bước 3.1.7)
            // 3.1.9 Hệ thống gán danh sách kết quả trả về từ DB vào biến tập hợp List<Image> images
            images = imageService.searchByKW(userId, keyword.trim());



            // =========================
            // 3.1.10 Thiết lập dữ liệu hiển thị
            // =========================

            // 3.1.10 Đính kèm keyword đã chuẩn hóa
            // để hiển thị lại trên ô tìm kiếm
            request.setAttribute("searchKeyword", keyword.trim());

        } else {

            // =========================
            // Alternative Flow 3.2
            // 3.2.1 – Keyword rỗng/null
            // =========================

            // Không gọi ImageService
            // 3.2.4 Gán danh sách kết quả rỗng
            images = List.of();
        }



        // =========================
        // 3.1.11 - 3.1.13 Thiết lập dữ liệu và trạng thái hiển thị
        // =========================

        // 3.1.11 Đính kèm danh sách ảnh tìm được
        request.setAttribute("images", images);

        // 3.1.12 Đánh dấu đây là trang kết quả tìm kiếm
        request.setAttribute("isSearchResult", true);

        // 3.1.13 Thiết lập menu active
        request.setAttribute("activeTopNav", "photos");



        // =========================
        // 3.1.14 - 3.1.15 Chuyển tiếp giao diện kết quả
        // =========================

        // 3.1.14 Forward dữ liệu sang image.jsp
        request.getRequestDispatcher("image.jsp")
                .forward(request, response);

        // 3.1.15 image.jsp render danh sách ảnh kết quả tìm kiếm
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}