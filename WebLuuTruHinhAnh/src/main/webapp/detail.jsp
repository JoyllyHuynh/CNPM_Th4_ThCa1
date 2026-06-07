<%--<%@ page contentType="text/html; charset=UTF-8" %>--%>
<%--<%@ taglib prefix="c" uri="jakarta.tags.core" %>--%>
<%--<!DOCTYPE html>--%>
<%--<html lang="vi">--%>

<%--<head>--%>
<%--    <meta charset="UTF-8"/>--%>
<%--    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>--%>
<%--    <title id="pageTitle">LensVault - ${image.fileName}</title>--%>
<%--    <link rel="preconnect" href="https://fonts.googleapis.com"/>--%>
<%--    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet"/>--%>
<%--    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght@100..700&display=swap"--%>
<%--          rel="stylesheet"/>--%>
<%--    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/variables.css">--%>
<%--    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/menu.css">--%>
<%--    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/detail.css">--%>

<%--    <style>--%>
<%--        /* CSS cho tính năng đổi tên */--%>
<%--        .edit-name-btn {--%>
<%--            background: none;--%>
<%--            border: none;--%>
<%--            cursor: pointer;--%>
<%--            color: #666;--%>
<%--            margin-left: 8px;--%>
<%--            display: flex;--%>
<%--            align-items: center;--%>
<%--            justify-content: center;--%>
<%--            border-radius: 4px;--%>
<%--            padding: 4px;--%>
<%--            transition: 0.2s;--%>
<%--        }--%>

<%--        .edit-name-btn:hover {--%>
<%--            background: #f0f0f0;--%>
<%--            color: #000;--%>
<%--        }--%>

<%--        .edit-input {--%>
<%--            width: 100%;--%>
<%--            padding: 6px 10px;--%>
<%--            border: 1px solid #ccc;--%>
<%--            border-radius: 6px;--%>
<%--            font-family: 'Inter', sans-serif;--%>
<%--            font-size: 14px;--%>
<%--            margin-top: 4px;--%>
<%--            box-sizing: border-box;--%>
<%--        }--%>

<%--        .edit-actions {--%>
<%--            display: flex;--%>
<%--            gap: 8px;--%>
<%--            margin-top: 10px;--%>
<%--        }--%>

<%--        .btn-save {--%>
<%--            padding: 6px 12px;--%>
<%--            background: #007bff;--%>
<%--            color: white;--%>
<%--            border: none;--%>
<%--            border-radius: 6px;--%>
<%--            cursor: pointer;--%>
<%--            font-size: 13px;--%>
<%--            font-weight: 500;--%>
<%--        }--%>

<%--        .btn-cancel {--%>
<%--            padding: 6px 12px;--%>
<%--            background: #e0e0e0;--%>
<%--            color: #333;--%>
<%--            border: none;--%>
<%--            border-radius: 6px;--%>
<%--            cursor: pointer;--%>
<%--            font-size: 13px;--%>
<%--            font-weight: 500;--%>
<%--        }--%>
<%--    </style>--%>
<%--</head>--%>

<%--<body>--%>
<%--<jsp:include page="/user/menu.jsp"/>--%>

<%--<div class="detail-layout">--%>
<%--    <div class="detail-main">--%>
<%--        <div class="detail-topbar">--%>
<%--            <a href="${pageContext.request.contextPath}/Photos" class="back-btn">--%>
<%--                <span class="material-symbols-outlined" style="font-size:18px;">arrow_back</span>--%>
<%--                Quay lại--%>
<%--            </a>--%>
<%--            <div class="topbar-actions">--%>
<%--                <a href="${pageContext.request.contextPath}/DownloadServlet?id=${image.id}" class="action-btn download">--%>
<%--                    <span class="material-symbols-outlined" style="font-size:18px;">download</span>--%>
<%--                    Tải xuống--%>
<%--                </a>--%>
<%--                <button class="action-btn delete" onclick="confirmDelete(${image.id})">--%>
<%--                    <span class="material-symbols-outlined" style="font-size:18px;">delete</span>--%>
<%--                    Xóa ảnh--%>
<%--                </button>--%>
<%--            </div>--%>
<%--        </div>--%>

