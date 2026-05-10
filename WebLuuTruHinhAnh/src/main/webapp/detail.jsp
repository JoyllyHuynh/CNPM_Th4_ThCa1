<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title id="pageTitle">LensVault - ${image.fileName}</title>
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet" />
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght@100..700&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/variables.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/menu.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/detail.css">

    <style>
        /* CSS cho tính năng đổi tên */
        .edit-name-btn { background: none; border: none; cursor: pointer; color: #666; margin-left: 8px; display: flex; align-items: center; justify-content: center; border-radius: 4px; padding: 4px; transition: 0.2s; }
        .edit-name-btn:hover { background: #f0f0f0; color: #000; }
        .edit-input { width: 100%; padding: 6px 10px; border: 1px solid #ccc; border-radius: 6px; font-family: 'Inter', sans-serif; font-size: 14px; margin-top: 4px; box-sizing: border-box;}
        .edit-actions { display: flex; gap: 8px; margin-top: 10px; }
        .btn-save { padding: 6px 12px; background: #007bff; color: white; border: none; border-radius: 6px; cursor: pointer; font-size: 13px; font-weight: 500;}
        .btn-cancel { padding: 6px 12px; background: #e0e0e0; color: #333; border: none; border-radius: 6px; cursor: pointer; font-size: 13px; font-weight: 500;}
    </style>
</head>

<body>
<jsp:include page="/user/menu.jsp" />

<div class="detail-layout">
    <div class="detail-main">
        <div class="detail-topbar">
            <a href="${pageContext.request.contextPath}/Photos" class="back-btn">
                <span class="material-symbols-outlined" style="font-size:18px;">arrow_back</span>
                Quay lại
            </a>
            <div class="topbar-actions">
                <a href="${pageContext.request.contextPath}/DownloadServlet?id=${image.id}" class="action-btn download">
                    <span class="material-symbols-outlined" style="font-size:18px;">download</span>
                    Tải xuống
                </a>
                <button class="action-btn delete" onclick="confirmDelete(${image.id})">
                    <span class="material-symbols-outlined" style="font-size:18px;">delete</span>
                    Xóa ảnh
                </button>
            </div>
        </div>

        <div class="image-viewer">
            <img src="${pageContext.request.contextPath}/uploads/${image.filePath}" alt="${image.fileName}">
        </div>
    </div>

    <div class="detail-sidebar">
        <div class="sidebar-header">
            <h2 id="headerFileName">${image.fileName}</h2>
            <p>Thông tin chi tiết ảnh</p>
        </div>

        <div class="sidebar-thumb" style="padding-top:24px;">
            <img src="${pageContext.request.contextPath}/uploads/${image.filePath}" alt="${image.fileName}">
        </div>

        <div class="sidebar-body">
            <div class="info-item">
                <div class="info-icon">
                    <span class="material-symbols-outlined">calendar_month</span>
                </div>
                <div class="info-content">
                    <p class="info-label">Ngày upload</p>
                    <p class="info-value">${image.uploadDate}</p>
                </div>
            </div>

            <div class="divider"></div>

            <div class="info-item">
                <div class="info-icon">
                    <span class="material-symbols-outlined">folder</span>
                </div>
                <div class="info-content">
                    <p class="info-label">Dung lượng</p>
                    <p class="info-value">
                        <c:choose>
                            <c:when test="${image.fileSize >= 1048576}">
                                ${image.fileSize / 1048576} MB
                            </c:when>
                            <c:otherwise>
                                ${image.fileSize / 1024} KB
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
            </div>

            <div class="divider"></div>

            <div class="info-item" id="viewNameMode">
                <div class="info-icon">
                    <span class="material-symbols-outlined">image</span>
                </div>
                <div class="info-content" style="flex: 1; display: flex; justify-content: space-between; align-items: center;">
                    <div>
                        <p class="info-label">Tên file</p>
                        <p class="info-value" id="currentFileName">${image.fileName}</p>
                    </div>
                    <button class="edit-name-btn" onclick="toggleEditName()" title="Đổi tên ảnh">
                        <span class="material-symbols-outlined" style="font-size:18px;">edit</span>
                    </button>
                </div>
            </div>

            <div class="info-item" id="editNameMode" style="display: none;">
                <div class="info-icon">
                    <span class="material-symbols-outlined">edit_document</span>
                </div>
                <div class="info-content" style="flex: 1;">
                    <p class="info-label">Đổi tên file mới</p>
                    <input type="text" id="newFileNameInput" class="edit-input" value="${image.fileName}" />
                    <div class="edit-actions">
                        <button class="btn-save" onclick="saveImageName(${image.id})">Lưu</button>
                        <button class="btn-cancel" onclick="toggleEditName()">Hủy</button>
                    </div>
                </div>
            </div>

            <c:if test="${not empty image.description}">
                <div class="divider"></div>
                <div class="info-item">
                    <div class="info-icon">
                        <span class="material-symbols-outlined">description</span>
                    </div>
                    <div class="info-content">
                        <p class="info-label">Mô tả</p>
                        <p class="info-value desc">${image.description}</p>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</div>

<script>
    // Xóa ảnh
    function confirmDelete(id) {
        if (confirm('Bạn có chắc muốn xóa ảnh này không?')) {
            fetch('${pageContext.request.contextPath}/DeleteImage?id=' + id, { method: 'POST' })
                .then(res => {
                    if (res.ok) {
                        window.location.href = '${pageContext.request.contextPath}/Photos';
                    } else {
                        alert('Xóa thất bại, vui lòng thử lại!');
                    }
                });
        }
    }

    // Chuyển đổi giữa chế độ xem và sửa tên
    function toggleEditName() {
        const viewMode = document.getElementById('viewNameMode');
        const editMode = document.getElementById('editNameMode');

        if (viewMode.style.display === 'none') {
            viewMode.style.display = 'flex';
            editMode.style.display = 'none';
        } else {
            viewMode.style.display = 'none';
            editMode.style.display = 'flex';
            document.getElementById('newFileNameInput').focus();
        }
    }

    // Gọi Servlet để lưu tên mới
    function saveImageName(id) {
        const inputField = document.getElementById('newFileNameInput');
        const newName = inputField.value.trim();

        if (!newName) {
            alert('Tên file không được để trống!');
            inputField.focus();
            return;
        }

        const formData = new URLSearchParams();
        formData.append('id', id);
        formData.append('newName', newName);

        // Fetch gửi đến RenameImageServlet
        fetch('${pageContext.request.contextPath}/RenameImage', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: formData.toString()
        })
            .then(res => {
                if (res.ok) {
                    // Thành công: Cập nhật giao diện mà không cần reload trang
                    document.getElementById('currentFileName').innerText = newName;
                    document.getElementById('headerFileName').innerText = newName;
                    document.getElementById('pageTitle').innerText = 'LensVault - ' + newName;

                    toggleEditName();
                } else {
                    alert('Lưu thất bại, có thể do lỗi phía máy chủ!');
                }
            })
            .catch(err => {
                console.error(err);
                alert('Lỗi kết nối đến máy chủ!');
            });
    }
</script>
</body>
</html>