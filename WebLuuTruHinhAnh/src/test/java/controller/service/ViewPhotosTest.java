package controller.service;

import DAO.ImageDao;
import model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UC-11: Xem ảnh")
class ViewPhotosTest {

    private ImageService imageService;
    private ImageDao mockImageDao;

    @BeforeEach
    void setUp() throws Exception {

        imageService = new ImageService();

        mockImageDao = mock(ImageDao.class);

        Field field =
                ImageService.class.getDeclaredField("imgd");

        field.setAccessible(true);
        field.set(imageService, mockImageDao);
    }

    // ==================================================
    // Normal Flow 11.1
    // ==================================================

    @Test
    @DisplayName("TC-VIEW-01 | [11.1.4] Xem danh sách ảnh thành công")
    void testGetImagesSuccess() {

        Image img1 = new Image();
        img1.setId(1);

        Image img2 = new Image();
        img2.setId(2);

        when(mockImageDao.getImagesSorted(1, "newest"))
                .thenReturn(Arrays.asList(img1, img2));

        List<Image> result =
                imageService.getImagesSorted(1, "newest");

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("TC-VIEW-02 | Sort newest")
    void testSortNewest() {

        when(mockImageDao.getImagesSorted(1, "newest"))
                .thenReturn(Collections.emptyList());

        imageService.getImagesSorted(1, "newest");

        verify(mockImageDao)
                .getImagesSorted(1, "newest");
    }

    @Test
    @DisplayName("TC-VIEW-03 | Sort oldest")
    void testSortOldest() {

        when(mockImageDao.getImagesSorted(1, "oldest"))
                .thenReturn(Collections.emptyList());

        imageService.getImagesSorted(1, "oldest");

        verify(mockImageDao)
                .getImagesSorted(1, "oldest");
    }

    @Test
    @DisplayName("TC-VIEW-04 | Sort A-Z")
    void testSortNameAZ() {

        when(mockImageDao.getImagesSorted(1, "nameAz"))
                .thenReturn(Collections.emptyList());

        imageService.getImagesSorted(1, "nameAz");

        verify(mockImageDao)
                .getImagesSorted(1, "nameAz");
    }

    @Test
    @DisplayName("TC-VIEW-05 | Sort Z-A")
    void testSortNameZA() {

        when(mockImageDao.getImagesSorted(1, "nameZa"))
                .thenReturn(Collections.emptyList());

        imageService.getImagesSorted(1, "nameZa");

        verify(mockImageDao)
                .getImagesSorted(1, "nameZa");
    }

    // ==================================================
    // Alternative Flow
    // ==================================================

    @Test
    @DisplayName("TC-VIEW-06 | [11.3] Danh sách ảnh rỗng")
    void testEmptyImageList() {

        when(mockImageDao.getImagesSorted(1, "newest"))
                .thenReturn(Collections.emptyList());

        List<Image> result =
                imageService.getImagesSorted(1, "newest");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("TC-VIEW-07 | sortBy null => newest")
    void testSortByNull() {

        when(mockImageDao.getImagesSorted(1, "newest"))
                .thenReturn(Collections.emptyList());

        imageService.getImagesSorted(1, null);

        verify(mockImageDao)
                .getImagesSorted(1, "newest");
    }

    // ==================================================
    // Chức năng mới
    // ==================================================

    @Test
    @DisplayName("TC-VIEW-08 | Hiển thị PUBLIC")
    void testPublicVisibility() {

        Image image = new Image();
        image.setVisibility("PUBLIC");

        when(mockImageDao.getImagesSorted(1, "newest"))
                .thenReturn(List.of(image));

        String result =
                imageService
                        .getImagesSorted(1, "newest")
                        .get(0)
                        .getVisibility();

        assertEquals("PUBLIC", result);
    }

    @Test
    @DisplayName("TC-VIEW-09 | Hiển thị PRIVATE")
    void testPrivateVisibility() {

        Image image = new Image();
        image.setVisibility("PRIVATE");

        when(mockImageDao.getImagesSorted(1, "newest"))
                .thenReturn(List.of(image));

        String result =
                imageService
                        .getImagesSorted(1, "newest")
                        .get(0)
                        .getVisibility();

        assertEquals("PRIVATE", result);
    }

    @Test
    @DisplayName("TC-VIEW-10 | Download count > 0")
    void testDownloadCount() {

        Image image = new Image();
        image.setDownloadCount(15);

        when(mockImageDao.getImagesSorted(1, "newest"))
                .thenReturn(List.of(image));

        int result =
                imageService
                        .getImagesSorted(1, "newest")
                        .get(0)
                        .getDownloadCount();

        assertEquals(15, result);
    }

    @Test
    @DisplayName("TC-VIEW-11 | Download count = 0")
    void testDownloadCountZero() {

        Image image = new Image();
        image.setDownloadCount(0);

        when(mockImageDao.getImagesSorted(1, "newest"))
                .thenReturn(List.of(image));

        int result =
                imageService
                        .getImagesSorted(1, "newest")
                        .get(0)
                        .getDownloadCount();

        assertEquals(0, result);
    }
}