<%--        <div class="image-viewer">--%>
<%--            <div id="imageWrapper">--%>
<%--                <img id="mainImage" src="${pageContext.request.contextPath}/uploads/${image.filePath}" alt="${image.fileName}">--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>

<%--    <div class="detail-sidebar">--%>
<%--        <div class="sidebar-header">--%>
<%--            <h2 id="headerFileName">${image.fileName}</h2>--%>
<%--            <p>Thông tin chi tiết ảnh</p>--%>
<%--        </div>--%>

<%--        <div class="sidebar-thumb" style="padding-top:24px;">--%>
<%--            <img src="${pageContext.request.contextPath}/uploads/${image.filePath}" alt="${image.fileName}">--%>
<%--        </div>--%>

<%--        <div class="sidebar-body">--%>

<%--            <div class="info-item">--%>
<%--                <div class="info-icon">--%>
<%--                    <span class="material-symbols-outlined">calendar_month</span>--%>
<%--                </div>--%>
<%--                <div class="info-content">--%>
<%--                    <p class="info-label">Ngày upload</p>--%>
<%--                    <p class="info-value">${image.uploadDate}</p>--%>
<%--                </div>--%>
<%--            </div>--%>

<%--            <div class="divider"></div>--%>

<%--            <div class="info-item">--%>
<%--                <div class="info-icon">--%>
<%--                    <span class="material-symbols-outlined">folder</span>--%>
<%--                </div>--%>
<%--                <div class="info-content">--%>
<%--                    <p class="info-label">Dung lượng</p>--%>
<%--                    <p class="info-value">--%>
<%--                        <c:choose>--%>
<%--                            <c:when test="${image.fileSize >= 1048576}">--%>
<%--                                ${image.fileSize / 1048576} MB--%>
<%--                            </c:when>--%>
<%--                            <c:otherwise>--%>
<%--                                ${image.fileSize / 1024} KB--%>
<%--                            </c:otherwise>--%>
<%--                        </c:choose>--%>
<%--                    </p>--%>
<%--                </div>--%>
<%--            </div>--%>

<%--            <div class="divider"></div>--%>

<%--            <div class="info-item" id="viewNameMode">--%>
<%--                <div class="info-icon">--%>
<%--                    <span class="material-symbols-outlined">image</span>--%>
<%--                </div>--%>
<%--                <div class="info-content"--%>
<%--                     style="flex: 1; display: flex; justify-content: space-between; align-items: center;">--%>
<%--                    <div>--%>
<%--                        <p class="info-label">Tên file</p>--%>
<%--                        <p class="info-value" id="currentFileName">${image.fileName}</p>--%>
<%--                    </div>--%>
<%--                    <button class="edit-name-btn" onclick="toggleEditName()" title="Đổi tên ảnh">--%>
<%--                        <span class="material-symbols-outlined" style="font-size:18px;">edit</span>--%>
<%--                    </button>--%>
<%--                </div>--%>
<%--            </div>--%>

<%--            <div class="info-item" id="editNameMode" style="display: none;">--%>
<%--                <div class="info-icon">--%>
<%--                    <span class="material-symbols-outlined">edit_document</span>--%>
<%--                </div>--%>
<%--                <div class="info-content" style="flex: 1;">--%>
<%--                    <p class="info-label">Đổi tên file mới</p>--%>
<%--                    <input type="text" id="newFileNameInput" class="edit-input" value="${image.fileName}"/>--%>
<%--                    <div class="edit-actions">--%>
<%--                        <button class="btn-save" onclick="saveImageName(${image.id})">Lưu</button>--%>
<%--                        <button class="btn-cancel" onclick="toggleEditName()">Hủy</button>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </div>--%>

<%--            <c:if test="${not empty image.description}">--%>
<%--                <div class="divider"></div>--%>
<%--                <div class="info-item">--%>
<%--                    <div class="info-icon">--%>
<%--                        <span class="material-symbols-outlined">description</span>--%>
<%--                    </div>--%>
<%--                    <div class="info-content">--%>
<%--                        <p class="info-label">Mô tả</p>--%>
<%--                        <p class="info-value desc">${image.description}</p>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </c:if>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>

