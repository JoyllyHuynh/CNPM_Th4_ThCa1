package controller.Image;

import controller.service.ImageService;
import model.Image;
import model.User;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

@DisplayName("UC-12: Tải ảnh (DownloadServlet) - Actual Logic Test")
class DownloadServletTest {

    private DownloadServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private ServletContext servletContext;

    @Mock
    private ImageService mockImageService;

    private User mockUser;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        servlet = spy(new DownloadServlet()); // dùng spy để mock getServletContext()
        
        doReturn(servletContext).when(servlet).getServletContext();
        when(servletContext.getRealPath("/uploads")).thenReturn("C:/test_uploads");

        Field field = DownloadServlet.class.getDeclaredField("imageService");
        field.setAccessible(true);
        field.set(servlet, mockImageService);

        mockUser = new User();
        mockUser.setId(1);
    }

    // ================================================================
    // Exception 12.3: Chưa đăng nhập
    // ================================================================

    @Test
    @DisplayName("TC-DWN-05 | [12.3] User chưa đăng nhập (Session null) -> Redirect về login.jsp")
    void testUserNotLoggedIn_SessionNull() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/app");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/app/login.jsp");
        verify(mockImageService, never()).getImageById(anyInt());
    }

    @Test
    @DisplayName("TC-DWN-05b | [12.3] User null trong Session -> Redirect về login.jsp")
    void testUserNotLoggedIn_UserNullInSession() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);
        when(request.getContextPath()).thenReturn("");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/login.jsp");
    }

    // ================================================================
    // Exception 12.4: Mã ID không hợp lệ
    // ================================================================

    @Test
    @DisplayName("TC-DWN-06 | [12.4] id='abc' (NumberFormatException) -> Bỏ qua tải ảnh, không crash")
    void testInvalidId_NotNumber() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        
        when(request.getParameter("id")).thenReturn("abc");

        servlet.doGet(request, response);

        // Hệ thống sẽ bắt ngoại lệ (e.printStackTrace()) và dừng lại
        // Mockito xác nhận getImageById KHÔNG bao giờ được gọi
        verify(mockImageService, never()).getImageById(anyInt());
    }

    // ================================================================
    // Exception 12.5: Ảnh không tồn tại
    // ================================================================

    @Test
    @DisplayName("TC-DWN-07 | [12.5] Ảnh không tồn tại trong DB -> Không làm gì cả")
    void testImageNotFoundInDb() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("id")).thenReturn("999");
        
        when(mockImageService.getImageById(999)).thenReturn(null);

        servlet.doGet(request, response);

        verify(mockImageService).getImageById(999);
        // Không gửi error, không lấy real path
        verify(servlet, never()).getServletContext();
    }

    // ================================================================
    // Exception 12.6: IDOR - Truy cập trái phép
    // ================================================================

    @Test
    @DisplayName("TC-DWN-08 | [12.6] Tải ảnh của người khác (IDOR) -> HTTP 403 Forbidden")
    void testIdor_AccessDenied() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser); // ID = 1
        when(request.getParameter("id")).thenReturn("5");

        Image mockImage = new Image();
        mockImage.setId(5);
        mockImage.setUserId(2); // Ảnh của user khác (ID = 2)
        when(mockImageService.getImageById(5)).thenReturn(mockImage);

        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền tải bức ảnh này vì bạn không phải là chủ sở hữu.");
    }

    // ================================================================
    // Alternative Flow 12.2: File không tồn tại vật lý
    // ================================================================

    @Test
    @DisplayName("TC-DWN-04 | [12.2] File không tồn tại trên Server -> HTTP 404")
    void testFileNotFoundPhysically() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser); // ID = 1
        when(request.getParameter("id")).thenReturn("5");

        Image mockImage = new Image();
        mockImage.setId(5);
        mockImage.setUserId(1); // Ảnh của chính user
        mockImage.setFilePath("non_existent_file.png");
        when(mockImageService.getImageById(5)).thenReturn(mockImage);

        servlet.doGet(request, response);

        verify(response).sendError(404, "File không tồn tại trên server!");
    }
}
