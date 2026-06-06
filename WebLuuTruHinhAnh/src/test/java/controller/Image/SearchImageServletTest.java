package controller.Image;

import controller.service.ImageService;
import model.Image;
import model.User;
import jakarta.servlet.RequestDispatcher;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("UC-03: Tìm kiếm ảnh (SearchImageServlet) - Actual Logic Test")
class SearchImageServletTest {

    private SearchImageServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private ImageService mockImageService;

    private User mockUser;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        servlet = new SearchImageServlet();

        // Sử dụng Java Reflection để thay thế ImageService thật bằng MockImageService
        Field field = SearchImageServlet.class.getDeclaredField("imageService");
        field.setAccessible(true);
        field.set(servlet, mockImageService);

        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        mockUser = new User();
        mockUser.setId(1);
    }

    // ================================================================
    // Exception Flow 3.4: Chưa đăng nhập
    // ================================================================

    @Test
    @DisplayName("TC-SEA-07 | [3.4] User chưa đăng nhập (User null) -> Redirect về login.jsp")
    void testUserNotLoggedIn() throws ServletException, IOException {
        when(session.getAttribute("user")).thenReturn(null);
        when(request.getContextPath()).thenReturn("/app");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/app/login.jsp");
        verify(mockImageService, never()).searchByKW(anyInt(), anyString());
    }

    // ================================================================
    // Normal Flow 3.1: Từ khóa hợp lệ
    // ================================================================

    @Test
    @DisplayName("TC-SEA-01 | [3.1] Keyword hợp lệ ('Beach') -> Gọi searchByKW và Forward về image.jsp")
    void testValidKeyword() throws ServletException, IOException {
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("keyword")).thenReturn("Beach");

        List<Image> mockList = List.of(new Image(), new Image());
        when(mockImageService.searchByKW(1, "Beach")).thenReturn(mockList);

        servlet.doGet(request, response);

        verify(mockImageService).searchByKW(1, "Beach");
        verify(request).setAttribute("searchKeyword", "Beach");
        verify(request).setAttribute("images", mockList);
        verify(request).setAttribute("isSearchResult", true);
        verify(request).setAttribute("activeTopNav", "photos");
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("TC-SEA-01b | [3.1.6] Keyword chứa khoảng trắng thừa -> Bị trim trước khi xử lý")
    void testKeywordWithWhitespace() throws ServletException, IOException {
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("keyword")).thenReturn("   Beach   ");

        List<Image> mockList = List.of(new Image());
        when(mockImageService.searchByKW(1, "Beach")).thenReturn(mockList);

        servlet.doGet(request, response);

        verify(mockImageService).searchByKW(1, "Beach");
        verify(request).setAttribute("searchKeyword", "Beach");
        verify(dispatcher).forward(request, response);
    }

    // ================================================================
    // Alternative Flow 3.2: Từ khóa rỗng
    // ================================================================

    @Test
    @DisplayName("TC-SEA-02 | [3.2] Keyword rỗng '' -> Không gọi DB, trả về mảng rỗng")
    void testEmptyKeyword() throws ServletException, IOException {
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("keyword")).thenReturn("");

        servlet.doGet(request, response);

        verify(mockImageService, never()).searchByKW(anyInt(), anyString());
        // Verify that 'images' attribute is set with an empty list
        verify(request).setAttribute(eq("images"), eq(List.of()));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("TC-SEA-03 | [3.2] Keyword chỉ có khoảng trắng '   ' -> Không gọi DB, trả về mảng rỗng")
    void testBlankKeyword() throws ServletException, IOException {
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("keyword")).thenReturn("   ");

        servlet.doGet(request, response);

        verify(mockImageService, never()).searchByKW(anyInt(), anyString());
        verify(request).setAttribute(eq("images"), eq(List.of()));
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("TC-SEA-03b | [3.2] Keyword bị null -> Không gọi DB, trả về mảng rỗng")
    void testNullKeyword() throws ServletException, IOException {
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("keyword")).thenReturn(null);

        servlet.doGet(request, response);

        verify(mockImageService, never()).searchByKW(anyInt(), anyString());
        verify(request).setAttribute(eq("images"), eq(List.of()));
        verify(dispatcher).forward(request, response);
    }
}