<%--<script>--%>
<%--    // Xóa ảnh--%>
<%--    function confirmDelete(id) {--%>
<%--        if (confirm('Bạn có chắc muốn xóa ảnh này không?')) {--%>
<%--            fetch('${pageContext.request.contextPath}/DeleteImage?id=' + id, {method: 'POST'})--%>
<%--                .then(res => {--%>
<%--                    if (res.ok) {--%>
<%--                        window.location.href = '${pageContext.request.contextPath}/Photos';--%>
<%--                    } else {--%>
<%--                        alert('Xóa thất bại, vui lòng thử lại!');--%>
<%--                    }--%>
<%--                });--%>
<%--        }--%>
<%--    }--%>

<%--    // Chuyển đổi giữa chế độ xem và sửa tên--%>
<%--    function toggleEditName() {--%>
<%--        const viewMode = document.getElementById('viewNameMode');--%>
<%--        const editMode = document.getElementById('editNameMode');--%>

<%--        if (viewMode.style.display === 'none') {--%>
<%--            viewMode.style.display = 'flex';--%>
<%--            editMode.style.display = 'none';--%>
<%--        } else {--%>
<%--            viewMode.style.display = 'none';--%>
<%--            editMode.style.display = 'flex';--%>
<%--            document.getElementById('newFileNameInput').focus();--%>
<%--        }--%>
<%--    }--%>

<%--    // Gọi Servlet để lưu tên mới--%>
<%--    function saveImageName(id) {--%>
<%--        const inputField = document.getElementById('newFileNameInput');--%>
<%--        const newName = inputField.value.trim();--%>

<%--        if (!newName) {--%>
<%--            alert('Tên file không được để trống!');--%>
<%--            inputField.focus();--%>
<%--            return;--%>
<%--        }--%>

<%--        const formData = new URLSearchParams();--%>
<%--        formData.append('id', id);--%>
<%--        formData.append('newName', newName);--%>

<%--        // Fetch gửi đến RenameImageServlet--%>
<%--        fetch('${pageContext.request.contextPath}/RenameImage', {--%>
<%--            method: 'POST',--%>
<%--            headers: {--%>
<%--                'Content-Type': 'application/x-www-form-urlencoded',--%>
<%--            },--%>
<%--            body: formData.toString()--%>
<%--        })--%>
<%--            .then(res => {--%>
<%--                if (res.ok) {--%>
<%--                    // Thành công: Cập nhật giao diện mà không cần reload trang--%>
<%--                    document.getElementById('currentFileName').innerText = newName;--%>
<%--                    document.getElementById('headerFileName').innerText = newName;--%>
<%--                    document.getElementById('pageTitle').innerText = 'LensVault - ' + newName;--%>

<%--                    toggleEditName();--%>
<%--                } else {--%>
<%--                    alert('Lưu thất bại, có thể do lỗi phía máy chủ!');--%>
<%--                }--%>
<%--            })--%>
<%--            .catch(err => {--%>
<%--                console.error(err);--%>
<%--                alert('Lỗi kết nối đến máy chủ!');--%>
<%--            });--%>
<%--    }--%>
<%--    const img = document.getElementById("mainImage");--%>
<%--    const wrapper = document.getElementById("imageWrapper");--%>
<%--    const viewer = document.querySelector(".image-viewer");--%>

<%--    let scale = 1;--%>
<%--    let panX = 0;--%>
<%--    let panY = 0;--%>
<%--    let isDragging = false;--%>
<%--    let lastMouseX, lastMouseY;--%>

<%--    function applyTransform() {--%>
<%--        wrapper.style.transform = "translate(" + panX + "px, " + panY + "px) scale(" + scale + ")";--%>
<%--        --%>
<%--        // In ra console để kiểm tra giá trị scale và tọa độ--%>
<%--        console.log("[Zoom/Pan] Scale: " + scale.toFixed(2) + "x | Pan (X: " + panX.toFixed(2) + "px, Y: " + panY.toFixed(2) + "px)");--%>
<%--        --%>
<%--        // In ra kích thước thực tế của ảnh sau khi bị biến đổi--%>
<%--        const rect = wrapper.getBoundingClientRect();--%>
<%--        console.log("[Zoom/Pan] Kích thước khung hiển thị thực: " + rect.width.toFixed(1) + "px x " + rect.height.toFixed(1) + "px");--%>
<%--    }--%>

