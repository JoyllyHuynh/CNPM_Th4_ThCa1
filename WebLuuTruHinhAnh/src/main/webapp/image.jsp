<%@ page contentType="text/html; charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                <title>LensVault - My Photos</title>
                <link rel="preconnect" href="https://fonts.googleapis.com" />
                <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap"
                    rel="stylesheet" />
                <link
                    href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght@100..700&display=swap"
                    rel="stylesheet" />
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/image.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/header.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/variables.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/menu.css">
            </head>

            <body>
                <c:set var="activeTopNav" value="${empty activeTopNav ? 'photos' : activeTopNav}" />
                <jsp:include page="/user/menu.jsp" />
                <jsp:include page="/user/header.jsp" />

                <div class="main-wrapper">
                    <main class="main-canvas">
                        <div class="page-header">
                            <div class="left-title">
                                <h1 class="page-title">My Photos</h1>
                                <p class="page-subtitle">
                                    <c:choose>
                                        <c:when test="${not empty searchKeyword}">Kết quả tìm kiếm cho: ${searchKeyword}
                                        </c:when>
                                        <c:otherwise>Browse, manage and organize your uploaded memories.</c:otherwise>
                                    </c:choose>
                                </p>
                            </div>

                            <!-- Sorting -->
                            <form class="right-sort" action="${pageContext.request.contextPath}/Photos" method="GET">
                                <div class="sort-container">
                                    <span class="material-symbols-outlined sort-icon">sort</span>
                                    <select name="sortBy" class="sort-select" onchange="this.form.submit()">
                                        <option value="newest" ${currentSort=='newest' || empty currentSort ? 'selected'
                                            : '' }>Newest First</option>
                                        <option value="oldest" ${currentSort=='oldest' ? 'selected' : '' }>Oldest First
                                        </option>
                                        <option value="nameAz" ${currentSort=='nameAz' ? 'selected' : '' }>Name (A-Z)
                                        </option>
                                        <option value="nameZa" ${currentSort=='nameZa' ? 'selected' : '' }>Name (Z-A)
                                        </option>
                                    </select>
                                </div>
                            </form>
                        </div>

                        <div class="photo-grid">
                            <!-- Upload Card -->
                            <form id="uploadForm" action="${pageContext.request.contextPath}/UploadImage" method="POST"
                                enctype="multipart/form-data">
                                <div class="photo-upload-card" onclick="document.getElementById('photoInput').click()">
                                    <div class="create-icon-wrap">
                                        <span class="material-symbols-outlined">upload</span>
                                    </div>
                                    <h3 class="create-title">Upload Photos</h3>
                                    <p class="create-desc">Add new memories to your cloud storage.</p>
                                    <input type="file" id="photoInput" name="photos" multiple hidden
                                        accept="image/jpeg,image/png,image/gif,image/bmp,image/webp"
                                        onchange="document.getElementById('uploadForm').submit()" />
                                </div>
                            </form>

                            <!-- Danh sách ảnh -->
                            <c:forEach var="img" items="${images}">
                                <article class="photo-card">
                                    <div class="photo-thumb">
                                        <img src="${pageContext.request.contextPath}/uploads/${img.filePath}"
                                            alt="${img.fileName}"
                                            onerror="this.onerror=null; this.style.background='#eee';">

                                        <div class="photo-actions">
                                            <a href="${pageContext.request.contextPath}/DownloadServlet?id=${img.id}"
                                                class="photo-action-btn" title="Download">
                                                <span class="material-symbols-outlined">download</span>
                                            </a>
                                            <button class="photo-action-btn" title="Delete"
                                                onclick="deletePhoto(${img.id})">
                                                <span class="material-symbols-outlined">delete</span>
                                            </button>
                                        </div>
                                    </div>
                                    <div class="photo-body">
                                        <h3 class="photo-name">${img.fileName}</h3>
                                        <div class="photo-info">
                                            <span class="photo-date">${img.uploadDate}</span>
                                            <span class="photo-size">
                                                ${img.fileSize / 1048576 < 1 ? fn:substring(String.valueOf(img.fileSize
                                                    / 1024), 0, 4).concat(' KB') :
                                                    fn:substring(String.valueOf(img.fileSize / 1048576), 0, 4).concat('
                                                    MB')} </span>
                                        </div>
                                        <c:if test="${not empty img.description}">
                                            <p class="photo-desc">${img.description}</p>
                                        </c:if>
                                    </div>
                                </article>
                            </c:forEach>

                            <c:if test="${empty images}">
                                <div style="grid-column: 1/-1; text-align:center; padding: 60px 20px; color: #888;">
                                    <span class="material-symbols-outlined" style="font-size:48px;">photo_library</span>
                                    <p style="margin-top:12px;">Chưa có ảnh nào. Hãy upload ảnh đầu tiên!</p>
                                </div>
                            </c:if>
                        </div>
                    </main>
                </div>

                <script>
                    function deletePhoto(id) {
                        if (confirm('Bạn có chắc muốn xóa ảnh này?')) {
                            fetch('${pageContext.request.contextPath}/DeleteImage?id=' + id, { method: 'POST' })
                                .then(() => location.reload());
                        }
                    }
                </script>
            </body>

            </html>