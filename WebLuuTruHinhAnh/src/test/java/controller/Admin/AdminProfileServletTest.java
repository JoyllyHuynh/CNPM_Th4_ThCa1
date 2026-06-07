package controller.Admin;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

@DisplayName("UC-24: Xem thông tin Admin")
class AdminProfileServletTest {

    private AdminProfileServlet servlet;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private HttpSession mockSession;

    @Mock
    private RequestDispatcher mockDispatcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new AdminProfileServlet();
        when(mockRequest.getContextPath()).thenReturn("/app");
    }

    private void invokeDoGet() throws Exception {
        Method doGetMethod = AdminProfileServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(servlet, mockRequest, mockResponse);
    }

    @Test
    @DisplayName("Chưa đăng nhập hoặc không phải Admin -> Chuyển hướng login")
    void testNotAdminRedirectsToLogin() throws Exception {
        when(mockRequest.getSession(false)).thenReturn(null);

        invokeDoGet();

        verify(mockResponse).sendRedirect("/app/login.jsp");
    }

    @Test
    @DisplayName("Đăng nhập Admin hợp lệ -> Forward tới profile.jsp")
    void testAdminProfileSuccess() throws Exception {
        User adminUser = new User();
        adminUser.setRole("ADMIN");
        adminUser.setFullName("Admin 1");

        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockSession.getAttribute("user")).thenReturn(adminUser);
        when(mockRequest.getRequestDispatcher("/Admin/profile.jsp")).thenReturn(mockDispatcher);

        invokeDoGet();

        verify(mockDispatcher).forward(mockRequest, mockResponse);
    }
}
