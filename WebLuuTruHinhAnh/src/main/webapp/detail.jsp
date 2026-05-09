<%@ page contentType="text/html; charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <title>LensVault - ${image.fileName}</title>
            <link rel="preconnect" href="https://fonts.googleapis.com" />
            <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap"
                rel="stylesheet" />
            <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght@100..700&display=swap"
                rel="stylesheet" />
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/variables.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/menu.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/detail.css">
        </head>

        <body>
            <jsp:include page="/user/menu.jsp" />

            <div class="detail-layout">
                <!-- LEFT: Image -->
                <div class="detail-main">
                    <!-- Top bar -->
                    <div class="detail-topbar">
                        <a href="${pageContext.request.contextPath}/Photos" class="back-btn">
                            <span class="material-symbols-outlined" style="font-size:18px;">arrow_back</span>
                            Quay lại
                        </a>
                        <div class="topbar-actions">
                            <a href="${pageContext.request.contextPath}/DownloadServlet?id=${image.id}"
                                class="action-btn download">
                                <span class="material-symbols-outlined" style="font-size:18px;">download</span>
                                Tải xuống
                            </a>
                            <button class="action-btn delete" onclick="confirmDelete(${image.id})">
                                <span class="material-symbols-outlined" style="font-size:18px;">delete</span>
                                Xóa ảnh
                            </button>
                        </div>
                    </div>

                    <!-- Image viewer -->
                    <div class="image-viewer">
                        <img src="${pageContext.request.contextPath}/uploads/${image.filePath}" alt="${image.fileName}">
                    </div>
                </div>

                <!-- RIGHT: Info sidebar -->
                <div class="detail-sidebar">
                    <div class="sidebar-header">
                        <h2>${image.fileName}</h2>
                        <p>Thông tin chi tiết ảnh</p>
                    </div>

                    <!-- Thumbnail -->
                    <div class="sidebar-thumb" style="padding-top:24px;">
                        <img src="${pageContext.request.contextPath}/uploads/${image.filePath}" alt="${image.fileName}">
                    </div>

                    <div class="sidebar-body">
                        <!-- Ngày upload -->
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

                        <!-- Dung lượng -->
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

                        <!-- Tên file -->
                        <div class="info-item">
                            <div class="info-icon">
                                <span class="material-symbols-outlined">image</span>
                            </div>
                            <div class="info-content">
                                <p class="info-label">Tên file gốc</p>
                                <p class="info-value">${image.fileName}</p>
                            </div>
                        </div>

                        <!-- Mô tả (nếu có) -->
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
            </script>
        </body>

        </html>