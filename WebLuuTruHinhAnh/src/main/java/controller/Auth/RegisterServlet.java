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
        
        // 6.1.2. Hệ thống (RegisterServlet) tiếp nhận yêu cầu gửi lên thông qua phương thức HTTP POST.
        // 6.1.3. Hệ thống thu thập ba tham số từ Request Parameter bao gồm: email, password, fullName.
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");

        // =========================
        // 6.2. Kiểm tra tính toàn vẹn của dữ liệu đầu vào (Data Validation)
        // =========================
        
        // 6.1.4. Hệ thống tự động loại bỏ các khoảng trắng thừa ở đầu và cuối của chuỗi dữ liệu Email và Họ tên thông qua hàm .trim().
        if (email != null) email = email.trim();
        if (fullName != null) fullName = fullName.trim();

        // 6.1.5. Hệ thống kiểm tra điều kiện dữ liệu bắt buộc: Xác định cả ba tham số không được phép mang giá trị rỗng hoặc chỉ chứa toàn khoảng trắng.
        // =========================
        // Exception Flow 6.2: Người dùng điền thiếu thông tin hoặc thông tin chỉ chứa khoảng trắng
        // =========================
        // 6.2.1. Tại bước 6.1.5, nếu một trong ba tham số truyền lên bị rỗng (null) hoặc isBlank()
        if (email == null || password == null || fullName == null ||
                email.isEmpty() || password.isBlank() || fullName.isEmpty()) {
            
            // 6.2.2. Hệ thống ngừng luồng xử lý nghiệp vụ ngay lập tức, không gọi xuống tầng AuthService.
            // 6.2.3. Hệ thống đính kèm thông báo cảnh báo lỗi "Vui lòng điền đầy đủ thông tin".
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin");
            
            // 6.2.4. Hệ thống thực hiện giữ chân người dùng tại trang đăng ký bằng cách chuyển tiếp luồng (forward).
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 6.1.6. Hệ thống kiểm tra định dạng dữ liệu (Validation): Đảm bảo Email đúng cấu trúc, Họ tên hợp lệ, và Mật khẩu đủ mạnh (Regex).
        // =========================
        // Exception Flow 6.3: Định dạng dữ liệu không hợp lệ
        // =========================
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        String nameRegex = "^[a-zA-ZÀ-ỹ\\s]+$"; // Tiếng Việt và khoảng trắng, không ký tự đặc biệt
        String passRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

        // 6.3.1. Tại bước 6.1.6, kiểm tra nếu email sai định dạng.
        if (!email.matches(emailRegex)) {
            // 6.3.2. Hệ thống ngừng luồng xử lý nghiệp vụ ngay lập tức.
            // 6.3.3. Hệ thống đính kèm thông báo cảnh báo lỗi tương ứng.
            request.setAttribute("error", "Định dạng email không hợp lệ!");
            // 6.3.4. Hệ thống thực hiện giữ chân người dùng tại trang đăng ký.
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Tiếp tục 6.3.1: kiểm tra nếu họ tên chứa ký tự đặc biệt/số.
        if (!fullName.matches(nameRegex)) {
            // 6.3.2. Hệ thống ngừng luồng xử lý nghiệp vụ.
            // 6.3.3. Hệ thống đính kèm thông báo cảnh báo lỗi tương ứng.
            request.setAttribute("error", "Họ tên không được chứa ký tự đặc biệt hoặc chữ số!");
            // 6.3.4. Hệ thống thực hiện giữ chân người dùng tại trang đăng ký.
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Tiếp tục 6.3.1: kiểm tra nếu mật khẩu không đủ mạnh.
        if (!password.matches(passRegex)) {
            // 6.3.2. Hệ thống ngừng luồng xử lý nghiệp vụ.
            // 6.3.3. Hệ thống đính kèm thông báo cảnh báo lỗi tương ứng.
            request.setAttribute("error", "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, chữ số và ký tự đặc biệt!");
            // 6.3.4. Hệ thống thực hiện giữ chân người dùng tại trang đăng ký.
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // =========================
        // 6.1.6 - 6.1.8 Thực hiện nghiệp vụ đăng ký tài khoản mới
        // =========================
        
        // 6.1.7. Lớp điều khiển gọi hàm nghiệp vụ xử lý đăng ký thuộc tầng dịch vụ AuthService.
        // 6.1.8. AuthService kết nối xuống cơ sở dữ liệu để kiểm tra sự tồn tại của Email.
        // 6.1.9. Khi xác nhận Email chưa tồn tại, hệ thống thực hiện mã hóa mật khẩu và chèn một bản ghi người dùng mới.
        boolean success = userService.register(email, password, fullName);

        // 6.1.10. Tiến trình xử lý dữ liệu hoàn tất thành công, trả về trạng thái kết quả success.
        // =========================
        // 6.1.11 - 6.1.12 Phản hồi trạng thái đăng ký thành công
        // =========================
        if (success) {
            // 6.1.11. Hệ thống đính kèm thông báo thành công vào thuộc tính Request.
            request.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
            
            // 6.1.12. Hệ thống thực hiện chuyển tiếp (Forward) luồng xử lý và dữ liệu sang trang đăng nhập (/login.jsp).
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            // =========================
            // Alternative Flow 6.4: Email đăng ký đã tồn tại trên hệ thống
            // =========================
            // 6.4.1. Tại bước 6.1.8, nếu AuthService phát hiện địa chỉ email này đã được sử dụng.
            // 6.4.2. Hàm nghiệp vụ trả về trạng thái kết quả thất bại (success = false).
            
            // 6.4.3. Hệ thống đính kèm thông báo lỗi "Email đã tồn tại!" vào thuộc tính Request.
            request.setAttribute("error", "Email đã tồn tại!");
            
            // 6.4.4. Hệ thống thực hiện chuyển tiếp ngược lại trang đăng ký (/register.jsp).
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}