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
                            <div class="sort-bar">

                                <a href="${pageContext.request.contextPath}/Photos?sortBy=newest"
                                   class="sort-chip ${empty currentSort || currentSort=='newest' ? 'active' : ''}">
                                    🕒 Mới nhất
                                </a>

                                <a href="${pageContext.request.contextPath}/Photos?sortBy=oldest"
                                   class="sort-chip ${currentSort=='oldest' ? 'active' : ''}">
                                    📅 Cũ nhất
                                </a>

                                <a href="${pageContext.request.contextPath}/Photos?sortBy=nameAz"
                                   class="sort-chip ${currentSort=='nameAz' ? 'active' : ''}">
                                    🔤 A → Z
                                </a>

                                <a href="${pageContext.request.contextPath}/Photos?sortBy=nameZa"
                                   class="sort-chip ${currentSort=='nameZa' ? 'active' : ''}">
                                    🔠 Z → A
                                </a>

                            </div>
                        </div>

                        <div class="photo-grid">
                            <!-- Upload Card -->
                            <form id="uploadForm"
                                  action="${pageContext.request.contextPath}/UploadImage"
                                  method="POST"
                                  enctype="multipart/form-data">

                                <div class="upload-photo-card">

                                    <input
                                            type="file"
                                            id="photoInput"
                                            name="photos"
                                            multiple
                                            hidden>

                                    <div class="upload-thumb"
                                         onclick="document.getElementById('photoInput').click()">

            <span class="material-symbols-outlined">
                add_photo_alternate
            </span>

                                    </div>

                                    <div class="upload-body">

                                        <h3 class="upload-title">
                                            Upload New Photo
                                        </h3>

                                        <div class="visibility-selector">

                                            <label class="visibility-option">

                                                <input type="radio"
                                                       name="visibility"
                                                       value="PUBLIC"
                                                       checked>

                                                🌍 Public

                                            </label>

                                            <label class="visibility-option">

                                                <input type="radio"
                                                       name="visibility"
                                                       value="PRIVATE">

                                                🔒 Private

                                            </label>

                                        </div>

                                    </div>

                                </div>

                            </form>

                            <!-- Danh sách ảnh -->
                            <c:forEach var="img" items="${images}">
                                <article class="photo-card">
                                    <div class="photo-thumb">
                                        <a href="${pageContext.request.contextPath}/ImageDetail?id=${img.id}"
                                           style="display:block; height:100%;">
                                            <img src="${pageContext.request.contextPath}/uploads/${img.filePath}"
                                                alt="${img.fileName}"
                                                onerror="this.onerror=null; this.style.background='#eee';">
                                        </a>

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
                                        <c:choose>

                                            <c:when test="${img.visibility == 'PRIVATE'}">
                                                <span class="privacy-badge private">
                                                    🔒 Private
                                                </span>
                                            </c:when>

                                            <c:otherwise>
                                                <span class="privacy-badge public">
                                                    🌍 Public
                                                </span>
                                            </c:otherwise>

                                        </c:choose>

                                        <div class="photo-info">

                                                <span class="photo-date">
                                                        ${img.uploadDate}
                                                </span>

                                            <span class="photo-size">
                                                    ${img.fileSize / 1048576 < 1 ?
                                                            fn:substring(String.valueOf(img.fileSize / 1024),0,4).concat(' KB')
                                                            :
                                                            fn:substring(String.valueOf(img.fileSize / 1048576),0,4).concat(' MB')}
                                            </span>

                                            <span class="photo-download">
                                                <span class="material-symbols-outlined">
                                                    download
                                                </span>
                                                ${img.downloadCount}
                                            </span>

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

                    document
                        .getElementById("photoInput")
                        .addEventListener("change", function () {

                            if(this.files.length > 0){

                                document
                                    .getElementById("uploadForm")
                                    .submit();
                            }
                        });
                </script>
            </body>

            </html>