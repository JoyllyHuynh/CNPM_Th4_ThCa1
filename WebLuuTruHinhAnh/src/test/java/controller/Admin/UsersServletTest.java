package controller.Admin;

import DAO.UserDao;
import jakarta.servlet.RequestDispatcher;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@DisplayName("UC-18: Xem danh sách người dùng (Admin) & Thuật toán Letter Avatar")
class UsersServletTest {

    private UsersServlet servlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private HttpSession mockSession;
    private RequestDispatcher mockDispatcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new UsersServlet();
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockSession = mock(HttpSession.class);
        mockDispatcher = mock(RequestDispatcher.class);
    }

    private void invokeDoGet() throws Exception {
        Method doGetMethod = UsersServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(servlet, mockRequest, mockResponse);
    }

    @Test
    @DisplayName("Lấy danh sách User thành công (Đã bao gồm cấu trúc Avatar trống để sinh Letter Avatar trên JSP)")
    void testViewUsersSuccess() throws Exception {
        User adminUser = new User();
        adminUser.setRole("ADMIN");

        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockSession.getAttribute("user")).thenReturn(adminUser);
        when(mockRequest.getRequestDispatcher("/Admin/users.jsp")).thenReturn(mockDispatcher);

        List<User> mockUsers = new ArrayList<>();
        User userWithoutAvatar = new User();
        userWithoutAvatar.setFullName("Nguyen Van A");
        userWithoutAvatar.setAvatar(null); // Để test logic Letter Avatar trên JSP ('N')
        mockUsers.add(userWithoutAvatar);

        try (MockedConstruction<UserDao> mockedDao = Mockito.mockConstruction(UserDao.class, (mock, context) -> {
            when(mock.getAllUsers()).thenReturn(mockUsers);
        })) {
            invokeDoGet();

            verify(mockRequest).setAttribute("users", mockUsers);
            verify(mockDispatcher).forward(mockRequest, mockResponse);
        }
    }
}
