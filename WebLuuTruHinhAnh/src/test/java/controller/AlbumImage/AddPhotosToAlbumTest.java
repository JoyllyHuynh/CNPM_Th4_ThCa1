package controller.service;

import DAO.AlbumsDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UC-10: Thêm ảnh vào Album (AlbumsService)")
class AddPhotosToAlbumTest {

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
    // Normal Flow 10.1
    // ================================================================

    @Test
    @DisplayName("TC-ADD-01 | [10.1.8] Thêm ảnh vào album thành công")
    void testAddPhotosSuccess() {

        when(mockAlbumsDao.addPhotosToAlbum(
                1,
                5,
                Arrays.asList(10,11)))
                .thenReturn("SUCCESS");

        String result =
                albumsService.addPhotosToAlbum(
                        1,
                        5,
                        Arrays.asList(10,11));

        assertEquals("SUCCESS", result);
    }

    // ================================================================
    // Alternate Flow 10.3
    // ================================================================

    @Test
    @DisplayName("TC-ADD-02 | [10.3] Danh sách ảnh rỗng")
    void testAddPhotosEmptyList() {

        String result =
                albumsService.addPhotosToAlbum(
                        1,
                        5,
                        Collections.emptyList());

        assertEquals(
                "Vui lòng chọn ít nhất một ảnh.",
                result);
    }
}