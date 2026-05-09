<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en" class="light">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>LensVault - Album Management</title>

    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet" />
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&display=swap" rel="stylesheet" />

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user/albums.css">
</head>
<body>

<%-- 1. Sidebar cố định bên trái (Bên ngoài wrapper) --%>
<jsp:include page="menu.jsp" />

<%-- 2. Khối nội dung chính bên phải --%>
<div class="main-wrapper">

    <%-- 3. Header phải nằm trên cùng của wrapper để tràn 100% --%>
    <jsp:include page="header.jsp" />

    <%-- 4. Main content --%>
    <main class="main-canvas" id="mainContent">

        <%-- Page header: Title & Sort --%>
        <div class="page-header">
            <div>
                <h1 class="page-title">My Albums</h1>
                <p class="page-subtitle">Organize and manage your curated collections.</p>
            </div>

            <div style="display:flex; align-items:center; gap:12px; position: relative;">
                <button class="btn-sort" id="sortBtn" onclick="toggleSortMenu()">
                    <span class="material-symbols-outlined" style="font-size:18px">filter_list</span>
                    Sort by: <span id="sortLabel">${not empty sortLabel ? sortLabel : 'Date'}</span>
                </button>

                <div id="sortMenu" role="menu" class="sort-dropdown" style="display:none;">
                    <c:forEach var="option" items="${['Date','Name','Size','Item Count']}">
                        <button onclick="applySort('${option}')">${option}</button>
                    </c:forEach>
                </div>
            </div>
        </div>

        <%-- Album Grid --%>
        <div class="album-grid" id="albumGrid">
            <%-- Nút tạo album mới --%>
            <div class="album-card-create" onclick="openCreateAlbumModal()">
                <div class="create-icon-wrap">
                    <span class="material-symbols-outlined" style="font-size:32px">add</span>
                </div>
                <h3 class="create-title">Create New Album</h3>
                <p class="create-desc">Group photos together to tell a story.</p>
            </div>

            <%-- Danh sách Album --%>
            <c:choose>
                <c:when test="${not empty albums}">
                    <c:forEach var="album" items="${albums}">
                        <article class="album-card" data-album-id="${album.id}">
                            <div class="album-thumb">
                                <img src="${not empty album.coverUrl ? album.coverUrl : pageContext.request.contextPath.concat('/assets/img/placeholder.jpg')}" alt="${album.name}" />
                                <div class="album-actions">
                                    <button class="album-action-btn edit" onclick="editAlbum(${album.id}, event)">
                                        <span class="material-symbols-outlined">edit</span>
                                    </button>
                                    <button class="album-action-btn delete" onclick="deleteAlbum(${album.id}, '${album.name}', event)">
                                        <span class="material-symbols-outlined">delete</span>
                                    </button>
                                </div>
                            </div>
                            <div class="album-body">
                                <h3 class="album-name">${album.name}</h3>
                                <div class="album-footer">
                                    <span class="album-count">${album.itemCount} items</span>
                                </div>
                            </div>
                        </article>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <span class="material-symbols-outlined" style="font-size:64px;">photo_library</span>
                        <p>No albums yet. Create your first one!</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </main>
</div>

<%-- Modal tạo Album --%>
<div id="createAlbumModal" class="modal-overlay" style="display:none;">
    <div class="modal-content">
        <h2>Create New Album</h2>
        <form id="createAlbumForm" onsubmit="return submitCreateAlbum(event)">
            <div class="form-group">
                <label>Album name *</label>
                <input id="albumNameInput" name="albumName" type="text" required placeholder="e.g. Summer 2024" />
            </div>
            <div class="modal-actions">
                <button type="button" onclick="closeCreateAlbumModal()">Cancel</button>
                <button type="submit" class="btn-primary">Create Album</button>
            </div>
        </form>
    </div>
</div>

<script>
    // JS được bọc trong DOMContentLoaded để tránh lỗi null khi Header/Menu chưa kịp render
    document.addEventListener('DOMContentLoaded', function() {
        const sidebar = document.getElementById('sidebar');
        const overlay = document.getElementById('sidebarOverlay');
        const menuToggleBtn = document.getElementById('menuToggleBtn');

        if (menuToggleBtn) {
            menuToggleBtn.addEventListener('click', () => {
                sidebar.classList.toggle('open');
                overlay.classList.toggle('open');
            });
        }
        if (overlay) overlay.addEventListener('click', () => {
            sidebar.classList.remove('open');
            overlay.classList.remove('open');
        });
    });

    /* Các hàm xử lý giao diện */
    function toggleSortMenu() {
        const menu = document.getElementById('sortMenu');
        menu.style.display = menu.style.display === 'block' ? 'none' : 'block';
    }

    function applySort(label) {
        const url = new URL(window.location.href);
        url.searchParams.set('sort', label.toLowerCase().replace(' ', '_'));
        window.location.href = url.toString();
    }

    function openCreateAlbumModal() {
        document.getElementById('createAlbumModal').style.display = 'flex';
    }

    function closeCreateAlbumModal() {
        document.getElementById('createAlbumModal').style.display = 'none';
    }

    async function submitCreateAlbum(e) {
        e.preventDefault();
        const name = document.getElementById('albumNameInput').value.trim();
        const res = await fetch('${pageContext.request.contextPath}/albums/create', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ albumName: name })
        });
        if (res.ok) window.location.reload();
        return false;
    }

    function editAlbum(id, e) {
        e.stopPropagation();
        window.location.href = '${pageContext.request.contextPath}/albums/' + id + '/edit';
    }

    async function deleteAlbum(id, name, e) {
        e.stopPropagation();
        if (!confirm('Delete "' + name + '"?')) return;
        const res = await fetch('${pageContext.request.contextPath}/albums/' + id, { method: 'DELETE' });
        if (res.ok) window.location.reload();
    }
</script>
</body>
</html>