<%--    function resetTransform() {--%>
<%--        scale = 1;--%>
<%--        panX = 0;--%>
<%--        panY = 0;--%>
<%--        applyTransform();--%>
<%--    }--%>

<%--    // Reset khi ảnh load xong--%>
<%--    img.addEventListener("load", resetTransform);--%>
<%--    --%>
<%--    // Nếu ảnh đã load xong trước khi sự kiện load được gán--%>
<%--    if (img.complete) {--%>
<%--        resetTransform();--%>
<%--    }--%>

<%--    // Zoom bằng wheel--%>
<%--    viewer.addEventListener("wheel", (e) => {--%>
<%--        e.preventDefault();--%>

<%--        const vr = viewer.getBoundingClientRect();--%>
<%--        --%>
<%--        // Tọa độ chuột so với viewer--%>
<%--        const mx = e.clientX - vr.left;--%>
<%--        const my = e.clientY - vr.top;--%>
<%--        --%>
<%--        // Tọa độ tâm của viewer (nơi wrapper được center mặc định)--%>
<%--        const cx = vr.width / 2;--%>
<%--        const cy = vr.height / 2;--%>

<%--        // Vector từ tâm viewer đến chuột--%>
<%--        const dx = mx - cx;--%>
<%--        const dy = my - cy;--%>

<%--        const zoomFactor = e.deltaY < 0 ? 1.1 : 0.9;--%>
<%--        const newScale = Math.min(10, Math.max(0.2, scale * zoomFactor));--%>

<%--        // Tỉ lệ thay đổi scale--%>
<%--        const ratio = newScale / scale;--%>
<%--        --%>
<%--        // Cập nhật panX, panY để giữ điểm dưới chuột đứng yên--%>
<%--        panX = dx - (dx - panX) * ratio;--%>
<%--        panY = dy - (dy - panY) * ratio;--%>
<%--        scale = newScale;--%>

<%--        img.style.cursor = scale > 1 ? "grab" : "zoom-in";--%>
<%--        applyTransform();--%>
<%--    }, { passive: false });--%>

<%--    // Drag để pan--%>
<%--    viewer.addEventListener("mousedown", (e) => {--%>
<%--        if (scale > 1) {--%>
<%--            isDragging = true;--%>
<%--            lastMouseX = e.clientX;--%>
<%--            lastMouseY = e.clientY;--%>
<%--            img.style.cursor = "grabbing";--%>
<%--            e.preventDefault(); // Ngăn hành vi drag ảnh mặc định của trình duyệt--%>
<%--        }--%>
<%--    });--%>

<%--    document.addEventListener("mousemove", (e) => {--%>
<%--        if (!isDragging) return;--%>
<%--        panX += e.clientX - lastMouseX;--%>
<%--        panY += e.clientY - lastMouseY;--%>
<%--        lastMouseX = e.clientX;--%>
<%--        lastMouseY = e.clientY;--%>
<%--        --%>
<%--        // Tạm thời tắt transition khi đang kéo để mượt hơn--%>
<%--        wrapper.style.transition = "none";--%>
<%--        applyTransform();--%>
<%--    });--%>

<%--    document.addEventListener("mouseup", () => {--%>
<%--        if (!isDragging) return;--%>
<%--        isDragging = false;--%>
<%--        img.style.cursor = scale > 1 ? "grab" : "zoom-in";--%>
<%--        // Bật lại transition--%>
<%--        wrapper.style.transition = "transform 0.05s ease-out";--%>
<%--    });--%>
<%--    --%>
<%--    // Xử lý khi chuột rời khỏi vùng viewer lúc đang kéo--%>
<%--    viewer.addEventListener("mouseleave", () => {--%>
<%--        if (isDragging) {--%>
<%--            isDragging = false;--%>
<%--            img.style.cursor = scale > 1 ? "grab" : "zoom-in";--%>
<%--            wrapper.style.transition = "transform 0.05s ease-out";--%>
<%--        }--%>
<%--    });--%>

