package controller.AlbumImage;

import DAO.AlbumsDao;
import controller.service.AlbumsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UC-07: Tạo Album (AlbumsService)")
class CreateAlbumTest {

    private AlbumsService albumsService;
    private AlbumsDao mockAlbumsDao;

    @BeforeEach
    void setUp() throws Exception {

        albumsService = new AlbumsService();
        mockAlbumsDao = mock(AlbumsDao.class);

        Field field = AlbumsService.class.getDeclaredField("albumsDao");
        field.setAccessible(true);
        field.set(albumsService, mockAlbumsDao);
    }

    // ================================================================
    // Normal Flow 7.1 — Tạo album thành công
    // ================================================================

    @Test
    @DisplayName("TC-ALB-01 | [7.1.8] Tạo album thành công")
    void testCreateAlbumSuccess() {

        when(mockAlbumsDao.isAlbumNameExist(1, "Travel"))
                .thenReturn(false);

        when(mockAlbumsDao.createAlbum(1, "Travel"))
                .thenReturn(true);

        boolean result = albumsService.createAlbum(1, "Travel");

        assertTrue(result);

        verify(mockAlbumsDao)
                .createAlbum(1, "Travel");
    }

    // ================================================================
    // Alternate Flow 7.2 — Album đã tồn tại
    // ================================================================

    @Test
    @DisplayName("TC-ALB-02 | [7.2.1] Album đã tồn tại")
    void testCreateAlbumDuplicateName() {

        when(mockAlbumsDao.isAlbumNameExist(1, "Travel"))
                .thenReturn(true);

        boolean result =
                albumsService.createAlbum(1, "Travel");

        assertFalse(result);

        verify(mockAlbumsDao, never())
                .createAlbum(anyInt(), anyString());
    }

    // ================================================================
    // Exception — Tên album null
    // ================================================================

    @Test
    @DisplayName("TC-ALB-03 | Tên album null")
    void testCreateAlbumNullName() {

        boolean result =
                albumsService.createAlbum(1, null);

        assertFalse(result);

        verify(mockAlbumsDao, never())
                .createAlbum(anyInt(), anyString());
    }

    // ================================================================
    // Exception — Tên album rỗng
    // ================================================================

    @Test
    @DisplayName("TC-ALB-04 | Tên album rỗng")
    void testCreateAlbumEmptyName() {

        boolean result =
                albumsService.createAlbum(1, "");

        assertFalse(result);

        verify(mockAlbumsDao, never())
                .createAlbum(anyInt(), anyString());
    }

    // ================================================================
    // Exception — Tên album chỉ chứa khoảng trắng
    // ================================================================

    @Test
    @DisplayName("TC-ALB-05 | Tên album chỉ chứa khoảng trắng")
    void testCreateAlbumBlankName() {

        boolean result =
                albumsService.createAlbum(1, "     ");

        assertFalse(result);

        verify(mockAlbumsDao, never())
                .createAlbum(anyInt(), anyString());
    }
}