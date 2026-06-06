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

@DisplayName("UC-01: Sắp xếp ảnh (SortImageServlet) - Actual Logic Test")
class SortImageServletTest {

    private SortImageServlet servlet;

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
        servlet = new SortImageServlet();

        // Sử dụng Java Reflection để thay thế ImageService thật bằng MockImageService
        Field field = SortImageServlet.class.getDeclaredField("imageService");
        field.setAccessible(true);
        field.set(servlet, mockImageService);

        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        mockUser = new User();
        mockUser.setId(1);
    }

    // ================================================================
    // Exception 1.3: Chưa đăng nhập
    // ================================================================

    @Test
    @DisplayName("TC-SRT-06 | [1.3] User chưa đăng nhập (Session null) -> Redirect về login.jsp")
    void testUserNotLoggedIn_SessionNull() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/app");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/app/login.jsp");
        verify(mockImageService, never()).getImagesSorted(anyInt(), anyString());
    }

    // ================================================================
    // Normal Flow 1.1 & Alternative Flow 1.2
    // ================================================================

    @Test
    @DisplayName("TC-SRT-01 | [1.1] User hợp lệ, sortBy='oldest' -> Sắp xếp theo oldest")
    void testSortByOldest() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("sortBy")).thenReturn("oldest");

        List<Image> mockList = List.of(new Image(), new Image());
        when(mockImageService.getImagesSorted(1, "oldest")).thenReturn(mockList);

        servlet.doGet(request, response);

        verify(request).setAttribute("images", mockList);
        verify(request).setAttribute("currentSort", "oldest");
        verify(request).setAttribute("activeTopNav", "photos");
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("TC-SRT-05 | [1.2] sortBy=null -> Tự động fallback về 'newest'")
    void testSortByNull_FallbackToNewest() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("sortBy")).thenReturn(null);

        List<Image> mockList = List.of(new Image());
        // Servlet chuyển thẳng sortBy (null) xuống ImageService
        when(mockImageService.getImagesSorted(1, null)).thenReturn(mockList);

        servlet.doGet(request, response);

        verify(request).setAttribute("images", mockList);
        verify(request).setAttribute("currentSort", "newest");
        verify(dispatcher).forward(request, response);
    }
}
