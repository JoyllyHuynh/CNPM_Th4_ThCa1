package controller.Auth;

import controller.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {

    private final AuthService userService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // =========================
        // 8.1. Tiếp nhận thông tin đăng ký
        // =========================

        // 8.1.2 Thu thập tham số email từ Request Parameter
        String email = request.getParameter("email");

        // 8.1.2 Thu thập tham số password từ Request Parameter
        String password = request.getParameter("password");

        // 8.1.2 Thu thập tham số fullName từ Request Parameter
        String fullName = request.getParameter("fullName");



        // =========================
        // 8.2. Kiểm tra tính toàn vẹn của dữ liệu đầu vào
        // =========================

        // 8.2.1 Kiểm tra dữ liệu null hoặc chỉ chứa khoảng trắng
        // Exception Flow 8.1:
        // Nếu email/password/fullName bị thiếu hoặc rỗng
        // -> Hệ thống dừng xử lý nghiệp vụ
        // -> Không gọi xuống AuthService
        // -> Forward lại trang register.jsp cùng thông báo lỗi
        if (email == null || password == null || fullName == null ||
                email.isBlank() || password.isBlank() || fullName.isBlank()) {

            // Đính kèm thông báo lỗi lên Request
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin");

            // Forward quay lại trang đăng ký
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 8.2.2 Dữ liệu hợp lệ, vượt qua kiểm tra validation



        // =========================
        // 8.3. Thực hiện nghiệp vụ đăng ký tài khoản mới
        // =========================

        // 8.3.1 Controller gọi tầng Service xử lý đăng ký
        // 8.3.2 Tự động trim() email và fullName
        // 8.3.3 AuthService kiểm tra email tồn tại,
        //       mã hóa mật khẩu và insert user mới vào DB
        // 8.3.4 Nhận kết quả success = true/false
        boolean success = userService.register(email.trim(), password, fullName.trim());



        // =========================
        // 8.4. Phản hồi trạng thái đăng ký thành công
        // =========================

        // Normal Flow:
        // Nếu đăng ký thành công
        if (success) {

            // 8.4.1 Đính kèm thông báo thành công
            request.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");

            // 8.4.2 Forward sang login.jsp
            // 8.4.3 login.jsp hiển thị thông báo đăng ký thành công
            request.getRequestDispatcher("/login.jsp").forward(request, response);

        } else {

            // =========================
            // Alternative Flow 7.1
            // 8.3.4 – Email đã tồn tại
            // =========================

            // AuthService trả về success = false
            // Hệ thống đính kèm thông báo lỗi
            request.setAttribute("error", "Email đã tồn tại!");

            // Forward quay lại register.jsp
            // để người dùng nhập email khác
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}