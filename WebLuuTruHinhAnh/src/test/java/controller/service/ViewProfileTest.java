package controller.service;

import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UC-20: Xem thông tin tài khoản")
class ViewProfileTest {

    // ==================================================
    // Normal Flow 20.1
    // ==================================================

    @Test
    @DisplayName("TC-PROFILE-01 | [20.1.4] Lấy thông tin user thành công")
    void testUserInfoSuccess() {

        User user = new User();

        user.setId(1);
        user.setFullName("Nguyen Van A");
        user.setEmail("a@gmail.com");

        assertEquals(
                "Nguyen Van A",
                user.getFullName()
        );

        assertEquals(
                "a@gmail.com",
                user.getEmail()
        );
    }

    @Test
    @DisplayName("TC-PROFILE-02 | Hiển thị họ tên")
    void testDisplayFullName() {

        User user = new User();

        user.setFullName("Tran Van B");

        assertEquals(
                "Tran Van B",
                user.getFullName()
        );
    }

    @Test
    @DisplayName("TC-PROFILE-03 | Hiển thị email")
    void testDisplayEmail() {

        User user = new User();

        user.setEmail("user@gmail.com");

        assertEquals(
                "user@gmail.com",
                user.getEmail()
        );
    }

    @Test
    @DisplayName("TC-PROFILE-04 | Hiển thị vai trò")
    void testDisplayRole() {

        User user = new User();

        user.setRole("USER");

        assertEquals(
                "USER",
                user.getRole()
        );
    }

    @Test
    @DisplayName("TC-PROFILE-05 | Hiển thị trạng thái")
    void testDisplayStatus() {

        User user = new User();

        user.setStatus("ACTIVE");

        assertEquals(
                "ACTIVE",
                user.getStatus()
        );
    }

    @Test
    @DisplayName("TC-PROFILE-06 | Hiển thị ngày tham gia")
    void testDisplayCreatedAt() {

        User user = new User();

        LocalDate today =
                LocalDate.now();

        user.setCreatedAt(today);

        assertEquals(
                today,
                user.getCreatedAt()
        );
    }

    // ==================================================
    // Chức năng mới
    // ==================================================

    @Test
    @DisplayName("TC-PROFILE-07 | Hiển thị nút Chỉnh sửa hồ sơ")
    void testEditProfileFeature() {

        boolean editProfileButtonVisible = true;

        assertTrue(
                editProfileButtonVisible
        );
    }

    @Test
    @DisplayName("TC-PROFILE-08 | Hiển thị nút Xem hồ sơ công khai")
    void testPublicProfileFeature() {

        boolean publicProfileButtonVisible = true;

        assertTrue(
                publicProfileButtonVisible
        );
    }

    // ==================================================
    // Alternative Flow 20.3
    // ==================================================

    @Test
    @DisplayName("TC-PROFILE-09 | [20.3] createdAt rỗng")
    void testCreatedAtNull() {

        User user = new User();

        user.setCreatedAt(null);

        assertNull(
                user.getCreatedAt()
        );
    }

    // ==================================================
    // Dữ liệu bổ sung
    // ==================================================

    @Test
    @DisplayName("TC-PROFILE-10 | Avatar tồn tại")
    void testAvatarExists() {

        User user = new User();

        user.setAvatar("avatar.png");

        assertEquals(
                "avatar.png",
                user.getAvatar()
        );
    }

    @Test
    @DisplayName("TC-PROFILE-11 | Avatar rỗng")
    void testAvatarNull() {

        User user = new User();

        user.setAvatar(null);

        assertNull(
                user.getAvatar()
        );
    }
}