<%--    // Double click reset--%>
<%--    viewer.addEventListener("dblclick", () => {--%>
<%--        resetTransform();--%>
<%--        img.style.cursor = "zoom-in";--%>
<%--    });--%>
<%--</script>--%>
<%--</body>--%>
<%--</html>--%>


<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title id="pageTitle">LensVault - ${image.fileName}</title>
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght@100..700&display=swap"
          rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/variables.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/menu.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/detail.css">

    <style>
        /* CSS cho tính năng đổi tên */
        .edit-name-btn {
            background: none;
            border: none;
            cursor: pointer;
            color: #666;
            margin-left: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 4px;
            padding: 4px;
            transition: 0.2s;
        }

        .edit-name-btn:hover {
            background: #f0f0f0;
            color: #000;
        }

        .edit-input {
            width: 100%;
            padding: 6px 10px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-family: 'Inter', sans-serif;
            font-size: 14px;
            margin-top: 4px;
            box-sizing: border-box;
        }

        .edit-actions {
            display: flex;
            gap: 8px;
            margin-top: 10px;
        }

        .btn-save {
            padding: 6px 12px;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 13px;
            font-weight: 500;
        }

        .btn-cancel {
            padding: 6px 12px;
            background: #e0e0e0;
            color: #333;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 13px;
            font-weight: 500;
        }

        /* PHẦN THÊM MỚI: CSS cho nút chuyển ảnh qua lại */
        .image-viewer {
            position: relative; /* Để định vị tuyệt đối 2 nút */
        }

        .nav-btn {
            position: absolute;
            top: 50%;
            transform: translateY(-50%);
            background: rgba(0, 0, 0, 0.4);
            color: white;
            border: none;
            width: 44px;
            height: 44px;
            border-radius: 50%;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: background 0.2s, transform 0.1s;
            z-index: 10; /* Đảm bảo luôn nằm trên ảnh */
            user-select: none;
        }

        .nav-btn:hover {
            background: rgba(0, 0, 0, 0.7);
            transform: translateY(-50%) scale(1.05);
        }

        .nav-btn:active {
            transform: translateY(-50%) scale(0.95);
        }

        .nav-btn.prev-btn {
            left: 20px;
        }

        .nav-btn.next-btn {
            right: 20px;
        }

        .user-link{
            color:#2563eb;
            text-decoration:none;
            font-weight:600;
        }

        .user-link:hover{
            text-decoration:underline;
        }

        .uploader-box{
            display:flex;
            align-items:center;
            gap:12px;
        }

        .uploader-avatar{
            width:42px;
            height:42px;
            border-radius:50%;
            background:#eef2ff;

            display:flex;
            align-items:center;
            justify-content:center;

            color:#2563eb;
            text-decoration:none;

            transition:.2s;
        }

        .uploader-avatar:hover{
            background:#dbeafe;
            transform:scale(1.05);
        }

        .uploader-avatar .material-symbols-outlined{
            font-size:28px;
        }

        .uploader-name{
            font-weight:600;
            color:#374151;
        }
    </style>
</head>

