<%@ page contentType="text/html; charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                <title>LensVault - Hồ sơ của tôi</title>
                <link rel="preconnect" href="https://fonts.googleapis.com" />
                <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap"
                    rel="stylesheet" />
                <link
                    href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght@100..700&display=swap"
                    rel="stylesheet" />
                <link rel="stylesheet"
                      href="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.6.2/cropper.min.css">

                <script src="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.6.2/cropper.min.js"></script>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/variables.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/menu.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/header.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/profile.css">
            </head>

            <body>
                <c:set var="activeTopNav" value="profile" />
                <jsp:include page="/user/menu.jsp" />
                <jsp:include page="/user/header.jsp" />

                <div id="editModal" class="modal">

                    <div class="modal-content">

                        <div class="modal-header">
                            <h3>Chỉnh sửa hồ sơ</h3>

                            <span class="close-btn"
                                  onclick="closeEditModal()">
            &times;
        </span>
                        </div>

                        <form action="${pageContext.request.contextPath}/UpdateProfile"
                              method="post"
                              enctype="multipart/form-data">

                            <div class="section-title">
                                Thông tin cá nhân
                            </div>

                            <div class="form-group">
                                <label>Ảnh đại diện</label>

                                <input type="file"
                                       id="avatarInput"
                                       name="avatar"
                                       accept="image/*">

                                <img id="avatarPreview">
                            </div>

                            <div class="form-group">
                                <label>Họ và tên</label>

                                <input type="text"
                                       name="fullName"
                                       value="${sessionScope.user.fullName}"
                                       required>
                            </div>

                            <div class="form-group">
                                <label>Email</label>

                                <input type="email"
                                       name="email"
                                       value="${sessionScope.user.email}"
                                       required>
                            </div>

                            <div class="section-title password-section">
                                Đổi mật khẩu (không bắt buộc)
                            </div>

                            <div class="form-group">
                                <label>Mật khẩu hiện tại</label>

                                <input type="password"
                                       name="currentPassword"
                                       placeholder="Nhập mật khẩu hiện tại">
                            </div>

                            <div class="form-group">
                                <label>Mật khẩu mới</label>

                                <input type="password"
                                       name="newPassword"
                                       placeholder="Nhập mật khẩu mới">
                            </div>

                            <div class="form-group">
                                <label>Xác nhận mật khẩu mới</label>

                                <input type="password"
                                       name="confirmPassword"
                                       placeholder="Nhập lại mật khẩu mới">
                            </div>

                            <div class="modal-actions">

                                <button type="button"
                                        class="btn-cancel"
                                        onclick="closeEditModal()">
                                    Hủy
                                </button>

                                <button type="submit"
                                        class="save-btn">
                                    Lưu thay đổi
                                </button>

                            </div>

                        </form>

                    </div>

                </div>

                <div class="main-wrapper">
                    <main class="main-canvas">

                        <div class="page-header-box">
                            <div class="left-title">
                                <h1 class="page-title">My Profile</h1>
                                <p class="page-subtitle">View and manage your account information.</p>
                            </div>
                        </div>

                        <div class="profile-layout">

                            <!-- [20.1.6] Render Avatar Card (avatar/chữ cái đầu, họ tên, email, badge vai trò) -->
                            <!-- Avatar card -->
                            <div class="profile-avatar-card">
                                <div class="avatar-circle">

                                    <c:choose>

                                        <c:when test="${not empty sessionScope.user.avatar}">
                                            <img
                                                    src="${pageContext.request.contextPath}/uploads/avatar/${sessionScope.user.avatar}"
                                                    alt="Avatar"
                                                    class="avatar-image">
                                        </c:when>

                                        <c:otherwise>
                                            ${fn:toUpperCase(fn:substring(sessionScope.user.fullName,0,1))}
                                        </c:otherwise>

                                    </c:choose>

                                </div>
                                <h2 class="avatar-name">${sessionScope.user.fullName}</h2>
                                <p class="avatar-email">${sessionScope.user.email}</p>
                                <span class="role-badge ${sessionScope.user.role == 'ADMIN' ? 'admin' : 'user'}">
                                    ${sessionScope.user.role == 'ADMIN' ? 'Quản trị viên' : 'Thành viên'}
                                </span>

                                <!-- [20.1.9] Hiển thị nút "Xem hồ sơ công khai" -->
                                <div class="public-profile-btn-wrap">

                                    <a href="${pageContext.request.contextPath}/UserProfile?id=${sessionScope.user.id}"
                                       class="public-profile-btn">

                                        👁 Xem hồ sơ công khai

                                    </a>

                                </div>
                            </div>

                            <!-- [20.1.7] Render Info Card (họ tên, email, vai trò, trạng thái, ngày tham gia) -->
                            <!-- Info card -->
                            <div class="profile-info-card">
                                <h3 class="info-card-title">Thông tin chi tiết</h3>

                                <div class="info-row">
                                    <div class="info-icon-wrap">
                                        <span class="material-symbols-outlined">person</span>
                                    </div>
                                    <div class="info-detail">
                                        <p class="info-label">Họ và tên</p>
                                        <p class="info-value">${sessionScope.user.fullName}</p>
                                    </div>
                                </div>

                                <div class="info-row">
                                    <div class="info-icon-wrap">
                                        <span class="material-symbols-outlined">email</span>
                                    </div>
                                    <div class="info-detail">
                                        <p class="info-label">Email</p>
                                        <p class="info-value">${sessionScope.user.email}</p>
                                    </div>
                                </div>

                                <div class="info-row">
                                    <div class="info-icon-wrap">
                                        <span class="material-symbols-outlined">shield_person</span>
                                    </div>
                                    <div class="info-detail">
                                        <p class="info-label">Vai trò</p>
                                        <p class="info-value">${sessionScope.user.role == 'ADMIN' ? 'Quản trị viên' :
                                            'Thành viên'}</p>
                                    </div>
                                </div>

                                <div class="info-row">
                                    <div class="info-icon-wrap">
                                        <span class="material-symbols-outlined">verified_user</span>
                                    </div>
                                    <div class="info-detail">
                                        <p class="info-label">Trạng thái tài khoản</p>
                                        <p class="info-value">
                                            <span
                                                class="status-dot ${sessionScope.user.status == 'ACTIVE' ? 'active' : 'inactive'}"></span>
                                            ${sessionScope.user.status == 'ACTIVE' ? 'Đang hoạt động' :
                                            sessionScope.user.status}
                                        </p>
                                    </div>
                                </div>

                                <!-- [Luồng 20.3] createdAt rỗng -->
                                <!-- [20.3.1] Phát hiện createdAt rỗng hoặc không tồn tại -->
                                <c:if test="${not empty sessionScope.user.createdAt}">
                                    <!-- [20.3.2] Ẩn dòng "Ngày tham gia" (Ngược lại, nếu có thì hiển thị) -->
                                    <div class="info-row">
                                        <div class="info-icon-wrap">
                                            <span class="material-symbols-outlined">calendar_month</span>
                                        </div>
                                        <div class="info-detail">
                                            <p class="info-label">Ngày tham gia</p>
                                            <p class="info-value">${sessionScope.user.createdAt}</p>
                                        </div>
                                    </div>
                                </c:if>

                                <div style="margin-top:20px">
                                    <!-- [20.1.8] Hiển thị nút "Chỉnh sửa hồ sơ" -->
                                    <button type="button"
                                            class="btn-primary"
                                            onclick="openEditModal()">
                                        Chỉnh sửa hồ sơ
                                    </button>
                                </div>

                            </div>
                        </div>

                    </main>
                </div>

                <script>

                    let cropper;

                    document
                        .getElementById("avatarInput")
                        .addEventListener("change", function(e){

                            const file = e.target.files[0];

                            if(!file) return;

                            const reader = new FileReader();

                            reader.onload = function(ev){

                                const img =
                                    document.getElementById("avatarPreview");

                                img.src = ev.target.result;
                                img.style.display = "block";

                                if(cropper){
                                    cropper.destroy();
                                }

                                cropper = new Cropper(img,{
                                    aspectRatio:1,
                                    viewMode:1,
                                    dragMode:'move',
                                    autoCropArea:1,
                                    cropBoxResizable:false,
                                    cropBoxMovable:false
                                });
                            };

                            reader.readAsDataURL(file);
                        });

                    function openEditModal(){
                        document.getElementById("editModal")
                            .style.display = "flex";
                    }

                    function closeEditModal(){
                        document.getElementById("editModal")
                            .style.display = "none";
                    }

                    window.onclick = function(event){

                        const modal =
                            document.getElementById("editModal");

                        if(event.target === modal){
                            closeEditModal();
                        }
                    }

                </script>
            </body>
            </html>