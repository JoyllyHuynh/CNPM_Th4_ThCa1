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
        // 6.1. Tiếp nhận thông tin đăng ký
        // =========================
        
        // 6.1.1 Hệ thống tiếp nhận yêu cầu gửi lên qua phương thức HTTP POST
        // 6.1.2 Thu thập ba tham số từ Request Parameter
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");

        // =========================
        // 6.2. Kiểm tra tính toàn vẹn của dữ liệu đầu vào (Data Validation)
        // =========================
        
        // 6.1.3 Loại bỏ khoảng trắng ở đầu và cuối chuỗi
        if (email != null) email = email.trim();
        if (fullName != null) fullName = fullName.trim();

        // 6.1.4 Kiểm tra dữ liệu bắt buộc (không rỗng/khoảng trắng)
        // =========================
        // Exception Flow 6.2: Người dùng điền thiếu thông tin hoặc thông tin chỉ chứa khoảng trắng
        // =========================
        // 6.2.1 Tại bước 6.1.4, nếu tham số bị rỗng (null) hoặc isBlank()
        if (email == null || password == null || fullName == null ||
                email.isEmpty() || password.isBlank() || fullName.isEmpty()) {
            
            // 6.2.2 Hệ thống ngừng luồng xử lý nghiệp vụ ngay lập tức
            // 6.2.3 Hệ thống đính kèm thông báo cảnh báo lỗi
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin");
            
            // 6.2.4 Hệ thống thực hiện giữ chân người dùng tại trang đăng ký
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 6.1.5 Kiểm tra định dạng dữ liệu (Email, Tên, Mật khẩu)
        // =========================
        // Exception Flow 6.3: Định dạng dữ liệu không hợp lệ
        // =========================
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        String nameRegex = "^[a-zA-ZÀ-ỹ\\s]+$"; // Tiếng Việt và khoảng trắng, không ký tự đặc biệt
        String passRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

        // 6.3.1 Tại bước 6.1.5, kiểm tra nếu email sai định dạng
        if (!email.matches(emailRegex)) {
            // 6.3.2 Hệ thống ngừng luồng xử lý nghiệp vụ ngay lập tức.
            // 6.3.3 Đính kèm thông báo cảnh báo lỗi tương ứng
            request.setAttribute("error", "Định dạng email không hợp lệ!");
            // 6.3.4 Giữ chân người dùng tại trang đăng ký
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 6.3.1 Tại bước 6.1.5, kiểm tra nếu họ tên chứa ký tự đặc biệt/số
        if (!fullName.matches(nameRegex)) {
            // 6.3.2 Hệ thống ngừng luồng xử lý nghiệp vụ
            // 6.3.3 Đính kèm thông báo cảnh báo lỗi tương ứng
            request.setAttribute("error", "Họ tên không được chứa ký tự đặc biệt hoặc chữ số!");
            // 6.3.4 Giữ chân người dùng tại trang đăng ký
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 6.3.1 Tại bước 6.1.5, kiểm tra nếu mật khẩu không đủ mạnh
        if (!password.matches(passRegex)) {
            // 6.3.2 Hệ thống ngừng luồng xử lý nghiệp vụ
            // 6.3.3 Đính kèm thông báo cảnh báo lỗi tương ứng
            request.setAttribute("error", "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, chữ số và ký tự đặc biệt!");
            // 6.3.4 Giữ chân người dùng tại trang đăng ký
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // =========================
        // 6.1.6 - 6.1.8 Thực hiện nghiệp vụ đăng ký tài khoản mới
        // =========================
        
        // 6.1.6 Lớp điều khiển gọi hàm nghiệp vụ
        // 6.1.7 AuthService kiểm tra email tồn tại
        // 6.1.8 Thực hiện mã hóa mật khẩu và chèn bản ghi
        boolean success = userService.register(email, password, fullName);

        // 6.1.9 Tiến trình xử lý dữ liệu hoàn tất thành công, trả về trạng thái kết quả
        // =========================
        // 6.1.10 - 6.1.12 Phản hồi trạng thái đăng ký thành công
        // =========================
        if (success) {
            // 6.1.10 Đính kèm thông báo thành công
            request.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
            
            // 6.1.11 Forward sang login.jsp
            // 6.1.12 login.jsp hiển thị hộp thoại thông báo
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            // =========================
            // Alternative Flow 6.4: Email đăng ký đã tồn tại trên hệ thống
            // =========================
            // 6.4.1 Tại bước 6.1.7, AuthService phát hiện địa chỉ email này đã được sử dụng
            // 6.4.2 Hàm nghiệp vụ trả về trạng thái kết quả thất bại (success = false)
            
            // 6.4.3 Hệ thống đính kèm thông báo lỗi vào thuộc tính Request
            request.setAttribute("error", "Email đã tồn tại!");
            
            // 6.4.4 Hệ thống thực hiện chuyển tiếp ngược lại trang đăng ký
            // 6.4.5 Trình duyệt hiển thị lại form đăng ký cùng thông báo lỗi
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}