<body>
<jsp:include page="/user/menu.jsp"/>

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
            <button class="nav-btn prev-btn" onclick="navigateImage('prev')" title="Ảnh trước">
                <span class="material-symbols-outlined" style="font-size: 28px;">chevron_left</span>
            </button>

            <div id="imageWrapper">
                <%--
                    [UC09 - Bước 9.1.8]
                    Render ảnh chi tiết từ đối tượng image
                    được ImageDetailServlet chuyển sang View.
                --%>
                <img id="mainImage" src="${pageContext.request.contextPath}/uploads/${image.filePath}" alt="${image.fileName}">
            </div>

            <button class="nav-btn next-btn" onclick="navigateImage('next')" title="Ảnh sau">
                <span class="material-symbols-outlined" style="font-size: 28px;">chevron_right</span>
            </button>
        </div>
    </div>

    <div class="detail-sidebar">
        <div class="sidebar-header">

            <h2 id="headerFileName">${image.fileName}</h2>

            <div class="detail-info-row">

                <p>Thông tin chi tiết ảnh</p>

                <%--
                    [UC09 - Chức năng mở rộng]
                    Hiển thị số lượt tải xuống của ảnh.

                    Giá trị downloadCount được lấy từ:
                    ImageDao.findById()
                        -> mapRow()
                        -> image.downloadCount

                    Sau mỗi lần tải:
                    DownloadServlet
                        -> increaseDownloadCount()
                --%>
                <div class="download-badge">
            <span class="material-symbols-outlined">
                download
            </span>

                    <span>
                ${image.downloadCount} lượt tải
            </span>
                </div>

            </div>

        </div>

        <div class="sidebar-thumb" style="padding-top:24px;">
            <img src="${pageContext.request.contextPath}/uploads/${image.filePath}" alt="${image.fileName}">
        </div>

        <%--
            [UC09 - Bước 9.1.9]

            Hiển thị toàn bộ metadata ảnh:

            - Người upload
            - Ngày upload
            - Dung lượng
            - Tên file
            - Mô tả
            - Download Count

            Dữ liệu lấy từ đối tượng image
            và uploader được Servlet truyền sang.
        --%>
        <div class="sidebar-body">
            <%--
                [UC09 - Bước 9.1.6.1]
                Hiển thị thông tin người upload được lấy từ:
                ImageDetailServlet
                    -> UserService.getUserById()
                    -> UserDao.getUserById()

                Dữ liệu được lưu trong attribute:
                request.setAttribute("uploader", uploader);
            --%>
            <div class="info-item">
                <div class="info-content">
                    <p class="info-label">
                        Người upload
                    </p>
                    <div class="uploader-box">

                        <a href="${pageContext.request.contextPath}/UserProfile?id=${uploader.id}"
                           class="uploader-avatar">

                            <span class="material-symbols-outlined">
                                account_circle
                            </span>

                        </a>

                        <div class="uploader-info">

                        <span class="uploader-name">
                            ${uploader.fullName}
                        </span>

                        </div>

                    </div>
                </div>
            </div>

            <div class="divider"></div>

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
                <div class="info-content"
                     style="flex: 1; display: flex; justify-content: space-between; align-items: center;">
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
                    <input type="text" id="newFileNameInput" class="edit-input" value="${image.fileName}"/>
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

    /*
    [UC09 - Bước 9.1.6.2]

    Danh sách imageIds được lấy từ:

    ImageDetailServlet
        -> ImageService.getImageIdsByUserId()
        -> ImageDao.getImageIdsByUserId()

    Dùng để điều hướng ảnh trước/sau
    của cùng uploader.
    */
    const idList = [
        <c:forEach var="id" items="${imageIds}" varStatus="status">
        ${id}${!status.last ? ',' : ''}
        </c:forEach>
    ];

    /*
    [UC09 - Chức năng mở rộng]

    Điều hướng Previous / Next.

    Luồng:
    User Click
        -> navigateImage()
        -> xác định id kế tiếp
        -> redirect:
           /ImageDetail?id=...

    Servlet sẽ tải ảnh mới và render lại.
    */
    function navigateImage(direction) {

        const currentId = ${image.id};

        const currentIndex = idList.indexOf(currentId);

        let targetIndex;

        if(direction === 'next') {

            targetIndex =
                (currentIndex + 1) % idList.length;

        } else {

            targetIndex =
                (currentIndex - 1 + idList.length)
                % idList.length;
        }

        window.location =
            '${pageContext.request.contextPath}/ImageDetail?id='
            + idList[targetIndex];
    }

    // Xóa ảnh
    function confirmDelete(id) {
        if (confirm('Bạn có chắc muốn xóa ảnh này không?')) {
            fetch('${pageContext.request.contextPath}/DeleteImage?id=' + id, {method: 'POST'})
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
    const img = document.getElementById("mainImage");
    const wrapper = document.getElementById("imageWrapper");
    const viewer = document.querySelector(".image-viewer");

    let scale = 1;
    let panX = 0;
    let panY = 0;
    let isDragging = false;
    let lastMouseX, lastMouseY;

    function applyTransform() {
        wrapper.style.transform = "translate(" + panX + "px, " + panY + "px) scale(" + scale + ")";

        // In ra console để kiểm tra giá trị scale và tọa độ
        console.log("[Zoom/Pan] Scale: " + scale.toFixed(2) + "x | Pan (X: " + panX.toFixed(2) + "px, Y: " + panY.toFixed(2) + "px)");

        // In ra kích thước thực tế của ảnh sau khi bị biến đổi
        const rect = wrapper.getBoundingClientRect();
        console.log("[Zoom/Pan] Kích thước khung hiển thị thực: " + rect.width.toFixed(1) + "px x " + rect.height.toFixed(1) + "px");
    }

    function resetTransform() {
        scale = 1;
        panX = 0;
        panY = 0;
        applyTransform();
    }

    // Reset khi ảnh load xong
    img.addEventListener("load", resetTransform);

    // Nếu ảnh đã load xong trước khi sự kiện load được gán
    if (img.complete) {
        resetTransform();
    }

    // Zoom bằng wheel
    viewer.addEventListener("wheel", (e) => {
        e.preventDefault();

        const vr = viewer.getBoundingClientRect();

        // Tọa độ chuột so với viewer
        const mx = e.clientX - vr.left;
        const my = e.clientY - vr.top;

        // Tọa độ tâm của viewer (nơi wrapper được center mặc định)
        const cx = vr.width / 2;
        const cy = vr.height / 2;

        // Vector từ tâm viewer đến chuột
        const dx = mx - cx;
        const dy = my - cy;

        const zoomFactor = e.deltaY < 0 ? 1.1 : 0.9;
        const newScale = Math.min(10, Math.max(0.2, scale * zoomFactor));

        // Tỉ lệ thay đổi scale
        const ratio = newScale / scale;

        // Cập nhật panX, panY để giữ điểm dưới chuột đứng yên
        panX = dx - (dx - panX) * ratio;
        panY = dy - (dy - panY) * ratio;
        scale = newScale;

        img.style.cursor = scale > 1 ? "grab" : "zoom-in";
        applyTransform();
    }, { passive: false });

    // Drag để pan
    viewer.addEventListener("mousedown", (e) => {
        // NGĂN CHẶN SỰ KIỆN KHI CLICK VÀO NÚT CHUYỂN ẢNH
        if (e.target.closest('.nav-btn')) return;

        if (scale > 1) {
            isDragging = true;
            lastMouseX = e.clientX;
            lastMouseY = e.clientY;
            img.style.cursor = "grabbing";
            e.preventDefault(); // Ngăn hành vi drag ảnh mặc định của trình duyệt
        }
    });

    document.addEventListener("mousemove", (e) => {
        if (!isDragging) return;
        panX += e.clientX - lastMouseX;
        panY += e.clientY - lastMouseY;
        lastMouseX = e.clientX;
        lastMouseY = e.clientY;

        // Tạm thời tắt transition khi đang kéo để mượt hơn
        wrapper.style.transition = "none";
        applyTransform();
    });

    document.addEventListener("mouseup", () => {
        if (!isDragging) return;
        isDragging = false;
        img.style.cursor = scale > 1 ? "grab" : "zoom-in";
        // Bật lại transition
        wrapper.style.transition = "transform 0.05s ease-out";
    });

    // Xử lý khi chuột rời khỏi vùng viewer lúc đang kéo
    viewer.addEventListener("mouseleave", () => {
        if (isDragging) {
            isDragging = false;
            img.style.cursor = scale > 1 ? "grab" : "zoom-in";
            wrapper.style.transition = "transform 0.05s ease-out";
        }
    });

    // Double click reset
    viewer.addEventListener("dblclick", (e) => {
        if (e.target.closest('.nav-btn')) return;
        resetTransform();
        img.style.cursor = "zoom-in";
    });
</script>
</body>
</html>