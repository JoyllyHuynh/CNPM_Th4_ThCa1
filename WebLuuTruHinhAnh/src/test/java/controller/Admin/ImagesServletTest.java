package controller.Admin;

import DAO.ImageDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Image;
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

@DisplayName("UC-16: Xem danh sách ảnh toàn hệ thống (Admin)")
class ImagesServletTest {

    private ImagesServlet servlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private RequestDispatcher mockDispatcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new ImagesServlet();
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockDispatcher = mock(RequestDispatcher.class);
    }

    private void invokeDoGet() throws Exception {
        Method doGetMethod = ImagesServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(servlet, mockRequest, mockResponse);
    }

    @Test
    @DisplayName("Lấy danh sách ảnh thành công và forward tới images.jsp")
    void testViewImagesSuccess() throws Exception {
        List<Image> mockImages = new ArrayList<>();
        mockImages.add(new Image());
        mockImages.add(new Image());

        when(mockRequest.getRequestDispatcher("/Admin/images.jsp")).thenReturn(mockDispatcher);

        try (MockedConstruction<ImageDao> mockedDao = Mockito.mockConstruction(ImageDao.class, (mock, context) -> {
            when(mock.getAllImages()).thenReturn(mockImages);
        })) {
            invokeDoGet();

            verify(mockRequest).setAttribute("images", mockImages);
            verify(mockDispatcher).forward(mockRequest, mockResponse);
        }
    }
}
