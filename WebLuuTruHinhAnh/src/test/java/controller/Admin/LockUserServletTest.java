package controller.Admin;

import DAO.UserDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

@DisplayName("UC-23: Khóa người dùng")
class LockUserServletTest {

    private LockUserServlet servlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private HttpSession mockSession;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new LockUserServlet();
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockSession = mock(HttpSession.class);
    }

    private void invokeDoGet() throws Exception {
        Method doGetMethod = LockUserServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(servlet, mockRequest, mockResponse);
    }

    @Test
    @DisplayName("Khóa tài khoản thành công khi là Admin hợp lệ")
    void testLockUserSuccess() throws Exception {
        User adminUser = new User();
        adminUser.setRole("ADMIN");

        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockSession.getAttribute("user")).thenReturn(adminUser);
        when(mockRequest.getParameter("id")).thenReturn("5");

        try (MockedConstruction<UserDao> mockedUserDao = Mockito.mockConstruction(UserDao.class)) {
            invokeDoGet();

            UserDao daoInstance = mockedUserDao.constructed().get(0);
            verify(daoInstance).updateStatus(5, "BANNED");
            verify(mockResponse).sendRedirect("users");
        }
    }
}
