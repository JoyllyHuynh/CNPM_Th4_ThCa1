<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Profile</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght@100..700&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.6.2/cropper.min.css">

    <script src="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.6.2/cropper.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/sidebar.css?v=2">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/variables.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/profile.css">

    <style>
        .profile-layout {
            margin-top: 0;
        }
        .page-header-box {
            padding: 0;
            margin-bottom: 24px;
        }
        body {
            display: block !important;
        }
    </style>
</head>
<body>

<%@ include file="/Admin/sidebar.jsp" %>

<div id="editModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Chỉnh sửa hồ sơ</h3>
            <span class="close-btn" onclick="closeEditModal()">&times;</span>
        </div>
        <form action="${pageContext.request.contextPath}/UpdateProfile" method="post" enctype="multipart/form-data">
            <div class="section-title">Thông tin cá nhân</div>
            <div class="form-group">
                <label>Ảnh đại diện</label>
                <input type="file" id="avatarInput" name="avatar" accept="image/*">
                <img id="avatarPreview">
            </div>
            <div class="form-group">
                <label>Họ và tên</label>
                <input type="text" name="fullName" value="${sessionScope.user.fullName}" required>
            </div>
            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" value="${sessionScope.user.email}" required>
            </div>
            <div class="section-title password-section">Đổi mật khẩu (không bắt buộc)</div>
            <div class="form-group">
                <label>Mật khẩu hiện tại</label>
                <input type="password" name="currentPassword" placeholder="Nhập mật khẩu hiện tại">
            </div>
            <div class="form-group">
                <label>Mật khẩu mới</label>
                <input type="password" name="newPassword" placeholder="Nhập mật khẩu mới">
            </div>
            <div class="form-group">
                <label>Xác nhận mật khẩu mới</label>
                <input type="password" name="confirmPassword" placeholder="Nhập lại mật khẩu mới">
            </div>
            <div class="modal-actions">
                <button type="button" class="btn-cancel" onclick="closeEditModal()">Hủy</button>
                <button type="submit" class="save-btn">Lưu thay đổi</button>
            </div>
        </form>
    </div>
</div>

<div class="main">
    <div class="topbar">
        <h2>Hồ sơ cá nhân</h2>
    </div>

    <div class="content">
        <div class="profile-layout">
            <!-- Avatar card -->
            <div class="profile-avatar-card">
                <div class="avatar-circle">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user.avatar}">
                            <img src="${pageContext.request.contextPath}/uploads/avatar/${sessionScope.user.avatar}" alt="Avatar" class="avatar-image">
                        </c:when>
                        <c:otherwise>
                            ${fn:toUpperCase(fn:substring(sessionScope.user.fullName,0,1))}
                        </c:otherwise>
                    </c:choose>
                </div>
                <h2 class="avatar-name">${sessionScope.user.fullName}</h2>
                <p class="avatar-email">${sessionScope.user.email}</p>
                <span class="role-badge admin">Quản trị viên</span>
            </div>

            <!-- Info card -->
            <div class="profile-info-card">
                <h3 class="info-card-title">Thông tin chi tiết</h3>

                <div class="info-row">
                    <div class="info-icon-wrap"><span class="material-symbols-outlined">person</span></div>
                    <div class="info-detail">
                        <p class="info-label">Họ và tên</p>
                        <p class="info-value">${sessionScope.user.fullName}</p>
                    </div>
                </div>

                <div class="info-row">
                    <div class="info-icon-wrap"><span class="material-symbols-outlined">email</span></div>
                    <div class="info-detail">
                        <p class="info-label">Email</p>
                        <p class="info-value">${sessionScope.user.email}</p>
                    </div>
                </div>

                <div class="info-row">
                    <div class="info-icon-wrap"><span class="material-symbols-outlined">shield_person</span></div>
                    <div class="info-detail">
                        <p class="info-label">Vai trò</p>
                        <p class="info-value">Quản trị viên</p>
                    </div>
                </div>

                <div class="info-row">
                    <div class="info-icon-wrap"><span class="material-symbols-outlined">verified_user</span></div>
                    <div class="info-detail">
                        <p class="info-label">Trạng thái tài khoản</p>
                        <p class="info-value">
                            <span class="status-dot ${sessionScope.user.status == 'ACTIVE' ? 'active' : 'inactive'}"></span>
                            ${sessionScope.user.status == 'ACTIVE' ? 'Đang hoạt động' : sessionScope.user.status}
                        </p>
                    </div>
                </div>

                <c:if test="${not empty sessionScope.user.createdAt}">
                    <div class="info-row">
                        <div class="info-icon-wrap"><span class="material-symbols-outlined">calendar_month</span></div>
                        <div class="info-detail">
                            <p class="info-label">Ngày tham gia</p>
                            <p class="info-value">${sessionScope.user.createdAt}</p>
                        </div>
                    </div>
                </c:if>

                <div style="margin-top:20px">
                    <button type="button" class="btn-primary" onclick="openEditModal()">Chỉnh sửa hồ sơ</button>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    let cropper;

    document.getElementById("avatarInput").addEventListener("change", function(e){
        const file = e.target.files[0];
        if(!file) return;

        const reader = new FileReader();
        reader.onload = function(ev){
            const img = document.getElementById("avatarPreview");
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
        document.getElementById("editModal").style.display = "flex";
    }

    function closeEditModal(){
        document.getElementById("editModal").style.display = "none";
    }

    window.onclick = function(event){
        const modal = document.getElementById("editModal");
        if(event.target === modal){
            closeEditModal();
        }
    }
</script>
</body>
</html>
