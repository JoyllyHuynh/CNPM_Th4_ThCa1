package controller.Album;

import DAO.AlbumsDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UC-08: Xóa Album (AlbumsService)")
class DeleteAlbumTest {

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
    // Normal Flow 8.1
    // ================================================================

    @Test
    @DisplayName("TC-DEL-01 | [2.1.9] Xóa album thành công")
    void testDeleteAlbumSuccess() {

        when(mockAlbumsDao.deleteAlbum(1, 10))
                .thenReturn(true);

        boolean result =
                albumsService.deleteAlbum(1, 10);

        assertTrue(result);
    }

    // ================================================================
    // Alternate Flow 8.4
    // ================================================================

    @Test
    @DisplayName("TC-DEL-02 | [2.4.2] Album không tồn tại")
    void testDeleteAlbumNotFound() {

        when(mockAlbumsDao.deleteAlbum(1, 999))
                .thenReturn(false);

        boolean result =
                albumsService.deleteAlbum(1, 999);

        assertFalse(result);
    }

    @Test
    @DisplayName("TC-DEL-03 | [2.4.2] Không có quyền xóa album")
    void testDeleteAlbumNoPermission() {

        when(mockAlbumsDao.deleteAlbum(2, 10))
                .thenReturn(false);

        boolean result =
                albumsService.deleteAlbum(2, 10);

        assertFalse(result);
    }
}