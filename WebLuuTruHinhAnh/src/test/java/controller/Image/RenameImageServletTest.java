package controller.Image;

import controller.service.ImageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("UC-13: Chỉnh sửa ảnh (RenameImageServlet) - Actual Logic Test")
class RenameImageServletTest {

    private RenameImageServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ImageService mockImageService;

    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        servlet = new RenameImageServlet();

        // Sử dụng Java Reflection để thay thế ImageService thật bằng MockImageService
        Field field = RenameImageServlet.class.getDeclaredField("imageService");
        field.setAccessible(true);
        field.set(servlet, mockImageService);

        // Chuẩn bị Mock PrintWriter để lấy đầu ra của response
        responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    // ================================================================
    // Exception 13.2 — Dữ liệu rỗng / null
    // ================================================================

    @Test
    @DisplayName("TC-REN-06 | [13.2] newName rỗng '' -> Bad Request 400")
    void testEmptyName() throws ServletException, IOException {
        when(request.getParameter("id")).thenReturn("5");
        when(request.getParameter("newName")).thenReturn("");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(mockImageService, never()).renameImage(anyInt(), anyString());
    }

    @Test
    @DisplayName("TC-REN-07 | [13.2] newName chỉ toàn khoảng trắng -> Bad Request 400")
    void testBlankName() throws ServletException, IOException {
        when(request.getParameter("id")).thenReturn("5");
        when(request.getParameter("newName")).thenReturn("   ");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("TC-REN-06b | [13.2] id bị thiếu (null) -> Bad Request 400")
    void testNullId() throws ServletException, IOException {
        when(request.getParameter("id")).thenReturn(null);
        when(request.getParameter("newName")).thenReturn("NewName");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    // ================================================================
    // Exception 13.3 — Parse ID NumberFormatException
    // ================================================================

    @Test
    @DisplayName("TC-REN-08 | [13.3] id không phải số nguyên ('abc') -> Bad Request 400")
    void testIdNotNumber() throws ServletException, IOException {
        when(request.getParameter("id")).thenReturn("abc");
        when(request.getParameter("newName")).thenReturn("NewName");

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    // ================================================================
    // Exception 13.4 — DB Error
    // ================================================================

    @Test
    @DisplayName("TC-DBE | [13.4] Lỗi cập nhật CSDL (ImageService trả về false) -> HTTP 500")
    void testDbFailure() throws ServletException, IOException {
        when(request.getParameter("id")).thenReturn("5");
        when(request.getParameter("newName")).thenReturn("Beach");
        
        when(mockImageService.renameImage(5, "Beach.png")).thenReturn(false);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    // ================================================================
    // Normal Flow 13.1 — Extension Auto-Append & Thành công
    // ================================================================

    @Test
    @DisplayName("TC-REN-01 | [13.1.8] Đổi tên thành công (Tên không có .png sẽ được tự thêm .png)")
    void testRenameSuccess_AutoAppendExtension() throws ServletException, IOException {
        when(request.getParameter("id")).thenReturn("5");
        when(request.getParameter("newName")).thenReturn("Summer_Vibe");
        
        when(mockImageService.renameImage(5, "Summer_Vibe.png")).thenReturn(true);

        servlet.doPost(request, response);

        verify(response).setContentType("text/plain");
        verify(response, atLeastOnce()).setCharacterEncoding("UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        
        // Assert kết quả ghi ra luồng
        assertEquals("Summer_Vibe.png", responseWriter.toString());
    }

    @Test
    @DisplayName("TC-REN-02 | [13.1.7] Tên đã có .png -> Không nối thêm, Đổi tên thành công")
    void testRenameSuccess_AlreadyHasExtension() throws ServletException, IOException {
        when(request.getParameter("id")).thenReturn("5");
        when(request.getParameter("newName")).thenReturn("Beach.png");
        
        when(mockImageService.renameImage(5, "Beach.png")).thenReturn(true);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals("Beach.png", responseWriter.toString());
    }

    @Test
    @DisplayName("TC-REN-05b | [13.1.6] Tên có khoảng trắng -> Bị trim trước khi xử lý")
    void testRenameSuccess_WithWhitespace() throws ServletException, IOException {
        when(request.getParameter("id")).thenReturn("5");
        when(request.getParameter("newName")).thenReturn("   Sunrise   ");
        
        when(mockImageService.renameImage(5, "Sunrise.png")).thenReturn(true);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals("Sunrise.png", responseWriter.toString());
    }
}
