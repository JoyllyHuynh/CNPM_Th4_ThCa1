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
        // 3.1. Kiểm tra quyền truy cập và thông tin người dùng
        // =========================

        // 3.1.1 Lấy Session hiện tại
        HttpSession session = request.getSession();

        // 3.1.2 Lấy đối tượng User từ Session
        User user = (User) session.getAttribute("user");

        // 3.1.3 Lấy userId và kiểm tra hợp lệ
        Integer userId = user.getId();



        // Exception Flow 8.1
        // 3.1.3 – Session/User không hợp lệ
        // Nếu userId == null
        // -> Hủy tiến trình tìm kiếm
        // -> Redirect về login.jsp
        if (userId == null) {

            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }



        // =========================
        // 3.2. Tiếp nhận và chuẩn hóa từ khóa tìm kiếm
        // =========================

        // 3.2.1 Lấy Request Parameter "keyword"
        String keyword = request.getParameter("keyword");



        // Danh sách kết quả ảnh
        List<Image> images;



        // 3.2.2 Kiểm tra keyword:
        // khác null và không rỗng sau trim()
        if (keyword != null && !keyword.trim().isEmpty()) {



            // =========================
            // 3.3. Xử lý nghiệp vụ truy vấn dữ liệu
            // =========================

            // 3.3.1 Controller gọi tầng Service tìm kiếm

            // 3.3.2 Truyền userId và keyword đã trim()

            // 3.3.3 Service/DAO truy vấn DB:
            // tìm ảnh thuộc userId và chứa keyword

            // 3.3.4 Nhận kết quả List<Image> images
            images = imageService.searchByKW(userId, keyword.trim());



            // =========================
            // 3.4. Thiết lập dữ liệu hiển thị
            // =========================

            // 3.4.1 Đính kèm keyword đã chuẩn hóa
            // để hiển thị lại trên ô tìm kiếm
            request.setAttribute("searchKeyword", keyword.trim());

        } else {

            // =========================
            // Alternative Flow 7.1
            // 3.2.2 – Keyword rỗng/null
            // =========================

            // Không gọi ImageService
            // Gán danh sách kết quả rỗng
            images = List.of();
        }



        // =========================
        // 3.4. Thiết lập dữ liệu và trạng thái hiển thị
        // =========================

        // 3.4.2 Đính kèm danh sách ảnh tìm được
        request.setAttribute("images", images);

        // 3.4.3 Đánh dấu đây là trang kết quả tìm kiếm
        request.setAttribute("isSearchResult", true);

        // 3.4.4 Thiết lập menu active
        request.setAttribute("activeTopNav", "photos");



        // =========================
        // 3.5. Chuyển tiếp giao diện kết quả
        // =========================

        // 3.5.1 Forward dữ liệu sang image.jsp
        request.getRequestDispatcher("image.jsp")
                .forward(request, response);

        // 3.5.2 image.jsp render danh sách ảnh kết quả tìm kiếm
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}