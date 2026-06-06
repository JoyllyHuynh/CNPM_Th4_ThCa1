package controller.Auth;

import controller.service.AuthService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("UC-06: Đăng ký tài khoản (RegisterServlet) - Actual Logic Test")
class RegisterServletTest {

    private RegisterServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private AuthService mockAuthService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        servlet = new RegisterServlet();

        // Sử dụng Java Reflection để thay thế AuthService thật bằng MockAuthService
        // để không cần kết nối tới MySQL khi chạy Unit Test (đúng chuẩn Unit Test)
        Field field = RegisterServlet.class.getDeclaredField("userService");
        field.setAccessible(true);
        field.set(servlet, mockAuthService);

        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    // ================================================================
    // Exception Flow 6.2: Người dùng điền thiếu thông tin
    // ================================================================

    @Test
    @DisplayName("TC-REG-03 | [6.2] Dữ liệu rỗng -> Forward về register.jsp với lỗi")
    void testMissingData_AllEmpty() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("email")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");
        when(request.getParameter("fullName")).thenReturn("");

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(request).setAttribute("error", "Vui lòng điền đầy đủ thông tin");
        verify(request).getRequestDispatcher("/register.jsp");
        verify(dispatcher).forward(request, response);
        verify(mockAuthService, never()).register(anyString(), anyString(), anyString()); // Không gọi tầng service
    }

    @Test
    @DisplayName("TC-REG-03b | [6.2] Tham số bị null -> Forward về register.jsp với lỗi")
    void testMissingData_Null() throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn(null);
        when(request.getParameter("password")).thenReturn("Pass@123");
        when(request.getParameter("fullName")).thenReturn("Nguyen Van A");

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Vui lòng điền đầy đủ thông tin");
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("TC-REG-04 | [6.2] Mật khẩu chỉ chứa khoảng trắng -> Forward về register.jsp với lỗi")
    void testMissingData_BlankPassword() throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn("user@gmail.com");
        when(request.getParameter("password")).thenReturn("   ");
        when(request.getParameter("fullName")).thenReturn("Nguyen Van A");

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Vui lòng điền đầy đủ thông tin");
        verify(dispatcher).forward(request, response);
    }

    // ================================================================
    // Exception Flow 6.3: Định dạng dữ liệu không hợp lệ
    // ================================================================

    @Test
    @DisplayName("TC-REG-05b | [6.3] Email sai định dạng (thiếu @) -> Báo lỗi")
    void testInvalidEmail() throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn("usergmail.com");
        when(request.getParameter("password")).thenReturn("Pass@123");
        when(request.getParameter("fullName")).thenReturn("Nguyen Van A");

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Định dạng email không hợp lệ!");
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("TC-REG-06b | [6.3] Họ tên chứa số -> Báo lỗi")
    void testInvalidFullName_ContainsNumber() throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn("user@gmail.com");
        when(request.getParameter("password")).thenReturn("Pass@123");
        when(request.getParameter("fullName")).thenReturn("Nguyen Van 123");

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Họ tên không được chứa ký tự đặc biệt hoặc chữ số!");
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("TC-REG-07b | [6.3] Mật khẩu yếu (không có ký tự đặc biệt) -> Báo lỗi")
    void testInvalidPassword_Weak() throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn("user@gmail.com");
        when(request.getParameter("password")).thenReturn("Password123"); // Thiếu ký tự đặc biệt
        when(request.getParameter("fullName")).thenReturn("Nguyen Van A");

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, chữ số và ký tự đặc biệt!");
        verify(dispatcher).forward(request, response);
    }

    // ================================================================
    // Alternative Flow 6.4: Email đã tồn tại
    // ================================================================

    @Test
    @DisplayName("TC-REG-09 | [6.4] Email đã tồn tại -> Báo lỗi email đã tồn tại")
    void testEmailAlreadyExists() throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn("user@gmail.com");
        when(request.getParameter("password")).thenReturn("Pass@123");
        when(request.getParameter("fullName")).thenReturn("Nguyen Van A");
        
        // Mock tầng service trả về false (đăng ký thất bại do email tồn tại)
        when(mockAuthService.register("user@gmail.com", "Pass@123", "Nguyen Van A")).thenReturn(false);

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Email đã tồn tại!");
        verify(request).getRequestDispatcher("/register.jsp");
        verify(dispatcher).forward(request, response);
    }

    // ================================================================
    // Normal Flow 6.1: Đăng ký thành công
    // ================================================================

    @Test
    @DisplayName("TC-REG-10 | [6.1] Thông tin hợp lệ -> Đăng ký thành công -> Forward tới login.jsp")
    void testRegisterSuccess() throws ServletException, IOException {
        // Có khoảng trắng thừa ở email và fullName để test hàm .trim() [6.1.4]
        when(request.getParameter("email")).thenReturn("  user@gmail.com  ");
        when(request.getParameter("password")).thenReturn("Pass@123");
        when(request.getParameter("fullName")).thenReturn("  Nguyen Van A  ");
        
        // Mock tầng service trả về true
        when(mockAuthService.register("user@gmail.com", "Pass@123", "Nguyen Van A")).thenReturn(true);

        servlet.doPost(request, response);

        verify(request).setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
        verify(request).getRequestDispatcher("/login.jsp");
        verify(dispatcher).forward(request, response);
    }
}
