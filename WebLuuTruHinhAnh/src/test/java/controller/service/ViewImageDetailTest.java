package controller.service;

import DAO.ImageDao;
import DAO.UserDao;
import model.Image;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UC-09: Xem thông tin ảnh")
class ViewImageDetailTest {

    private ImageService imageService;
    private UserService userService;

    private ImageDao mockImageDao;
    private UserDao mockUserDao;

    @BeforeEach
    void setUp() throws Exception {

        imageService = new ImageService();
        userService = new UserService();

        mockImageDao = mock(ImageDao.class);
        mockUserDao = mock(UserDao.class);

        Field imageDaoField =
                ImageService.class.getDeclaredField("imgd");

        imageDaoField.setAccessible(true);
        imageDaoField.set(imageService, mockImageDao);

        Field userDaoField =
                UserService.class.getDeclaredField("userDao");

        userDaoField.setAccessible(true);
        userDaoField.set(userService, mockUserDao);
    }

    // ==================================================
    // Normal Flow 9.1
    // ==================================================

    @Test
    @DisplayName("TC-DETAIL-01 | [9.1.5] Lấy ảnh theo id thành công")
    void testGetImageByIdSuccess() {

        Image image = new Image();
        image.setId(10);
        image.setFileName("beach.jpg");

        when(mockImageDao.findById(10))
                .thenReturn(image);

        Image result =
                imageService.getImageById(10);

        assertNotNull(result);
        assertEquals(10, result.getId());
    }

    @Test
    @DisplayName("TC-DETAIL-02 | [9.1.7] Lấy uploader thành công")
    void testGetUploaderSuccess() {

        User uploader = new User();
        uploader.setId(5);
        uploader.setFullName("Nguyen Van A");

        when(mockUserDao.getUserById(5))
                .thenReturn(uploader);

        User result =
                userService.getUserById(5);

        assertNotNull(result);
        assertEquals(5, result.getId());
    }

    @Test
    @DisplayName("TC-DETAIL-03 | [9.1.8] Lấy danh sách imageIds thành công")
    void testGetImageIdsByUserIdSuccess() {

        when(mockImageDao.getImageIdsByUserId(5))
                .thenReturn(Arrays.asList(1,2,3));

        assertEquals(
                3,
                imageService.getImageIdsByUserId(5).size()
        );
    }

    // ==================================================
    // Alternative Flow 9.4
    // ==================================================

    @Test
    @DisplayName("TC-DETAIL-04 | [9.4] Ảnh không tồn tại")
    void testImageNotFound() {

        when(mockImageDao.findById(999))
                .thenReturn(null);

        Image result =
                imageService.getImageById(999);

        assertNull(result);
    }

    @Test
    @DisplayName("TC-DETAIL-05 | [9.4] Uploader không tồn tại")
    void testUploaderNotFound() {

        when(mockUserDao.getUserById(999))
                .thenReturn(null);

        User result =
                userService.getUserById(999);

        assertNull(result);
    }

    @Test
    @DisplayName("TC-DETAIL-06 | [9.1.8] Danh sách ảnh rỗng")
    void testEmptyImageIds() {

        when(mockImageDao.getImageIdsByUserId(5))
                .thenReturn(Collections.emptyList());

        assertTrue(
                imageService
                        .getImageIdsByUserId(5)
                        .isEmpty()
        );
    }

    // ==================================================
    // Chức năng mới
    // ==================================================

    @Test
    @DisplayName("TC-DETAIL-07 | Hiển thị ảnh PUBLIC")
    void testPublicImage() {

        Image image = new Image();
        image.setVisibility("PUBLIC");

        when(mockImageDao.findById(1))
                .thenReturn(image);

        assertEquals(
                "PUBLIC",
                imageService.getImageById(1)
                        .getVisibility()
        );
    }

    @Test
    @DisplayName("TC-DETAIL-08 | Hiển thị ảnh PRIVATE")
    void testPrivateImage() {

        Image image = new Image();
        image.setVisibility("PRIVATE");

        when(mockImageDao.findById(2))
                .thenReturn(image);

        assertEquals(
                "PRIVATE",
                imageService.getImageById(2)
                        .getVisibility()
        );
    }

    @Test
    @DisplayName("TC-DETAIL-09 | Hiển thị download count")
    void testDownloadCount() {

        Image image = new Image();
        image.setDownloadCount(25);

        when(mockImageDao.findById(3))
                .thenReturn(image);

        assertEquals(
                25,
                imageService.getImageById(3)
                        .getDownloadCount()
        );
    }
}