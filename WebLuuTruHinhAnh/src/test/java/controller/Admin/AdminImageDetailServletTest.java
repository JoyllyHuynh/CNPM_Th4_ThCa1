package controller.Admin;

import controller.service.ImageService;
import controller.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Image;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

@DisplayName("UC-21: Xem chi tiết ảnh hệ thống (Admin)")
class AdminImageDetailServletTest {

    private AdminImageDetailServlet servlet;

    @Mock
    private ImageService mockImageService;

    @Mock
    private UserService mockUserService;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private HttpSession mockSession;

    @Mock
    private RequestDispatcher mockDispatcher;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        servlet = new AdminImageDetailServlet();

        Field imageServiceField = AdminImageDetailServlet.class.getDeclaredField("imageService");
        imageServiceField.setAccessible(true);
        imageServiceField.set(servlet, mockImageService);

        Field userServiceField = AdminImageDetailServlet.class.getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(servlet, mockUserService);

        when(mockRequest.getContextPath()).thenReturn("/app");
    }

    private void invokeDoGet() throws Exception {
        Method doGetMethod = AdminImageDetailServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
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
    @DisplayName("Thiếu ID ảnh -> Chuyển hướng về danh sách ảnh")
    void testMissingIdRedirectsToImages() throws Exception {
        User adminUser = new User();
        adminUser.setRole("ADMIN");
        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockSession.getAttribute("user")).thenReturn(adminUser);
        
        when(mockRequest.getParameter("id")).thenReturn(null);

        invokeDoGet();

        verify(mockResponse).sendRedirect("/app/admin/images");
    }

    @Test
    @DisplayName("ID ảnh không hợp lệ -> Chuyển hướng về danh sách ảnh")
    void testInvalidIdRedirectsToImages() throws Exception {
        User adminUser = new User();
        adminUser.setRole("ADMIN");
        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockSession.getAttribute("user")).thenReturn(adminUser);

        when(mockRequest.getParameter("id")).thenReturn("abc");

        invokeDoGet();

        verify(mockResponse).sendRedirect("/app/admin/images");
    }

    @Test
    @DisplayName("ID hợp lệ nhưng không tìm thấy ảnh -> Chuyển hướng về danh sách ảnh")
    void testImageNotFoundRedirectsToImages() throws Exception {
        User adminUser = new User();
        adminUser.setRole("ADMIN");
        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockSession.getAttribute("user")).thenReturn(adminUser);

        when(mockRequest.getParameter("id")).thenReturn("99");
        when(mockImageService.getImageById(99)).thenReturn(null);

        invokeDoGet();

        verify(mockResponse).sendRedirect("/app/admin/images");
    }

    @Test
    @DisplayName("Xem chi tiết ảnh thành công -> Forward tới JSP")
    void testViewImageDetailSuccess() throws Exception {
        User adminUser = new User();
        adminUser.setRole("ADMIN");
        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockSession.getAttribute("user")).thenReturn(adminUser);

        when(mockRequest.getParameter("id")).thenReturn("1");

        Image mockImage = new Image();
        mockImage.setId(1);
        mockImage.setUserId(2);

        User mockUploader = new User();
        mockUploader.setId(2);
        mockUploader.setFullName("Nguyen Van A");

        when(mockImageService.getImageById(1)).thenReturn(mockImage);
        when(mockUserService.getUserById(2)).thenReturn(mockUploader);
        when(mockRequest.getRequestDispatcher("/Admin/image-detail.jsp")).thenReturn(mockDispatcher);

        invokeDoGet();

        verify(mockRequest).setAttribute("image", mockImage);
        verify(mockRequest).setAttribute("uploader", mockUploader);
        verify(mockDispatcher).forward(mockRequest, mockResponse);
    }
}
