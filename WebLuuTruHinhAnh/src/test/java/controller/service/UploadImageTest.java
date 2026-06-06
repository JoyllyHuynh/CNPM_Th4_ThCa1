package controller.service;

import DAO.ImageDao;
import model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UC-15: Upload Ảnh")
class UploadImageTest {

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
    // Normal Flow 15.1
    // ==================================================

    @Test
    @DisplayName("TC-UPLOAD-01 | [15.1.12] Upload ảnh thành công")
    void testUploadSuccess() {

        Image image = new Image();

        image.setUserId(1);
        image.setFileName("beach.jpg");
        image.setFilePath("uuid_beach.jpg");

        imageService.uploadImage(image);

        verify(mockImageDao, times(1))
                .insertImage(image);
    }

    @Test
    @DisplayName("TC-UPLOAD-02 | Lưu đúng fileName")
    void testFileName() {

        Image image = new Image();

        image.setFileName("summer.png");

        imageService.uploadImage(image);

        verify(mockImageDao)
                .insertImage(image);

        assertEquals(
                "summer.png",
                image.getFileName()
        );
    }

    @Test
    @DisplayName("TC-UPLOAD-03 | Lưu đúng filePath")
    void testFilePath() {

        Image image = new Image();

        image.setFilePath(
                "123e4567_photo.png"
        );

        imageService.uploadImage(image);

        verify(mockImageDao)
                .insertImage(image);

        assertEquals(
                "123e4567_photo.png",
                image.getFilePath()
        );
    }

    @Test
    @DisplayName("TC-UPLOAD-04 | Lưu đúng userId")
    void testUserId() {

        Image image = new Image();

        image.setUserId(5);

        imageService.uploadImage(image);

        verify(mockImageDao)
                .insertImage(image);

        assertEquals(
                5,
                image.getUserId()
        );
    }

    // ==================================================
    // Chức năng mới: Visibility
    // ==================================================

    @Test
    @DisplayName("TC-UPLOAD-05 | Upload ảnh PUBLIC")
    void testUploadPublicImage() {

        Image image = new Image();

        image.setVisibility("PUBLIC");

        imageService.uploadImage(image);

        verify(mockImageDao)
                .insertImage(image);

        assertEquals(
                "PUBLIC",
                image.getVisibility()
        );
    }

    @Test
    @DisplayName("TC-UPLOAD-06 | Upload ảnh PRIVATE")
    void testUploadPrivateImage() {

        Image image = new Image();

        image.setVisibility("PRIVATE");

        imageService.uploadImage(image);

        verify(mockImageDao)
                .insertImage(image);

        assertEquals(
                "PRIVATE",
                image.getVisibility()
        );
    }

    // ==================================================
    // Dữ liệu ảnh
    // ==================================================

    @Test
    @DisplayName("TC-UPLOAD-07 | Description có dữ liệu")
    void testDescription() {

        Image image = new Image();

        image.setDescription(
                "Ảnh đi du lịch Đà Nẵng"
        );

        imageService.uploadImage(image);

        verify(mockImageDao)
                .insertImage(image);

        assertEquals(
                "Ảnh đi du lịch Đà Nẵng",
                image.getDescription()
        );
    }

    @Test
    @DisplayName("TC-UPLOAD-08 | Description rỗng")
    void testEmptyDescription() {

        Image image = new Image();

        image.setDescription("");

        imageService.uploadImage(image);

        verify(mockImageDao)
                .insertImage(image);

        assertEquals(
                "",
                image.getDescription()
        );
    }

    @Test
    @DisplayName("TC-UPLOAD-09 | Lưu uploadDate")
    void testUploadDate() {

        Image image = new Image();

        image.setUploadDate(
                LocalDate.now()
        );

        imageService.uploadImage(image);

        verify(mockImageDao)
                .insertImage(image);

        assertNotNull(
                image.getUploadDate()
        );
    }

    @Test
    @DisplayName("TC-UPLOAD-10 | Lưu fileSize")
    void testFileSize() {

        Image image = new Image();

        image.setFileSize(
                1024 * 1024
        );

        imageService.uploadImage(image);

        verify(mockImageDao)
                .insertImage(image);

        assertEquals(
                1024 * 1024,
                image.getFileSize()
        );
    }
}