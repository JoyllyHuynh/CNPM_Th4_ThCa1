package controller.service;

import DAO.ImageeDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UC-19: Xóa ảnh khỏi Album (ImagService)")
class RemovePhotosFromAlbumTest {

    private ImagService imagService;
    private ImageeDao mockImageDao;

    @BeforeEach
    void setUp() throws Exception {

        imagService = new ImagService();
        mockImageDao = mock(ImageeDao.class);

        Field field = ImagService.class.getDeclaredField("imageDao");
        field.setAccessible(true);
        field.set(imagService, mockImageDao);
    }

    // ================================================================
    // Normal Flow 19.1
    // ================================================================

    @Test
    @DisplayName("TC-REM-01 | [19.1.9] Xóa ảnh khỏi album thành công")
    void testRemovePhotosSuccess() {

        when(mockImageDao.removePhotosFromAlbum(
                1,
                5,
                Arrays.asList(10,11)))
                .thenReturn(true);

        boolean result =
                imagService.removePhotosFromAlbum(
                        1,
                        5,
                        Arrays.asList(10,11));

        assertTrue(result);
    }

    // ================================================================
    // Alternate Flow 19.3
    // ================================================================

    @Test
    @DisplayName("TC-REM-02 | [19.3.2] Ảnh không tồn tại trong album")
    void testRemovePhotosNotFound() {

        when(mockImageDao.removePhotosFromAlbum(
                1,
                5,
                Arrays.asList(999)))
                .thenReturn(false);

        boolean result =
                imagService.removePhotosFromAlbum(
                        1,
                        5,
                        Arrays.asList(999));

        assertFalse(result);
    }

    // ================================================================
    // Alternate Flow 19.4
    // ================================================================

    @Test
    @DisplayName("TC-REM-03 | [19.4.2] Không có quyền thao tác")
    void testRemovePhotosNoPermission() {

        when(mockImageDao.removePhotosFromAlbum(
                2,
                5,
                Arrays.asList(10)))
                .thenReturn(false);

        boolean result =
                imagService.removePhotosFromAlbum(
                        2,
                        5,
                        Arrays.asList(10));

        assertFalse(result);
    }
}