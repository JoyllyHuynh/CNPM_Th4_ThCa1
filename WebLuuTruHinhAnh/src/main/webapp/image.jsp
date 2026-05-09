<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>LensVault - My Photos</title>

    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght@100..700&display=swap" rel="stylesheet"/>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/image.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/variables.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/menu.css">
</head>
<body>

<jsp:include page="/user/menu.jsp"/>
<jsp:include page="/user/header.jsp"/>

<div class="main-wrapper">
    <main class="main-canvas">
        <div class="page-header">
            <div class="left-title">
                <h1 class="page-title">My Photos</h1>
                <p class="page-subtitle">
                    ${not empty searchKeyword ?
                            'Kết quả tìm kiếm cho: '.concat(searchKeyword) :
                            'Browse, manage and organize your uploaded memories.'}
                </p>
            </div>

            <!-- Sorting -->
            <form class="right-sort" action="${pageContext.request.contextPath}/Photos" method="GET">
                <div class="sort-container">
                    <span class="material-symbols-outlined sort-icon">sort</span>
                    <select name="sortBy" class="sort-select" onchange="this.form.submit()">
                        <option value="newest" ${param.sortBy == 'newest' || empty param.sortBy ? 'selected' : ''}>Newest First</option>
                        <option value="oldest" ${param.sortBy == 'oldest' ? 'selected' : ''}>Oldest First</option>
                        <option value="nameAz" ${param.sortBy == 'nameAz' ? 'selected' : ''}>Name (A-Z)</option>
                        <option value="nameZa" ${param.sortBy == 'nameZa' ? 'selected' : ''}>Name (Z-A)</option>
                    </select>
                </div>
            </form>
        </div>

        <div class="photo-grid">
            <!-- Card Upload -->
            <div class="photo-upload-card" onclick="document.getElementById('photoInput').click()">
                <div class="create-icon-wrap">
                    <span class="material-symbols-outlined">upload</span>
                </div>
                <h3 class="create-title">Upload Photos</h3>
                <p class="create-desc">Add new memories to your cloud storage.</p>
                <input type="file" id="photoInput" multiple hidden/>
            </div>

            <!-- Danh sách ảnh -->
            <c:forEach var="img" items="${images}">
                <article class="photo-card">
                    <div class="photo-thumb">
                        <img src="${pageContext.request.contextPath}/uploads/${img.filePath}"
                             alt="${img.fileName}"
                             onerror="this.src='${pageContext.request.contextPath}/assets/img/placeholder.jpg'">

                        <div class="photo-actions">
                            <a href="${pageContext.request.contextPath}/DownloadServlet?id=${img.photoId}"
                               class="photo-action-btn"
                               title="Download">
                                <span class="material-symbols-outlined">download</span>
                            </a>
                            <button class="photo-action-btn" title="Delete" onclick="deletePhoto(${img.id})">
                                <span class="material-symbols-outlined">delete</span>
                            </button>
                        </div>
                    </div>
                    <div class="photo-body">
                        <h3 class="photo-name">${img.fileName}</h3>
                        <div class="photo-info">
                            <span class="photo-date">
                                <fmt:formatDate value="${img.uploadDate}" pattern="dd MMM yyyy"/>
                            </span>
                            <span class="photo-size">
                                <fmt:formatNumber value="${img.fileSize / (1024 * 1024)}" maxFractionDigits="1"/> MB
                            </span>
                        </div>
                        <c:if test="${not empty img.description}">
                            <p class="photo-desc">${img.description}</p>
                        </c:if>
                    </div>
                </article>
            </c:forEach>

            <!-- Trường hợp không có ảnh -->
            <c:if test="${empty images}">
                <div class="empty-state">
                    <span class="material-symbols-outlined empty-icon">image</span>
                    <h3>Chưa có ảnh nào</h3>
                    <p>Hãy tải lên một số ảnh để bắt đầu.</p>
                </div>
            </c:if>
        </div>
    </main>
</div>

</body>
</html>