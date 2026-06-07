package controller.Image;

import DAO.ImageDao;
import controller.service.ImageService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

@DisplayName("UC-22: Tải ảnh hệ thống (Admin)")
class DownloadServletTest {

    private DownloadServlet servlet;

    @Mock
    private ImageService mockImageService;

    @Mock
    private ImageDao mockImageDao;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private ServletContext mockServletContext;

    @Mock
    private ServletOutputStream mockOutputStream;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        
        // Use spy to mock getServletContext()
        servlet = spy(new DownloadServlet());
        doReturn(mockServletContext).when(servlet).getServletContext();

        Field imageServiceField = DownloadServlet.class.getDeclaredField("imageService");
        imageServiceField.setAccessible(true);
        imageServiceField.set(servlet, mockImageService);

        Field imageDaoField = DownloadServlet.class.getDeclaredField("imageDao");
        imageDaoField.setAccessible(true);
        imageDaoField.set(servlet, mockImageDao);
    }

    private void invokeDoGet() throws Exception {
        Method doGetMethod = DownloadServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(servlet, mockRequest, mockResponse);
    }

    @Test
    @DisplayName("Tải ảnh thành công")
    void testDownloadSuccess() throws Exception {
        when(mockRequest.getParameter("id")).thenReturn("1");

        Image mockImage = new Image();
        mockImage.setId(1);
        mockImage.setFileName("test.jpg");
        mockImage.setFilePath("test.jpg");
        when(mockImageService.getImageById(1)).thenReturn(mockImage);

        // Tạo thư mục tạm thời và file tạm để test
        String tempDir = System.getProperty("java.io.tmpdir");
        File testFile = new File(tempDir, "test.jpg");
        try (FileOutputStream fos = new FileOutputStream(testFile)) {
            fos.write("dummy content".getBytes());
        }

        when(mockServletContext.getRealPath("/uploads")).thenReturn(tempDir);
        when(mockResponse.getOutputStream()).thenReturn(mockOutputStream);

        invokeDoGet();

        verify(mockResponse).setContentType("application/octet-stream");
        verify(mockResponse).setHeader(eq("Content-Disposition"), contains("filename=\"test.jpg\""));
        verify(mockImageDao).increaseDownloadCount(1);
        
        // Clean up
        testFile.delete();
    }

    @Test
    @DisplayName("Ảnh không tồn tại trên hệ thống file -> 404")
    void testDownloadFileNotFound() throws Exception {
        when(mockRequest.getParameter("id")).thenReturn("2");

        Image mockImage = new Image();
        mockImage.setId(2);
        mockImage.setFileName("not_exist.jpg");
        mockImage.setFilePath("not_exist.jpg");
        when(mockImageService.getImageById(2)).thenReturn(mockImage);

        when(mockServletContext.getRealPath("/uploads")).thenReturn("C:/fake/path");

        invokeDoGet();

        verify(mockResponse).sendError(404, "File không tồn tại trên server!");
    }

    @Test
    @DisplayName("Tham số id sai định dạng -> Không gọi service")
    void testDownloadInvalidId() throws Exception {
        when(mockRequest.getParameter("id")).thenReturn("abc");

        invokeDoGet();

        verify(mockImageService, never()).getImageById(anyInt());
    }
}
